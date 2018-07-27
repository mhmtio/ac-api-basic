package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;

import java.util.function.BiFunction;

public class TsValidationFormula extends TsValidation {

    private String formula;

    public TsValidationFormula(String formula) {
        this.formula = formula;
    }

    public String getFormula() {
        return formula;
    }

    public TsValidationInfo mergeWith(
            TsValidationInfo existingValidationInfo,
            BiFunction<String, String, String> amendFormula,
            BiFunction<CheckFunctionMask, CheckFunctionMask.CheckFunction, CheckFunctionMask> amendChecks) {
        return new TsValidationInfo(
                existingValidationInfo.getCalendarNumber(),
                amendChecks.apply(existingValidationInfo.getCheckFunctionMask(),
                        CheckFunctionMask.CheckFunction.FORMULA),
                existingValidationInfo.getPercentage(),
                existingValidationInfo.getStdevFactor(),
                existingValidationInfo.getStdevLen(),
                amendFormula.apply(existingValidationInfo.getFormula(), this.formula), null);
    }


}
