package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TsValidationFormulaTest {

    @Test
    public void canGetFormula() throws Exception {
        TsValidationFormula val = new TsValidationFormula("f1();");
        assertThat(val.getFormula(), is("f1();"));
    }

    @Test
    public void mergeWithAddsInFormulaIfNotPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(
                CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(0),
                5.0, 1.0, 10, "", null);
        TsValidation val = new TsValidationFormula("f1();");
        TsValidationInfo newInfo = val.mergeWith(info, TsValidation::appendFormula, TsValidation::appendCheck);
        assertThat(newInfo.getCheckFunctionMask().hasCheck(CheckFunctionMask.CheckFunction.FORMULA), is(true));
        assertThat(newInfo.getFormula(), is("f1();"));
    }

    @Test
    public void mergeWithAmendsFormulaIfPresent() throws Exception {
        TsValidationInfo info = new TsValidationInfo(
                CalendarNumber.create(-1),
                CheckFunctionMask.getInstance(0),
                5.0, 1.0, 10, "f1();", null);
        TsValidation val = new TsValidationFormula("f2();");
        TsValidationInfo newInfo = val.mergeWith(info, TsValidation::appendFormula, TsValidation::appendCheck);
        assertThat(newInfo.getCheckFunctionMask().hasCheck(CheckFunctionMask.CheckFunction.FORMULA), is(true));
        assertThat(newInfo.getFormula(), is("f1();f2();"));
    }
}