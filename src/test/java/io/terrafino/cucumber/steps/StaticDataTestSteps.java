package io.terrafino.cucumber.steps;

import com.google.common.base.Strings;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.ado.Ado;
import io.terrafino.api.ac.service.AcConnection;
import io.terrafino.api.ac.service.AcService;
import io.terrafino.api.ac.value.Value;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StaticDataTestSteps {

    private static AcConnection conn;
    private static AcService ac;

    private Ado ado;
    private Value value;

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
            ado = null;
        }
    }

    @Given("^an Ado with prefix (\\S+)$")
    public void anAdoWithPrefix(String prefix) throws AcException {
        ado = ac.testAdoWithPrefix(prefix)
                .withTemplate(getDefaultTemplate(prefix))
                .createInAc();
    }

    @And("^we set (\\S+) to (\\S*)$")
    public void weSetAttrToValue(String attr, String value) throws AcException {
        if (!Strings.isNullOrEmpty(value)) {
            ado.set(attr, value);
            ado.store();
        }
    }

    @When("^we read the value of (\\S+)$")
    public void weReadTheValueOf(String attr) throws AcException {
        value = ado.load(attr);
    }

    @Then("^we get (\\S+)$")
    public void weGet(String expectedValue) {
        assertThat(value.toString(), is(expectedValue));
    }

    private String getDefaultTemplate(String prefix) {
        if (prefix.startsWith("BB")) {
            return "BB_LSA";
        } else {
            return "C0_I_T001_LSA";
        }
    }

}
