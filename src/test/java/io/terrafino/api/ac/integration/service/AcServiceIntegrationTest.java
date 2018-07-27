package io.terrafino.api.ac.integration.service;

import io.terrafino.api.ac.ado.Ado;
import io.terrafino.api.ac.service.AcConnection;
import io.terrafino.api.ac.service.AcService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AcServiceIntegrationTest {

    private static AcConnection conn;
    private static AcService ac;

    @BeforeClass
    public static void setUp() throws Exception {
        conn = AcConnection.getDefaultConnection();
        ac = new AcService(conn);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        conn.disconnect();
    }

    @Test
    public void executesFormulaEngineQueryWithSimpleResult() throws Exception {
        List<List<String>> res = ac.executeFormulaEngineQuery("return 1;");
        assertThat(res.get(0).get(0), is("1"));

    }

    @Test
    public void executesFormulaEngineQueryWithListResult() throws Exception {
        List<List<String>> res = ac.executeFormulaEngineQuery("return [1, 2];");
        assertThat(res.get(0).get(0), is("1"));
        assertThat(res.get(1).get(0), is("2"));
    }

    @Test
    public void executesFormulaEngineQueryWithMatrixResult() throws Exception {
        List<List<String>> res = ac.executeFormulaEngineQuery("return [[1, 2], [3, 4]];");
        assertThat(res.get(0).get(0), is("1"));
        assertThat(res.get(0).get(1), is("2"));
        assertThat(res.get(1).get(0), is("3"));
        assertThat(res.get(1).get(1), is("4"));
    }
}