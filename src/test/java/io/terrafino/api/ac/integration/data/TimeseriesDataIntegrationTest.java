package io.terrafino.api.ac.integration.data;

import com.ac.api.data.model.TimeSeriesAttributeNumber;
import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.timeseries.TsHeader;
import io.terrafino.api.ac.timeseries.TsRecord;
import io.terrafino.api.ac.timeseries.TsRecords;
import io.terrafino.api.ac.validation.*;
import io.terrafino.api.ac.value.ValueFactory;
import io.terrafino.api.ac.value.Values;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class TimeseriesDataIntegrationTest extends StaticDataIntegrationTest {

    private static final String TREE = "CONSOLIDATION_C0";
    private static final Attributes ATTRIBUTES = new Attributes("CLOSE");

    @Override
    public void createTestAdo() throws AcException {
        ado = ac.testAdoWithPrefix("C0.TFN").withTemplate("C0_I_T001_LSA").createInAc();
    }

    @Test
    public void canExtendHeader() throws Exception {
        Optional<TsHeader> potentialHeader = ado.getTimeseriesHeader(TREE);
        assertThat(potentialHeader, isEmpty());

        ado.amendTimeseriesHeaderWith(TREE, ATTRIBUTES);
        potentialHeader = ado.getTimeseriesHeader(TREE);

        assertThat(potentialHeader, isPresent());
        assertThat(potentialHeader.get().getAttributeNumbers(), is(Collections.singletonList(TimeSeriesAttributeNumber.CLOSE)));
    }

    @Test
    public void canStoreAndLoadTimeseries() throws Exception {
        TsRecord record = new TsRecord(20180101, 235959, ATTRIBUTES, new Values(ValueFactory.createValue(100.0)));
        TsRecords records = new TsRecords(record);
        ado.storeTimeseries(TREE, records);
        TsRecords tsRecords = ado.loadTimeseries(TREE, 20180101, 20180101, new Attributes("CLOSE"));
        assertThat(tsRecords, is(records));
    }


    @Test
    public void canAddValidationFunction() throws Exception {
        ado.amendTimeseriesHeaderWith(TREE, ATTRIBUTES);
        ado.addTimeseriesValidation(TREE, new TsValidationNonPositive());
        Optional<List<TsValidation>> tsValidations = ado.getTimeseriesValidations(TREE);
        assertThat(tsValidations, isPresent());
        assertThat(tsValidations.get().get(0), instanceOf(TsValidationNonPositive.class));
    }

    @Test
    public void storingTimeseriesDataTriggersValidation() throws Exception {
        ado.amendTimeseriesHeaderWith(TREE, ATTRIBUTES);
        ado.addTimeseriesValidation(TREE, new TsValidationNonPositive());
        TsRecord record = new TsRecord(20180101, 235959, ATTRIBUTES, new Values(ValueFactory.createValue(-100.0)));
        TsRecords records = new TsRecords(record);

        ado.storeTimeseries(TREE, records);

        TsRecords validatedRecords = ado.loadTimeseries(TREE, 0, 0, ATTRIBUTES);
        assertThat(validatedRecords.getRecords().get(0).get(0).getStatus(), is(130));
    }
    @Test
    public void canRevalidateTimeseries() throws Exception {
        TsRecord record = new TsRecord(20180101, 235959, ATTRIBUTES, new Values(ValueFactory.createValue(-100.0)));
        TsRecords records = new TsRecords(record);

        ado.storeTimeseries(TREE, records);

        TsRecords unvalidatedRecords = ado.loadTimeseries(TREE, 0, 0, ATTRIBUTES);
        assertThat(unvalidatedRecords.getRecords().get(0).get(0).getStatus(), is(1));

        ado.addTimeseriesValidation(TREE, new TsValidationNonPositive());
        ado.revalidate(TREE, 20180101, 20180101);

        TsRecords validatedRecords = ado.loadTimeseries(TREE, 0, 0, ATTRIBUTES);
        assertThat(validatedRecords.getRecords().get(0).get(0).getStatus(), is(130));
    }

    @Test
    public void canRemoveAllTimeseriesValidations() throws Exception {
        ado.amendTimeseriesHeaderWith(TREE, ATTRIBUTES);
        ado.addTimeseriesValidation(TREE, new TsValidationNonPositive());
        ado.addTimeseriesValidation(TREE, new TsValidationBMA());
        Optional<List<TsValidation>> tsValidations = ado.getTimeseriesValidations(TREE);
        assertThat(tsValidations, isPresent());

        ado.removeAllTimeseriesValidations(TREE);
        tsValidations = ado.getTimeseriesValidations(TREE);
        assertThat(tsValidations, isEmpty());
    }

    @Test
    public void canRemoveIndividualTimeseriesValidation() throws Exception {
        ado.amendTimeseriesHeaderWith(TREE, ATTRIBUTES);
        ado.addTimeseriesValidation(TREE, new TsValidationNonPositive());
        ado.addTimeseriesValidation(TREE, new TsValidationBMA());
        Optional<List<TsValidation>> tsValidations = ado.getTimeseriesValidations(TREE);
        assertThat(tsValidations, isPresent());
        assertThat(tsValidations.get().size(), is(2));
        assertThat(tsValidations.get().get(0), is(instanceOf(TsValidationBMA.class)));
        assertThat(tsValidations.get().get(1), is(instanceOf(TsValidationNonPositive.class)));

        ado.removeTimeseriesValidation(TREE, new TsValidationBMA());
        tsValidations = ado.getTimeseriesValidations(TREE);
        assertThat(tsValidations, isPresent());
        assertThat(tsValidations.get().size(), is(1));
        assertThat(tsValidations.get().get(0), is(instanceOf(TsValidationNonPositive.class)));
    }

    @Test
    public void canAddAndRemoveFormulaTimeseriesValidation() throws Exception {
        ado.amendTimeseriesHeaderWith(TREE, ATTRIBUTES);
        ado.addTimeseriesValidation(TREE, new TsValidationFormula("always_suspect();"));
        TsRecord record = new TsRecord(20180101, 235959, ATTRIBUTES, new Values(ValueFactory.createValue(100.0)));
        TsRecords records = new TsRecords(record);

        ado.storeTimeseries(TREE, records);

        TsRecords validatedRecords = ado.loadTimeseries(TREE, 20180101, 20180101, ATTRIBUTES);
        assertThat(validatedRecords.getRecords().get(0).get(0).getStatus(), is(132));

        ado.removeTimeseriesValidation(TREE, new TsValidationFormula("always_suspect();"));
        ado.revalidate(TREE, 20180101, 20180101);

        TsRecords unvalidatedRecords = ado.loadTimeseries(TREE, 20180101, 20180101, ATTRIBUTES);
        assertThat(unvalidatedRecords.getRecords().get(0).get(0).getStatus(), is(1));
    }

    @Test
    public void canRevalidateAllTimeseries() throws Exception {
        ado.amendTimeseriesHeaderWith(TREE, ATTRIBUTES);
        TsRecord record1 = new TsRecord(20180101, 235959, ATTRIBUTES, new Values(ValueFactory.createValue(100.0)));
        TsRecord record2 = new TsRecord(20180102, 235959, ATTRIBUTES, new Values(ValueFactory.createValue(200.0)));
        TsRecords records = new TsRecords(record1, record2);
        ado.storeTimeseries(TREE, records);

        ado.addTimeseriesValidation(TREE, new TsValidationFormula("always_suspect();"));
        ado.revalidate(TREE);

        TsRecords validatedRecords = ado.loadTimeseries(TREE, 20180101, 20180102, ATTRIBUTES);
        assertThat(validatedRecords.getRecords().get(0).get(0).getStatus(), is(132));
        assertThat(validatedRecords.getRecords().get(1).get(0).getStatus(), is(132));
    }

    @Test
    public void canRevalidateSelectedTimeseries() throws Exception {
        ado.amendTimeseriesHeaderWith(TREE, ATTRIBUTES);
        TsRecord record1 = new TsRecord(20180101, 235959, ATTRIBUTES, new Values(ValueFactory.createValue(100.0)));
        TsRecord record2 = new TsRecord(20180102, 235959, ATTRIBUTES, new Values(ValueFactory.createValue(200.0)));
        TsRecords records = new TsRecords(record1, record2);
        ado.storeTimeseries(TREE, records);

        ado.addTimeseriesValidation(TREE, new TsValidationFormula("always_suspect();"));
        ado.revalidate(TREE, 20180101, 20180101);

        TsRecords partiallyValidatedRecords = ado.loadTimeseries(TREE, 20180101, 20180102, ATTRIBUTES);
        assertThat(partiallyValidatedRecords.getRecords().get(0).get(0).getStatus(), is(132));
        assertThat(partiallyValidatedRecords.getRecords().get(1).get(0).getStatus(), is(1));
    }

    @Override
    protected void ensureAdoIsDeleted() throws AcException {
        if (ado != null && ado.existsInAc()) {
            ado.deleteTimeseries(TREE);
            ado.delete();
            ado = null;
        }
    }

}
