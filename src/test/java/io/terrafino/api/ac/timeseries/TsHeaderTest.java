package io.terrafino.api.ac.timeseries;

import com.ac.api.data.model.*;
import com.ac.api.data.timeseries.CheckFunctionMask;
import io.terrafino.api.ac.validation.TsValidationInfo;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TsHeaderTest {

    private static Security security;
    private static TsHeader tsHeader;
    private  static List<TimeSeriesAttributeNumber> tsAttrs;
    private  static TsValidationInfo valInfo;

    @BeforeClass
    public static void setUpClass() throws Exception {
         security = Security.create(UserNumber.create(100L),
                GroupNumber.create(100L),
                Permissions.create(null, null));
         tsAttrs = Arrays.asList(TimeSeriesAttributeNumber.DATE, TimeSeriesAttributeNumber.TIME);
         valInfo = new TsValidationInfo(CalendarNumber.create(1L), CheckFunctionMask.getInstance(0),
                 5.0, 1.0, 10, "f1", null);
        tsHeader = new TsHeader(security, false, true, tsAttrs, valInfo);
    }

    @Test
    public void canGetSecurity() throws Exception {
        assertThat(tsHeader.getSecurity(), is(security));
    }

    @Test
    public void canGetIsIntraDay() throws Exception {
        assertThat(tsHeader.isIntraDay(), is(false));
    }

    @Test
    public void canGetIsIntraSecond() throws Exception {
        assertThat(tsHeader.isIntraSecond(), is(true));
    }

    @Test
    public void canGetAttributeNumbers() throws Exception {
        assertThat(tsHeader.getAttributeNumbers(), is(tsAttrs));
    }

    @Test
    public void canGetValidationInfo() throws Exception {
        assertThat(tsHeader.getValidationInfo(), is(valInfo));
    }
}