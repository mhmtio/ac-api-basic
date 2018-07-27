package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TsValidationStdevTest {

    @Test
    public void canGetStdev() throws Exception {
        TsValidationStdDev val = new TsValidationStdDev(1.2);
        assertThat(val.getStdev(), is(1.2));
    }

    @Test
    public void mergeWithAddsInStdevIfNotPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(
                CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(0),
                5.0, null, 10, "", null);
        TsValidation val = new TsValidationStdDev(1.2);
        TsValidationInfo newInfo = val.mergeWith(info, TsValidation::appendFormula, TsValidation::appendCheck);
        assertThat(newInfo.getCheckFunctionMask().hasCheck(CheckFunctionMask.CheckFunction.STDDEV), is(true));
        assertThat(newInfo.getStdevFactor(), is(1.2));
    }

    @Test
    public void mergeWithvUpdatesStdevIfPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(
                CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.STDDEV),
                5.0, 1.2, 10, "", null);
        TsValidation val = new TsValidationStdDev(3.3);
        TsValidationInfo newInfo = val.mergeWith(info, TsValidation::appendFormula, TsValidation::appendCheck);
        assertThat(newInfo.getCheckFunctionMask().hasCheck(CheckFunctionMask.CheckFunction.STDDEV), is(true));
        assertThat(newInfo.getStdevFactor(), is(3.3));
    }
}