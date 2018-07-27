package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TsValidationPercentageTest {

    @Test
    public void mergeWithAddsInPercentageIfNotPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(
                CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(0),
                null, 1.0, 10, "", null);
        TsValidation val = new TsValidationPercentage(12.0);
        TsValidationInfo newInfo = val.mergeWith(info, TsValidation::appendFormula, TsValidation::appendCheck);
        assertThat(newInfo.getCheckFunctionMask().hasCheck(CheckFunctionMask.CheckFunction.PERCENTAGE), is(true));
        assertThat(newInfo.getPercentage(), is(12.0));
    }

    @Test
    public void mergeWithUpdatesPercentageIfPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(
                CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.PERCENTAGE),
                10.0, 1.0, 10, "", null);
        TsValidation val = new TsValidationPercentage(12.0);
        TsValidationInfo newInfo = val.mergeWith(info, TsValidation::appendFormula, TsValidation::appendCheck);
        assertThat(newInfo.getCheckFunctionMask().hasCheck(CheckFunctionMask.CheckFunction.PERCENTAGE), is(true));
        assertThat(newInfo.getPercentage(), is(12.0));
    }
}