package io.terrafino.api.ac.timeseries;

import com.ac.api.data.model.Security;
import com.ac.api.data.model.TimeSeriesAttributeNumber;
import io.terrafino.api.ac.validation.TsValidationInfo;

import java.util.List;

public class TsHeader {

    private Security security;
    private boolean isIntraDay;
    private boolean isIntraSecond;
    private List<TimeSeriesAttributeNumber> attributeNumbers;
    private TsValidationInfo validationInfo;

    public TsHeader(Security security, Boolean isIntraDay, Boolean isIntraSecond, List<TimeSeriesAttributeNumber> attributeNumbers, TsValidationInfo validationInfo) {
        this.security = security;
        this.isIntraDay = isIntraDay;
        this.isIntraSecond = isIntraSecond;
        this.attributeNumbers = attributeNumbers;
        this.validationInfo = validationInfo;
    }

    public Security getSecurity() {
        return this.security;
    }

    public boolean isIntraDay() {
        return isIntraDay;
    }

    public boolean isIntraSecond() {
        return isIntraSecond;
    }

    public TsValidationInfo getValidationInfo() {
        return this.validationInfo;
    }

    public List<TimeSeriesAttributeNumber> getAttributeNumbers() {
        return attributeNumbers;
    }
}
