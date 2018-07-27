package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;
import com.ac.api.data.timeseries.TimeSeriesValidation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TsValidationInfo {

    private CalendarNumber calendarNumber;
    private CheckFunctionMask checkFunctionMask;
    private Double percentage;
    private Double stdevFactor;
    private Integer stdevLen;
    private String formula;
    private TimeSeriesValidation acTsValidation;

    public TsValidationInfo(CalendarNumber calendarNumber, CheckFunctionMask checkFunctionMask,
                            Double percentage, Double stdevFactor, Integer stdevLen,
                            String formula, TimeSeriesValidation acTsValidation) {
        this.calendarNumber = calendarNumber;
        this.checkFunctionMask = checkFunctionMask;
        this.percentage = percentage;
        this.stdevFactor = stdevFactor;
        this.stdevLen = stdevLen;
        this.formula = formula;
        this.acTsValidation = acTsValidation;
    }

    public List<TsValidation> getValidations() {
        List<TsValidation> validations = new ArrayList<>();
        if (checkFunctionMask.hasCheck(CheckFunctionMask.CheckFunction.OHLC)) {
            validations.add(new TsValidationOHLC());
        }
        if (checkFunctionMask.hasCheck(CheckFunctionMask.CheckFunction.BMA)) {
            validations.add(new TsValidationBMA());
        }
        if (checkFunctionMask.hasCheck(CheckFunctionMask.CheckFunction.NON_POSITIVE)) {
            validations.add(new TsValidationNonPositive());
        }
        if (checkFunctionMask.hasCheck(CheckFunctionMask.CheckFunction.PERCENTAGE)) {
            validations.add(new TsValidationPercentage(percentage));
        }
        if (checkFunctionMask.hasCheck(CheckFunctionMask.CheckFunction.STDDEV)) {
            validations.add(new TsValidationStdDev(percentage)); // yes, percentage here!
        }
        if (checkFunctionMask.hasCheck(CheckFunctionMask.CheckFunction.STDEV_LOG)) {
            validations.add(new TsValidationStdDevLog(stdevFactor, stdevLen));
        }
        if (checkFunctionMask.hasCheck(CheckFunctionMask.CheckFunction.FORMULA)) {
            validations.add(new TsValidationFormula(formula));
        }
        return validations;
    }

    public CalendarNumber getCalendarNumber() {
        return calendarNumber;
    }

    public CheckFunctionMask getCheckFunctionMask() {
        return checkFunctionMask;
    }

    public Double getPercentage() {
        return percentage;
    }

    public Double getStdevFactor() {
        return stdevFactor;
    }

    public Integer getStdevLen() {
        return stdevLen;
    }

    public String getFormula() {
        return formula;
    }

    public TimeSeriesValidation getAcValidation() {
        return this.acTsValidation;
    }
}
