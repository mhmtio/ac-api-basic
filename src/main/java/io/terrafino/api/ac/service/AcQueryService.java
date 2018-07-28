package io.terrafino.api.ac.service;

import com.ac.api.data.model.*;
import com.ac.api.data.timeseries.*;
import com.ac.api.error.ServerException;
import com.ac.api.query.*;
import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.ado.Ado;
import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.timeseries.TsHeader;
import io.terrafino.api.ac.timeseries.TsRecord;
import io.terrafino.api.ac.timeseries.TsRecordFactory;
import io.terrafino.api.ac.timeseries.TsRecords;
import io.terrafino.api.ac.validation.TsValidation;
import io.terrafino.api.ac.validation.TsValidationInfo;
import io.terrafino.api.ac.value.Value;

import java.util.*;
import java.util.stream.Collectors;

public class AcQueryService {

    private AcConnection conn;

    public AcQueryService(AcConnection conn) {
        this.conn = conn;
    }

    public boolean exists(Ado ado) throws AcException {
        return adoBrowse(String.format("symbol = '%s'", ado.getId())).size() == 1;
    }

    public List<Ado> adoBrowse(String query) throws AcException {
        AcService ac = new AcService(conn);
        List<Ado> ados = new ArrayList<>();
        try {
            AdoBrowseQuery adoBrowseQuery = conn.queryFactory.createAdoBrowseQuery(query, false, false, false);
            Optional<AdoBrowseResult> result = Optional.ofNullable(conn.serverInterface.adoBrowseQuery(adoBrowseQuery));
            result.ifPresent(res ->
                    res.forEach(ado ->
                            ados.add(ac.fromAcAdo(ado))
                    )
            );
        } catch (ServerException e) {
            throw new AcException(String.format("Could not execute adoBrowse: %s", query), e);
        }
        return ados;
    }

    public List<Value> load(Ado ado, List<String> attrs) throws AcException {
        List<StaticAttributeId> attributeIds = attrs.stream().map(StaticAttributeId::create).collect(Collectors.toList());
        AdoQuery adoQuery = conn.queryFactory.createAdoQuery(AdoId.create(ado.getId()), attributeIds, null, null, null, DerivedOptions.EVALUATE_DERIVEDS);
        try {
            AdoResult adoResult = conn.serverInterface.adoQuery(adoQuery);
            return adoResult.getAdo().getAttributeValues().stream().map((acValue) -> new Value(acValue.getValue())).collect(Collectors.toList());
        } catch (ServerException e) {
            throw new AcException(String.format("Could not load %s::%s", ado.getId(), attrs), e);
        }
    }

    public List<List<String>> executeFormulaEngineQuery(String fe) throws AcException {
        try {
            FormulaEngineQuery query = conn.queryFactory.createFormulaEngineQuery(fe, null, null, null);
            FormulaEngineResult result = conn.serverInterface.formulaEngineQuery(query);
            if (Optional.ofNullable(result).isPresent() && Optional.ofNullable(result.getValueMatrix()).isPresent()) {
                return result.getValueMatrix().getRowValues().stream().map(
                        row -> {
                            List<String> rowRes = new ArrayList<>();
                            row.iterator().forEachRemaining(e -> rowRes.add(e.toString()));
                            return rowRes;
                        }
                ).collect(Collectors.toList());
            } else {
                return Collections.singletonList(Collections.emptyList());
            }
        } catch (ServerException e) {
            throw new AcException(String.format("Could not execute FE query: %s", fe));
        }
    }

    public TsRecords loadTimeseries(Ado ado, String tree, int start, int end, Attributes attributes)
            throws AcException {
        RecordRange range = getRecordRange(conn, start, end);
        return loadTimeseries(ado, tree, range, attributes);
    }

    public Optional<TsHeader> getTimeseriesHeader(Ado ado, String tree) throws AcException {
        try {
            TimeSeriesResults res = conn.serverInterface.timeSeriesQuery(createHeaderQuery(ado, tree));
            if (res != null && res.size() > 0 &&
                    res.getTimeSeriesResult(0).getTimeSeries() != null &&
                    res.getTimeSeriesResult(0).getTimeSeries().getInfo() != null) {

                TimeSeriesInfo info = res.getTimeSeriesResult(0).getTimeSeries().getInfo();
                TimeSeriesDefinition definition = info.getDefinition();

                TimeSeriesValidation validation = definition.getValidation();
                CalendarNumber calendarNumber = validation.getCalendarNumber();
                CheckFunctionMask checkMask = validation.getCheckMask();
                Double percentage = validation.getPercentage();
                Double stdevFactor = validation.getMultiplicationStdDevLogReturn();
                Integer stdevLen = validation.getRangeStdDevLogReturn();
                String formula = validation.getFormula();

                TsValidationInfo validationInfo = new TsValidationInfo(calendarNumber, checkMask, percentage,
                        stdevFactor, stdevLen, formula, validation);
                Security security = definition.getSecurity();
                Boolean isIntraDay = definition.isIntraDay();
                Boolean isIntraSecond = definition.isIntraSecond();
                List<TimeSeriesAttributeNumber> attributeNumbers = definition.getAttributeNumbers();

                return Optional.of(new TsHeader(security, isIntraDay, isIntraSecond, attributeNumbers, validationInfo));
            } else {
                return Optional.empty();
            }
        } catch (ServerException e) {
            throw new AcException(String.format("Could not determine header of %s / %s", tree, ado.getId()), e);
        }
    }

    public Optional<List<TsValidation>> getTsValidations(Ado ado, String tree)
            throws AcException {
        Optional<List<TsValidation>> tsValidations = getTimeseriesHeader(ado, tree).map(TsHeader::getValidationInfo).map(TsValidationInfo::getValidations);
        if (tsValidations.isPresent() && tsValidations.get().size() > 0) {
            return tsValidations;
        } else return Optional.empty();
    }


    private TimeSeriesQuery createHeaderQuery(Ado ado, String tree) {
        List<AdoId> ados = Collections.singletonList(AdoId.create(ado.getId()));
        TimeSeriesStoreId tsStore = TimeSeriesStoreId.create(tree);
        TimeSeriesOptions options = conn.queryFactory.createTimeSeriesOptions();
        RecordRange range = conn.queryFactory.createRecordRange(1, 0);
        RecordCorrection correction = conn.queryFactory.createRecordCorrection(false);
        RecordFilter filter = conn.queryFactory.createRecordFilter(DayMask.ALL_DAYS, HourMask.ALL_HOURS);
        RecordAlignment alignment = conn.queryFactory.createRecordAlignment(false);
        return conn.queryFactory.createTimeSeriesQuery(tsStore, ados, null, options, 0, range, correction, filter, alignment);
    }

    private TsRecords loadTimeseries(Ado ado, String tree,
                                     RecordRange range, Attributes attributes) throws AcException {
        List<AdoId> ados = Collections.singletonList(AdoId.create(ado.getId()));
        TimeSeriesAttributeNumber[] attributeNumbers = getAttributeNumbers(attributes);
        TimeSeriesOptions timeSeriesOptions = conn.queryFactory.createTimeSeriesOptions(
                TimeseriesOption.RETURN_NO_DEFINITION);
        RecordCorrection enableRecordCorrection = conn.queryFactory.createRecordCorrection(true);
        RecordFilter recordFilter = conn.queryFactory.createRecordFilter(DayMask.ALL_DAYS, HourMask.ALL_HOURS);
        RecordAlignment recordAlignment = conn.queryFactory.createRecordAlignment(false);
        TimeSeriesQuery query = conn.queryFactory.createTimeSeriesQuery(TimeSeriesStoreId.create(tree), ados, attributeNumbers, timeSeriesOptions,
                null, range, enableRecordCorrection, recordFilter, recordAlignment);
        try {
            TimeSeriesResults results = conn.serverInterface.timeSeriesQuery(query);
            if (Optional.ofNullable(results).isPresent() && results.size() == 1) {
                return TsRecordFactory.fromAc(results.getTimeSeriesResult(0).getTimeSeries().getRecords(), attributes, attributeNumbers);
            } else {
                return new TsRecords(new ArrayList<>());
            }
        } catch (ServerException e) {
            throw new AcException(String.format("Could not load timeseries of %s / %s / %s", ado.getId(), tree, attributes), e);
        }
    }

    private TimeSeriesAttributeNumber[] getAttributeNumbers(Attributes attributes) throws AcException {
        List<TimeSeriesAttributeNumber> attributeNumbers = new ArrayList<>();
        for (String attr : attributes.getAll()) {
            attributeNumbers.add(conn.getMetaService().getTimeseriesAttributeNumber(attr));
        }
        return attributeNumbers.toArray(new TimeSeriesAttributeNumber[attributeNumbers.size()]);
    }

    private RecordRange getRecordRange(AcConnection conn, int start, int end) {
        return conn.queryFactory.createRecordRange(start, end);
    }
}
