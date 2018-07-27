package io.terrafino.api.ac.service;

import io.terrafino.api.ac.ado.Ado;
import io.terrafino.api.ac.service.AcConnection;
import io.terrafino.api.ac.service.AcService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;


public class AcServiceTest {

    private static AcConnection conn = mock(AcConnection.class);
    private static AcService ac;

    @BeforeClass
    public static void setUp() throws Exception {
        ac = new AcService(conn);
    }

    @Test
    public void canCreateAdoWithIdOnly() throws Exception {
        Ado ado = ac.createAdo("ADO.1");
        assertThat(ado.getId(), is("ADO.1"));
        assertThat(ado.getTemplate(), is(""));
        assertThat(ado.getLongname(), is(""));
    }

    @Test
    public void canCreateAdoWithIdTemplateAndLongname() throws Exception {
        Ado ado = ac.createAdo("ADO.1", "TEST ADO", "C0#EQUITY_LSA");
        assertThat(ado.getId(), is("ADO.1"));
        assertThat(ado.getTemplate(), is("C0#EQUITY_LSA"));
        assertThat(ado.getLongname(), is("TEST ADO"));
    }

    @Test
    public void createsTestAdoBasedOnPrefix() throws Exception {
        Ado ado = ac.testAdoWithPrefix("BB");
        assertThat(ado.getId(), startsWith("BB."));
        assertThat(ado.getId(), endsWith("TEST"));
    }

}