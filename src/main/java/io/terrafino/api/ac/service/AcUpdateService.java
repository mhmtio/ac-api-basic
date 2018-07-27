package io.terrafino.api.ac.service;

import com.ac.api.data.model.*;
import com.ac.api.data.statics.AdoMaster;
import com.ac.api.data.statics.StaticAttributeValue;
import com.ac.api.data.timeseries.CheckFunctionMask;
import com.ac.api.data.timeseries.TimeSeriesDefinition;
import com.ac.api.data.timeseries.TimeSeriesRecord;
import com.ac.api.data.timeseries.TimeSeriesValidation;
import com.ac.api.data.value.TimestampValue;
import com.ac.api.data.value.Value;
import com.ac.api.data.value.ValueList;
import com.ac.api.transaction.*;
import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.ado.Ado;
import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.timeseries.TsHeader;
import io.terrafino.api.ac.timeseries.TsRecord;
import io.terrafino.api.ac.timeseries.TsRecords;
import io.terrafino.api.ac.utils.TimeUtils;
import io.terrafino.api.ac.validation.TsValidation;
import io.terrafino.api.ac.validation.TsValidationInfo;
import io.terrafino.api.ac.validation.TsValidationNone;
import io.terrafino.api.ac.value.ValueFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


public class AcUpdateService {

    private AcConnection conn;

    public AcUpdateService(AcConnection conn) {
        this.conn = conn;
    }

    public void create(Ado ado) throws AcException {
        AdoId id = AdoId.create(ado.getId());
        ObjectOperation operation = conn.transactionFactory.createAdoOperation(
                OperationType.ADD, id, ado.getLongname(), ListId.create(ado.getTemplate()), null, null, null);
        ObjectTransaction transaction = new ObjectTransaction(conn.transactionFactory.createTransaction(operation));
        conn.executeTransaction(transaction, String.format("Could not createInAc Ado %s!", ado.getId()));
    }

    public void delete(Ado ado) throws AcException {
        AdoMaster adoMaster = conn.staticFactory.createAdoMaster(AdoId.create(ado.getId()), null, null, null);
        com.ac.api.data.statics.Ado acAdo = conn.staticFactory.createAdo(adoMaster, null, null, null);
        com.ac.api.transaction.ObjectTransaction acTransaction = conn.transactionFactory.createAdoTransaction(OperationType.DELETE, true, acAdo);
        ObjectTransaction transaction = new ObjectTransaction(acTransaction);
        conn.executeTransaction(transaction, String.format("Could not delete Ado %s!", ado.getId()));
    }

    public void store(Ado ado) throws AcException {
        AdoId adoId = AdoId.create(ado.getId());
        List<ObjectOperation> operations = ado.getAttributes().getAll().stream().map((attr) -> {
                    StaticAttributeId attrId = StaticAttributeId.create(attr);
                    com.ac.api.data.value.Value value = ado.get(attr).getAcValue();
                    List<StaticAttributeValue> data = Collections.singletonList(conn.staticFactory.createAttributeValue(attrId, value, null, null, null));
                    return conn.transactionFactory.createAdoAttributesOperation(OperationType.ADD, adoId, data, null);
                }
        ).collect(Collectors.toList());
        ObjectTransaction transaction = new ObjectTransaction(conn.transactionFactory.createTransaction(operations));
        conn.executeTransaction(transaction, String.format("Could not store Ado %s!", ado.getId()));
    }

    public void storeTimeseries(Ado ado, String tree, TsRecords records) throws AcException {
        Attributes attributes = records.getAttributes();
        List<TimeSeriesAttributeNumber> timeseriesAttributeNumbers = conn.getMetaService().getTimeseriesAttributeNumbers(attributes);
        Optional<TsHeader> timeseriesHeader = ado.getTimeseriesHeader(tree);
        if (!timeseriesHeader.isPresent() || !timeseriesHeader.get().getAttributeNumbers().containsAll(timeseriesAttributeNumbers)) {
            amendTimeseriesHeaderWith(ado, tree, attributes);
        }
        TimeSeriesChecks checks = TimeSeriesChecks.create(TimeSeriesChecks.Check.PERFORM_CHECKS);
        List<TimeSeriesRecordOperation> operations = new ArrayList<>();
        for (TsRecord record : records.getRecords()) {
            ValueList<Value> acValueList = ValueFactory.toAcValueList(record.getValues());
            TimestampValue timestampValue = TimestampValue.create(record.getTimestamp());
            TimeSeriesRecord acRecord = conn.timeseriesFactory.createRecord(timestampValue, timeseriesAttributeNumbers, acValueList);
            TimeSeriesRecordOperation operation = conn.transactionFactory.createRecordOperation(OperationType.ADD, checks, acRecord);
            operations.add(operation);
        }
        TimeseriesTransaction transaction = new TimeseriesTransaction(conn.transactionFactory.createTransaction(AdoId.create(ado.getId()),
                TimeSeriesStoreId.create(tree), operations, null));
        conn.executeTransaction(transaction, String.format("Could not store timeseries for %s / %s", tree, ado.getId()));
    }

    public void deleteTimeseries(Ado ado, String tree) throws AcException {
        TimeSeriesDefinitionOperation operation = conn.transactionFactory.createDefinitionOperation(OperationType.DELETE);
        AdoId adoId = AdoId.create(ado.getId());
        TimeSeriesStoreId tsStoreId = TimeSeriesStoreId.create(tree);
        TimeseriesTransaction transaction = new TimeseriesTransaction(conn.transactionFactory.createTransaction(adoId, tsStoreId, operation, null));
        conn.executeTransaction(transaction, String.format("Could not delete timeseries for %s / %s", tree, ado.getId()));
    }

    public void revalidate(Ado ado, String tree, int startDate, int startTime, int endDate, int endTime)
            throws AcException {
        TimeSeriesChecks checks = TimeSeriesChecks.create(TimeSeriesChecks.Check.PERFORM_CHECKS);
        TimestampValue startTimestamp = TimestampValue.create(TimeUtils.getTimestamp(startDate, startTime));
        TimestampValue endTimestamp = TimestampValue.create(TimeUtils.getTimestamp(endDate, endTime));
        TimeSeriesRecordOperation operation = conn.transactionFactory.createRevalidateOperation(checks, startTimestamp, endTimestamp);
        TimeseriesTransaction transaction = new TimeseriesTransaction(
                conn.transactionFactory.createTransaction(AdoId.create(ado.getId()), TimeSeriesStoreId.create(tree), operation));
        conn.executeTransaction(transaction, String.format("Could not revalidate timeseries for %s / %s", tree, ado.getId()));
    }

    public void amendTimeseriesHeaderWith(Ado ado, String tree, Attributes attributes) throws AcException {
        AdoId adoId = AdoId.create(ado.getId());
        TimeSeriesStoreId storeId = TimeSeriesStoreId.create(tree);
        Optional<TsHeader> possHeader = conn.getQueryService().getTimeseriesHeader(ado, tree);
        Security security = possHeader.map(TsHeader::getSecurity).orElse(Security.create(null, null, null));
        TimeSeriesValidation validation = possHeader.map(h -> h.getValidationInfo().getAcValidation()).orElse(null);
        List<TimeSeriesAttributeNumber> attrNums = conn.getMetaService().getAttributeNumbersFor(attributes);
        TimeSeriesDefinition definition = conn.timeseriesFactory.createDefinition(security, validation, true, false, attrNums);
        TimeSeriesDefinitionOperation operation = conn.transactionFactory.createExtendDefinitionOperation(definition);
        TimeseriesTransaction transaction = new TimeseriesTransaction(conn.transactionFactory.createTransaction(adoId, storeId, operation, null));
        conn.executeTransaction(transaction, String.format("Could not amend header of %s / %s", tree, ado.getId()));
    }

    public void removeAllTimeseriesValidations(Ado ado, String tree) throws AcException {
        addOrRemoveTimeseriesValidation(TsValidation::removeAllFormulas, TsValidation::removeAllChecks, ado, tree, new TsValidationNone());
    }

    public void removeTimeseriesValidation(Ado ado, String tree, TsValidation valFunc) throws AcException {
        addOrRemoveTimeseriesValidation(TsValidation::removeFormula, TsValidation::removeCheck, ado, tree, valFunc);
    }

    public void addTimeseriesValidation(Ado ado, String tree, TsValidation valFunc) throws AcException {
        addOrRemoveTimeseriesValidation(TsValidation::appendFormula, TsValidation::appendCheck, ado, tree, valFunc);
    }

    private void addOrRemoveTimeseriesValidation(BiFunction<String, String, String> amendFormula,
                                                 BiFunction<CheckFunctionMask, CheckFunctionMask.CheckFunction, CheckFunctionMask> amendChecks,
                                                 Ado ado, String tree, TsValidation validation) throws AcException {
        TsHeader tsHeader = conn.getQueryService().getTimeseriesHeader(ado, tree).orElseThrow(() -> new AcException(String.format("%s has no header in %s!", ado.getId(), tree)));
        TsValidationInfo newValidationInfo = validation.mergeWith(tsHeader.getValidationInfo(), amendFormula, amendChecks);

        TimeSeriesValidation newValidation = conn.timeseriesFactory.createValidation(newValidationInfo.getCalendarNumber(),
                newValidationInfo.getCheckFunctionMask(), newValidationInfo.getPercentage(),
                newValidationInfo.getStdevFactor(), newValidationInfo.getStdevLen(), newValidationInfo.getFormula());
        TimeSeriesDefinition definition = conn.timeseriesFactory.createDefinition(tsHeader.getSecurity(), newValidation,
                tsHeader.isIntraDay(), tsHeader.isIntraSecond(), tsHeader.getAttributeNumbers());
        TimeSeriesDefinitionOperation operation = conn.transactionFactory.createExtendDefinitionOperation(definition);
        TimeseriesTransaction transaction = new TimeseriesTransaction(conn.transactionFactory.createTransaction(AdoId.create(ado.getId()),
                TimeSeriesStoreId.create(tree), operation, null));
        conn.executeTransaction(transaction, String.format("Could not amend validation functions of %s / %s", tree, ado.getId()));
    }

}
