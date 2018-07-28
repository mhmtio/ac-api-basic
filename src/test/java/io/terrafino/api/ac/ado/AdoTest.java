package io.terrafino.api.ac.ado;

import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.service.AcConnection;
import io.terrafino.api.ac.service.AcService;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static io.terrafino.api.ac.value.ValueFactory.*;


public class AdoTest {

    private static AcConnection conn = mock(AcConnection.class);
    private static AcService ac;

    @BeforeClass
    public static void setUpClass() throws Exception {
        ac = spy(new AcService(conn));
    }

    @Test
    public void canSetLongname() throws Exception {
        Ado ado = ac.createAdo("ADO.1");
        ado.setLongname("longname");
        assertThat(ado.getLongname(), is("longname"));
    }

    @Test
    public void canSetTemplate() throws Exception {
        Ado ado = ac.createAdo("ADO.1");
        ado.setTemplate("template");
        assertThat(ado.getTemplate(), is("template"));
    }

    @Test
    public void canCreateCopyWithTemplate() throws Exception {
        String newTemplate = "newTemplate";
        Ado ado = ac.createAdo("ADO.1");
        when(ac.createAdo(anyString(), anyString(), anyString()))
                .thenReturn(new Ado(ado.getId(), ado.getLongname(), newTemplate, conn));

        Ado adoWithNewTemplate = ado.withTemplate(newTemplate);
        assertThat(adoWithNewTemplate.getTemplate(), is(newTemplate));
    }

    @Test
    public void canGetAttributes() throws Exception {
        Ado ado = ac.createAdo("ADO.1");
        ado.set("attr1", createNA());
        ado.set("attr2", createNA());
        assertThat(ado.getAttributes(), is(new Attributes("attr1", "attr2")));
    }

    @Test
    public void canChainTheSettingOfAttributeValues() throws Exception {
        Ado ado = ac.createAdo("ADO.1");
        Ado ado2 = ado.withAttrValue("attribute", createValue("value"));
        assertThat(ado.get("attribute"), is(createValue("value")));
        assertThat(ado2, is(ado));
    }

    @Test
    public void canSetStaticAttributeAsString() throws Exception {
        Ado ado = ac.createAdo("ADO.1");
        ado.set("C0#SA001", "EUR");
        assertThat(ado.get("C0#SA001").toString(), is("EUR"));
    }

    @Test
    public void canSetStaticAttributeAsLong() throws Exception {
        Ado ado = ac.createAdo("ADO.1");
        ado.set("C0#SA002", 1L);
        assertThat(ado.get("C0#SA002").toLong(), is(1L));
    }

    @Test
    public void canSetStaticAttributeAsDouble() throws Exception {
        Ado ado = ac.createAdo("ADO.1");
        ado.set("C0#SA003", 1.5);
        assertThat(ado.get("C0#SA003").toDouble(), is(1.5));
    }

}