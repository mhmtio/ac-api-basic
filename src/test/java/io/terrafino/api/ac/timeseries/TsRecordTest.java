package io.terrafino.api.ac.timeseries;

import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.utils.TimeUtils;
import io.terrafino.api.ac.value.Value;
import io.terrafino.api.ac.value.Values;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.terrafino.api.ac.value.ValueFactory.createValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TsRecordTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void canCreateTsRecord() throws Exception {
        TsRecord record = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));

        assertThat(record.getDate(), is(20180101));
        assertThat(record.getTime(), is(235959));
        assertThat(record.getAttributes(), is(new Attributes("C0#0001")));
        assertThat(record.getValues(), is(new Values(createValue(1.0))));
    }

    @Test
    public void throwsIllegalArgumentExceptionIfArgumentsAndValuesDoNotAlign() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Number of attributes has to match number of values!");
        new TsRecord(20180101, 235959, new Attributes("C0#0001", "C0#0002"), new Values(createValue(1.0)));
    }

    @Test
    public void canGetValueByIndex() throws Exception {
        Value value = createValue(1.0);
        TsRecord record = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(value));

        assertThat(record.get(0), is(value));
    }

    @Test
    public void canGetTimestampOk() throws Exception {
        TsRecord record = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));
        assertThat(record.getTimestamp(), is(TimeUtils.getTimestamp(record.getDate(), record.getTime())));
    }

    @Test
    public void equalsReturnsTrueForIdenticalRecords() throws Exception {
        TsRecord record = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));
        assertThat(record.equals(record), is(true));
    }

    @Test
    public void equalsReturnsFalseIfOtherIsNull() throws Exception {
        TsRecord record = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));
        assertThat(record.equals(null), is(false));
    }

    @Test
    public void equalsReturnsFalseIfOtherIsDifferentType() throws Exception {
        TsRecord record = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));
        assertThat(record.equals("other"), is(false));
    }

    @Test
    public void equalsReturnsFalseIfDatesDiffer() throws Exception {
        TsRecord record1 = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));
        TsRecord record2 = new TsRecord(20180102, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));
        assertThat(record1.equals(record2), is(false));
    }

    @Test
    public void equalsReturnsFalseIfTimesDiffer() throws Exception {
        TsRecord record1 = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));
        TsRecord record2 = new TsRecord(20180101, 150000, new Attributes("C0#0001"), new Values(createValue(1.0)));
        assertThat(record1.equals(record2), is(false));
    }

    @Test
    public void equalsReturnsFalseIfAttributesDiffer() throws Exception {
        TsRecord record1 = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));
        TsRecord record2 = new TsRecord(20180101, 235959, new Attributes("C0#0002"), new Values(createValue(1.0)));
        assertThat(record1.equals(record2), is(false));
    }

    @Test
    public void equalsReturnsFalseIfValuesDiffer() throws Exception {
        TsRecord record1 = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0)));
        TsRecord record2 = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(2.0)));
        assertThat(record1.equals(record2), is(false));
    }

    @Test
    public void equalsWithStatusReturnsTrueIfStatusMatches() throws Exception {
        TsRecord record1 = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0).withStatus(130)));
        TsRecord record2 = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0).withStatus(130)));
        assertThat(record1.equalsWithStatus(record2), is(true));
    }

    @Test
    public void equalsWithStatusReturnsFalseIfStatusDoesNotMatch() throws Exception {
        TsRecord record1 = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0).withStatus(130)));
        TsRecord record2 = new TsRecord(20180101, 235959, new Attributes("C0#0001"), new Values(createValue(1.0).withStatus(5)));
        assertThat(record1.equalsWithStatus(record2), is(false));
    }

}