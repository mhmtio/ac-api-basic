package io.terrafino.api.ac.service;

import com.ac.api.data.model.TimeSeriesAttribute;
import com.ac.api.data.model.TimeSeriesAttributeId;
import com.ac.api.data.model.TimeSeriesAttributeNumber;
import com.ac.api.error.ServerException;
import com.ac.api.query.ModelResult;
import com.ac.api.query.TimeSeriesAttributesQuery;
import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.attribute.Attributes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AcMetaService {

    private AcConnection conn;

    private static ConcurrentHashMap<String, TimeSeriesAttributeNumber> timeseriesAttributes = new ConcurrentHashMap<>();

    public AcMetaService(AcConnection conn) {
        this.conn = conn;
    }

    public TimeSeriesAttributeNumber getTimeseriesAttributeNumber(String attribute) throws AcException {
        if (!timeseriesAttributes.contains(attribute)) {
            addToTimeseriesCache(attribute);
        }
        return timeseriesAttributes.get(attribute);
    }

    public List<TimeSeriesAttributeNumber> getTimeseriesAttributeNumbers(Attributes attributes)
            throws AcException {
        List<TimeSeriesAttributeNumber> attributeNumbers = new ArrayList<>();
        for (String a : attributes.getAll()) {
            attributeNumbers.add(getTimeseriesAttributeNumber(a));
        }
        return attributeNumbers;
    }

    public List<TimeSeriesAttributeNumber> getAttributeNumbersFor(Attributes attributes) throws AcException {
        Attributes dateAndTime = new Attributes("DATE", "TIME");
        List<String> otherAttrs = attributes.getAll().stream().filter(a -> !a.equals("DATE") && !a.equals("TIME")).collect(Collectors.toList());
        List<String> allAttrs = new ArrayList<>();
        allAttrs.addAll(dateAndTime.getAll());
        allAttrs.addAll(otherAttrs);
        List<TimeSeriesAttributeNumber> attrNums = new ArrayList<>();
        for (String attr : allAttrs) {
            attrNums.add(getTimeseriesAttributeNumber(attr));
        }
        return attrNums;
    }




    private void addToTimeseriesCache(String attribute) throws AcException {
        List<TimeSeriesAttributeId> tsAttributeIds = Collections.singletonList(TimeSeriesAttributeId.create(attribute));
        TimeSeriesAttributesQuery query = conn.queryFactory.createTimeSeriesAttributesQuery(tsAttributeIds, true, false, false);
        try {
            ModelResult<TimeSeriesAttribute> result = conn.serverInterface.timeSeriesAttributesQuery(query);
            if (Optional.ofNullable(result).isPresent() && result.size() == 1) {
                TimeSeriesAttribute timeSeriesAttribute = result.asList().get(0);
                timeseriesAttributes.put(attribute, timeSeriesAttribute.getNumber());
            } else {
                throw new AcException(String.format("Could not find attribute number of %s!", attribute));
            }
        } catch (ServerException e) {
            throw new AcException(String.format("Could not query attribute number of %s!", attribute), e);
        }
    }
}
