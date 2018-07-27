package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TsValidationStdevLogTest {


    @Test
    public void canGetStdev() throws Exception {
        TsValidationStdDev val = new TsValidationStdDev(1.2);
        assertThat(val.getStdev(), is(1.2));
    }

    @Test
    public void canGetStdevLen() throws Exception {
        TsValidationStdDevLog val = new TsValidationStdDevLog(1.2, 10);
        assertThat(val.getStdev(), is(1.2));
        assertThat(val.getLen(), is(10));
    }

    @Test
    public void mergeWithAddsInStdevLogIfNotPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(
                CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(0),
                5.0, null, 10, "", null);
        TsValidation val = new TsValidationStdDevLog(1.2, 10);
        TsValidationInfo newInfo = val.mergeWith(info, TsValidation::appendFormula, TsValidation::appendCheck);
        assertThat(newInfo.getCheckFunctionMask().hasCheck(CheckFunctionMask.CheckFunction.STDEV_LOG), is(true));
        assertThat(newInfo.getStdevFactor(), is(1.2));
        assertThat(newInfo.getStdevLen(), is(10));
    }

    @Test
    public void mergeWithUpdatesStdevLogIfPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(
                CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(0),
                5.0, 1.2, 10, "", null);
        TsValidation val = new TsValidationStdDevLog(3.3, 20);
        TsValidationInfo newInfo = val.mergeWith(info, TsValidation::appendFormula, TsValidation::appendCheck);
        assertThat(newInfo.getCheckFunctionMask().hasCheck(CheckFunctionMask.CheckFunction.STDEV_LOG), is(true));
        assertThat(newInfo.getStdevFactor(), is(3.3));
        assertThat(newInfo.getStdevLen(), is(20));
    }
}