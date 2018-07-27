package io.terrafino.api.ac.validation;

import com.ac.api.data.timeseries.CheckFunctionMask;

import java.util.function.BiFunction;

public class TsValidationOHLC extends TsValidation {

    public TsValidationInfo mergeWith(
            TsValidationInfo existingValidationInfo,
            BiFunction<String, String, String> amendFormula,
            BiFunction<CheckFunctionMask, CheckFunctionMask.CheckFunction, CheckFunctionMask> amendChecks) {
        return new TsValidationInfo(
                existingValidationInfo.getCalendarNumber(),
                amendChecks.apply(existingValidationInfo.getCheckFunctionMask(),
                        CheckFunctionMask.CheckFunction.OHLC),
                existingValidationInfo.getPercentage(),
                existingValidationInfo.getStdevFactor(),
                existingValidationInfo.getStdevLen(),
                existingValidationInfo.getFormula(), null);
    }

}
