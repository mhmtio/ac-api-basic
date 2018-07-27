package io.terrafino.api.ac.timeseries;

import com.ac.api.data.model.TimeSeriesAttributeNumber;
import com.ac.api.data.timeseries.TimeSeriesRecord;
import com.ac.api.data.timeseries.TimeSeriesRecords;
import com.ac.api.data.value.FloatValue;
import com.ac.api.data.value.TimestampValue;
import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.utils.TimeUtils;
import io.terrafino.api.ac.value.ValueFactory;
import io.terrafino.api.ac.value.Values;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TsRecordFactoryTest extends TsRecordFactory {

    private Attributes attributes = new Attributes("CLOSE");
    private TimeSeriesAttributeNumber[] attributeNumbers = {TimeSeriesAttributeNumber.CLOSE};

    @Test
    public void canCreateSingleTsRecordFromAc() throws Exception {
        TsRecord record = TsRecordFactory.fromAc(createAcRecord(20180101, 235959, 100.0), attributes, attributeNumbers);
        ensureRecordMatches(record, 20180101, 235959, 100.0);
    }

    @Test
    public void canCreateTsRecordsFromAc() throws Exception {
        TimeSeriesRecords acRecords = mock(TimeSeriesRecords.class);
        List<TimeSeriesRecord> acRecordsList = Arrays.asList(
                createAcRecord(20180101, 235959, 100.0),
                createAcRecord(20180102, 235959, 200.0));
        when(acRecords.iterator()).thenReturn(acRecordsList.iterator());

        TsRecords records = TsRecordFactory.fromAc(acRecords, attributes, attributeNumbers);

        assertThat(records.size(), is(2));
        assertThat(records.getAttributes(), is(attributes));
        ensureRecordMatches(records.getRecords().get(0), 20180101, 235959, 100.0);
        ensureRecordMatches(records.getRecords().get(1), 20180102, 235959, 200.0);
    }

    private TimeSeriesRecord createAcRecord(int date, int time, double close) {
        TimestampValue timestamp = TimestampValue.create(TimeUtils.getTimestamp(date, time));
        TimeSeriesRecord acRecord = mock(TimeSeriesRecord.class);
        when(acRecord.getTimestamp()).thenReturn(timestamp);
        when(acRecord.getValue(TimeSeriesAttributeNumber.CLOSE)).thenReturn(FloatValue.create(close));
        return acRecord;
    }

    private void ensureRecordMatches(TsRecord record, int date, int time, double close) {
        assertThat(record.getDate(), is(date));
        assertThat(record.getTime(), is(time));
        assertThat(record.getAttributes(), is(attributes));
        assertThat(record.getValues(), is(new Values(ValueFactory.createValue(close))));
    }
}