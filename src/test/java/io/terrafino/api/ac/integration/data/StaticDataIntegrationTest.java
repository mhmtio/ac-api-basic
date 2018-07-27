package io.terrafino.api.ac.integration.data;

import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.ado.Ado;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class StaticDataIntegrationTest extends AdoBasedIntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void canCheckIfAdoExists() throws Exception {
        Ado doNotDelete = ac.createAdo("$P0_DEFAULT");
        assertThat(doNotDelete.existsInAc(), is(true));
    }

    @Test
    public void canCreateAndDeleteAdoInAC() throws Exception {
        ado = ac.testAdoWithPrefix("ADO").withTemplate("C0_I_T001_LSA").createInAc();
        assertThat(ado.existsInAc(), is(true));
        ado.delete();
        assertThat(ado.existsInAc(), is(false));
    }

    @Test
    public void deleteWillThrowExceptionIfAdoDoesNotExist() throws Exception {
        ado = ac.testAdoWithPrefix("NoSuchAdo").withTemplate("C0_I_T001_LSA");
        assertThat(ado.existsInAc(), is(false));
        thrown.expect(AcException.class);
        thrown.expectMessage(String.format("Could not delete Ado %s!: (18) Could not find Ado '%s'", ado.getId(), ado.getId()));
        ado.delete();
    }

    @Test
    public void canSetStaticAttributesInAc() throws Exception {
        ado = ac.testAdoWithPrefix("ADO").withTemplate("C0_I_T001_LSA").createInAc();
        String attr = "C0_I0449";
        String value = "nativeName";
        ado.set(attr, value);
        ado.store();
        assertThat(ado.load(attr).toString(), is(value));
    }

    @Test
    public void canSetAndStoreStaticAttributesInAc() throws Exception {
        ado = ac.testAdoWithPrefix("ADO").withTemplate("C0_I_T001_LSA").createInAc();
        String attr = "C0_I0449";
        String value = "nativeName";
        ado.setAndStore(attr, value);
        assertThat(ado.load(attr).toString(), is(value));
    }

}
