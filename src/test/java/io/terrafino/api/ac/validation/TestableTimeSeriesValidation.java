package io.terrafino.api.ac.validation;

import com.ac.api.data.model.CalendarNumber;
import com.ac.api.data.timeseries.CheckFunctionMask;
import com.ac.api.data.timeseries.TimeSeriesValidation;

class TestableTimeSeriesValidation implements TimeSeriesValidation {

    @Override
    public CalendarNumber getCalendarNumber() {
        return null;
    }

    @Override
    public CheckFunctionMask getCheckMask() {
        return null;
    }

    @Override
    public Double getPercentage() {
        return null;
    }

    @Override
    public Double getMultiplicationStdDevLogReturn() {
        return null;
    }

    @Override
    public Integer getRangeStdDevLogReturn() {
        return null;
    }

    @Override
    public String getFormula() {
        return null;
    }
}
