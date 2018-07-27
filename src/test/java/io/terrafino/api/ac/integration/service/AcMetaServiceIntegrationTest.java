package io.terrafino.api.ac.integration.service;

import com.ac.api.data.model.TimeSeriesAttributeNumber;
import io.terrafino.api.ac.service.AcConnection;
import io.terrafino.api.ac.service.AcMetaService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AcMetaServiceIntegrationTest {

    private static AcConnection conn;

    @BeforeClass
    public static void setUp() throws Exception {
        conn = AcConnection.getDefaultConnection();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        conn.disconnect();
    }

    @Test
    public void canDetermineAttributeNumberOfDateAsZero() throws Exception {
        assertThat(conn.getMetaService().getTimeseriesAttributeNumber("DATE"), is(TimeSeriesAttributeNumber.create(0)));
    }

    @Test
    public void canDetermineAttributeNumberOfTimeAsOne() throws Exception {
        assertThat(conn.getMetaService().getTimeseriesAttributeNumber("TIME"), is(TimeSeriesAttributeNumber.create(1)));
    }
}