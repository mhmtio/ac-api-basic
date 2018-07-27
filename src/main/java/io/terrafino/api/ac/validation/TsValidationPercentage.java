package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;

import java.util.function.BiFunction;

public class TsValidationPercentage extends TsValidation {

    private Double percentage;

    public TsValidationPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public TsValidationInfo mergeWith(
            TsValidationInfo existingValidationInfo,
            BiFunction<String, String, String> amendFormula,
            BiFunction<CheckFunctionMask, CheckFunctionMask.CheckFunction, CheckFunctionMask> amendChecks) {
        return new TsValidationInfo(
                existingValidationInfo.getCalendarNumber(),
                amendChecks.apply(existingValidationInfo.getCheckFunctionMask(),
                        CheckFunctionMask.CheckFunction.PERCENTAGE),
                percentage,
                existingValidationInfo.getStdevFactor(),
                existingValidationInfo.getStdevLen(),
                existingValidationInfo.getFormula(), null);
    }

}
