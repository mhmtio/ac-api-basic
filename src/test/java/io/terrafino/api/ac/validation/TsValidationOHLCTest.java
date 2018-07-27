package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TsValidationOHLCTest {

    @Test
    public void mergeWithAddsInOHLCIfNotPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(
                CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(0),
                5.0, 1.0, 10, "", null);
        TsValidationOHLC val = new TsValidationOHLC();
        TsValidationInfo newInfo = val.mergeWith(info, TsValidation::appendFormula, TsValidation::appendCheck);
        assertThat(newInfo.getCheckFunctionMask().hasCheck(CheckFunctionMask.CheckFunction.OHLC), is(true));
    }
}