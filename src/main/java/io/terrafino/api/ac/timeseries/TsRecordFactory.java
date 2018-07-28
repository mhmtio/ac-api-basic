package io.terrafino.api.ac.timeseries;

import com.ac.api.data.model.TimeSeriesAttributeNumber;
import com.ac.api.data.timeseries.TimeSeriesRecord;
import com.ac.api.data.timeseries.TimeSeriesRecords;
import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.utils.TimeUtils;
import io.terrafino.api.ac.value.Value;
import io.terrafino.api.ac.value.Values;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TsRecordFactory {

    public static TsRecords fromAc(TimeSeriesRecords acRecords, Attributes attributes, TimeSeriesAttributeNumber[] attributeNumbers) {
        List<TsRecord> records = new ArrayList<>();
        acRecords.iterator().forEachRemaining(
                record -> records.add(fromAc(record, attributes, attributeNumbers))
        );
        return new TsRecords(records);
    }

    public static TsRecord fromAc(TimeSeriesRecord record, Attributes attributes, TimeSeriesAttributeNumber[] attributeNumbers) {
        int date = TimeUtils.getDate(record.getTimestamp().getValue());
        int time = TimeUtils.getTime(record.getTimestamp().getValue());
        List<Value> values = Arrays.stream(attributeNumbers).map(attrNum -> new Value(record.getValue(attrNum))).collect(Collectors.toList());
        return new TsRecord(date, time, attributes, new Values(values));
    }

}
