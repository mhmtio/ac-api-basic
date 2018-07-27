package io.terrafino.api.ac.integration.interfaces.bloomberg;

import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.integration.data.AdoBasedIntegrationTest;
import org.junit.Test;

import static io.terrafino.api.ac.value.ValueFactory.createValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class BloombergInterfaceIntegrationTest extends AdoBasedIntegrationTest {

    private static final String TEMPL_ATTR = "BB.TEMPLATE";

    @Override
    public void createTestAdo() throws Exception {
        ado = ac.testAdoWithPrefix("BB").withTemplate("BB_LSA").createInAc();
    }

    @Test
    public void derivesTemplateForAdoTypeIAndProductAOk() throws Exception {
        whenWeSetAdoTypeAndProductTo("I", "A");
        thenTheTemplateShouldBe("BB+COM_LSA");
    }

    @Test
    public void derivesTemplateForAdoTypeIAndProductBOk() throws Exception {
        whenWeSetAdoTypeAndProductTo("I", "B");
        thenTheTemplateShouldBe("BB+COM_OP_LSA");
    }

    @Test
    public void derivesTemplateForAdoTypeIAndProductCOk() throws Exception {
        whenWeSetAdoTypeAndProductTo("I", "C");
        thenTheTemplateShouldBe("BB+GCGC_LSA");
    }

    private void whenWeSetAdoTypeAndProductTo(String adoType, String product) throws AcException {
        ado.set("BB_FLW_ADO_TYPE", createValue(adoType));
        ado.set("BB_FLW_PRODUCT", createValue(product));
        ado.store();
    }

    private void thenTheTemplateShouldBe(String template) throws AcException {
        assertThat(ado.load(TEMPL_ATTR).toString(), is(template));
    }

}
