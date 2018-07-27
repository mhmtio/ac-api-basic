package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class TsValidationInfoTest {

    @Test
    public void canGetAcValidation() throws Exception {
        TestableTimeSeriesValidation acValidation = new TestableTimeSeriesValidation();
        TsValidationInfo info = new TsValidationInfo(CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(0),
                5.0, 1.0, 10, null, acValidation);
        assertThat(info.getAcValidation(), is(acValidation));
    }

    @Test
    public void canGetAllValidationsIfPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.BMA,
                        CheckFunctionMask.CheckFunction.OHLC,
                        CheckFunctionMask.CheckFunction.NON_POSITIVE,
                        CheckFunctionMask.CheckFunction.PERCENTAGE,
                        CheckFunctionMask.CheckFunction.STDDEV,
                        CheckFunctionMask.CheckFunction.STDEV_LOG,
                        CheckFunctionMask.CheckFunction.FORMULA),
                5.0, 1.0, 10, "", null);
        List<TsValidation> validations = info.getValidations();
        assertThat(validations.size(), is(7));
        assertThat(validations.get(0), is(instanceOf(TsValidationOHLC.class)));
        assertThat(validations.get(1), is(instanceOf(TsValidationBMA.class)));
        assertThat(validations.get(2), is(instanceOf(TsValidationNonPositive.class)));
        assertThat(validations.get(3), is(instanceOf(TsValidationPercentage.class)));
        assertThat(validations.get(4), is(instanceOf(TsValidationStdDev.class)));
        assertThat(validations.get(5), is(instanceOf(TsValidationStdDevLog.class)));
        assertThat(validations.get(6), is(instanceOf(TsValidationFormula.class)));
    }

    @Test
    public void canGetNoValidationsIfNonePresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(0),
                5.0, 1.0, 10, "", null);
        List<TsValidation> validations = info.getValidations();
        assertThat(validations.size(), is(0));
    }

    ;

}