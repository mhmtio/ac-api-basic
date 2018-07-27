package io.terrafino.cucumber.steps;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.ado.Ado;
import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.service.AcConnection;
import io.terrafino.api.ac.service.AcService;
import io.terrafino.api.ac.timeseries.TsRecord;
import io.terrafino.api.ac.timeseries.TsRecords;
import io.terrafino.api.ac.validation.TsValidation;
import io.terrafino.api.ac.validation.TsValidationNonPositive;
import io.terrafino.api.ac.value.Value;
import io.terrafino.api.ac.value.ValueFactory;
import io.terrafino.api.ac.value.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TsValidationTestSteps {

    private static final String DATE = "DATE";
    private static final String TIME = "TIME";

    private static AcConnection conn;

    private static AcService ac;
    private Ado ado;
    private String tree;

    @Before
    public void before() throws AcException {
        if (conn == null) {
            conn = AcConnection.getDefaultConnection();
            ac = new AcService(conn);
        }
    }

    @After
    public void after() throws AcException {
        if (Optional.ofNullable(ado).isPresent()) {
            ado.delete();
//            ado.deleteTimeseries(tree);
            ado = null;
        }
    }

    @Given("^an ADO with the following timeseries in (\\S+)$")
    public void anAdoWithPrefix(String tree, DataTable datatable) throws AcException {
        this.tree = tree;
        ado = ac.testAdoWithPrefix("C0.TEST")
                .withTemplate("C0_I_T001_LSA")
                .createInAc();
        ado.storeTimeseries(tree, getTimeseriesFrom(datatable));
    }

    @When("^we apply the validation function (\\S+) and revalidate$")
    public void weSetAttrToValue(String valFunc) throws AcException {
        ado.addTimeseriesValidation(tree, getValidationFunction(valFunc));
        ado.revalidate(tree);
    }

    private TsValidation getValidationFunction(String valFunc) {
        switch (valFunc) {
            case "non-positive": return new TsValidationNonPositive();
            default: throw new IllegalArgumentException(String.format("Unknown validation function: %s", valFunc));
        }
    }

    @Then("^we get the following timeseries$")
    public void weSetAttrToValue(DataTable datatable) throws AcException {
        TsRecords expectedRecords = getTimeseriesFrom(datatable);
        TsRecords acRecords = ado.loadTimeseries(tree, 0, 0, expectedRecords.getAttributes());
        assertThat(acRecords.equalsWithStatus(expectedRecords), is(true));
    }

    private TsRecords getTimeseriesFrom(DataTable datatable) {
        List<List<String>> rows = datatable.asLists(String.class);
        List<String> header = rows.get(0);
        Attributes attributes = new Attributes(
                header.stream().filter(a -> !(a.equals(DATE) || a.equals(TIME))).collect(Collectors.toList())
        );
        List<TsRecord> records = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            int date = Integer.parseInt(rows.get(i).get(header.indexOf(DATE)));
            int time = Integer.parseInt(rows.get(i).get(header.indexOf(TIME)));
            List<Value> values = new ArrayList<>();
            for (String a : attributes.getAll()) {
                values.add(ValueFactory.createValueFromString(rows.get(i).get(header.indexOf(a))));
            }
            records.add(new TsRecord(date, time, attributes, new Values(values)));
        }
        return new TsRecords(attributes, records);
    }


}
