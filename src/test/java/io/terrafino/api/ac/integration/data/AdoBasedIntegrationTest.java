package io.terrafino.api.ac.integration.data;

import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.ado.Ado;
import io.terrafino.api.ac.service.AcConnection;
import io.terrafino.api.ac.service.AcService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class AdoBasedIntegrationTest {

    protected static AcConnection conn;
    protected static AcService ac;

    protected Ado ado;

    public void createTestAdo() throws Exception {
        // Override in sub classes as needed
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        conn = AcConnection.getDefaultConnection();
        ac = new AcService(conn);
    }

    @Before
    public void setUp() throws Exception {
        ensureAdoIsDeleted();
        createTestAdo();
    }

    @After
    public void tearDown() throws Exception {
        ensureAdoIsDeleted();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        conn.disconnect();
    }

    protected void ensureAdoIsDeleted() throws AcException {
        if (ado != null && ado.existsInAc()) {
            ado.delete();
            ado = null;
        }
    }
}
