package io.terrafino.api.ac.validation;

import com.ac.api.data.timeseries.CheckFunctionMask;

import java.util.function.BiFunction;

public class TsValidationStdDev extends TsValidation {

    private Double stdev;

    public TsValidationStdDev(Double stdev) {
        this.stdev = stdev;
    }

    public Double getStdev() {
        return stdev;
    }

    public TsValidationInfo mergeWith(
            TsValidationInfo existingValidationInfo,
            BiFunction<String, String, String> amendFormula,
            BiFunction<CheckFunctionMask, CheckFunctionMask.CheckFunction, CheckFunctionMask> amendChecks) {
        return new TsValidationInfo(
                existingValidationInfo.getCalendarNumber(),
                amendChecks.apply(existingValidationInfo.getCheckFunctionMask(),
                        CheckFunctionMask.CheckFunction.STDDEV),
                stdev, // yes, in place of percentage (AC bug?)
                stdev,
                existingValidationInfo.getStdevLen(),
                existingValidationInfo.getFormula(), null);
    }

}
