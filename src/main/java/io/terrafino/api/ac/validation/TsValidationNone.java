package io.terrafino.api.ac.validation;

import com.ac.api.data.timeseries.CheckFunctionMask;

import java.util.function.BiFunction;

public class TsValidationNone extends TsValidation {

    public TsValidationInfo mergeWith(
            TsValidationInfo existingValidationInfo,
            BiFunction<String, String, String> amendFormula,
            BiFunction<CheckFunctionMask, CheckFunctionMask.CheckFunction, CheckFunctionMask> amendChecks) {
        return new TsValidationInfo(
                existingValidationInfo.getCalendarNumber(),
                amendChecks.apply(existingValidationInfo.getCheckFunctionMask(), null),
                existingValidationInfo.getPercentage(),
                existingValidationInfo.getStdevFactor(),
                existingValidationInfo.getStdevLen(),
                amendFormula.apply(existingValidationInfo.getFormula(), null), null);
    }

}
