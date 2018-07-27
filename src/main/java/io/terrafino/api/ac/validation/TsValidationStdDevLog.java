package io.terrafino.api.ac.validation;

import com.ac.api.data.timeseries.CheckFunctionMask;

import java.util.function.BiFunction;

public class TsValidationStdDevLog extends TsValidation {

    private Double stdev;
    private Integer len;

    public TsValidationStdDevLog(Double stdev, Integer len) {
        this.stdev = stdev;
        this.len = len;
    }

    public Double getStdev() {
        return stdev;
    }

    public Integer getLen() {
        return len;
    }

    public TsValidationInfo mergeWith(
            TsValidationInfo existingValidationInfo,
            BiFunction<String, String, String> amendFormula,
            BiFunction<CheckFunctionMask, CheckFunctionMask.CheckFunction, CheckFunctionMask> amendChecks) {
        return new TsValidationInfo(
                existingValidationInfo.getCalendarNumber(),
                amendChecks.apply(existingValidationInfo.getCheckFunctionMask(),
                        CheckFunctionMask.CheckFunction.STDEV_LOG),
                existingValidationInfo.getPercentage(),
                stdev,
                len,
                existingValidationInfo.getFormula(), null);
    }

}
