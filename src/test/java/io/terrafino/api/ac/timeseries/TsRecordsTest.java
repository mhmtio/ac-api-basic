package io.terrafino.api.ac.timeseries;

import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.value.ValueFactory;
import io.terrafino.api.ac.value.Values;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static io.terrafino.api.ac.value.ValueFactory.*;


public class TsRecordsTest {

    private Attributes attributes = new Attributes("ATTR1", "ATTR2");
    private TsRecord record1 = new TsRecord(20180101, 235959, attributes, new Values(createValue(1.0), createValue(2.0)));
    private TsRecord record2 = new TsRecord(20180102, 235959, attributes, new Values(createValue(1.5), createValue(2.5)));

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void canCreateTsRecordsFromListOk() throws Exception {
        TsRecords records = new TsRecords(Arrays.asList(record1, record2));

        assertThat(records.getAttributes(), is(attributes));
        assertThat(records.getRecords(), is(Arrays.asList(record1, record2)));
    }

    @Test
    public void canCreateTsRecordsFromVarArgsOk() throws Exception {
        TsRecords records = new TsRecords(record1, record2);

        assertThat(records.getAttributes(), is(attributes));
        assertThat(records.getRecords(), is(Arrays.asList(record1, record2)));
    }

    @Test
    public void canGetRecordsByIndex() throws Exception {
        TsRecords records = new TsRecords(Arrays.asList(record1, record2));

        assertThat(records.get(0), is(record1));
    }

    @Test
    public void throwsExceptionWhenAttributesDoNotMatchAcrossAllRecords() throws Exception {
        Attributes twoAttributes = new Attributes("ATTR1", "ATTR2");
        TsRecord twoAttributeRecord = new TsRecord(20180101, 235959, twoAttributes, new Values(createValue(1.0), createValue(2.0)));
        TsRecord oneAttributeRecord = new TsRecord(20180102, 235959, new Attributes("ATTR1"), new Values(createValue(1.5)));

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Attributes have to be the same on all records!");
        new TsRecords(Arrays.asList(twoAttributeRecord, oneAttributeRecord));
    }

    @Test
    public void equalsReturnsTrueForIdenticalRecords() throws Exception {
        TsRecords records = new TsRecords(Arrays.asList(record1, record2));

        assertThat(records.equals(records), is(true));
    }

    @Test
    public void equalsReturnsTrueForEqualRecords() throws Exception {
        TsRecords recordsA = new TsRecords(Arrays.asList(record1, record2));
        TsRecord record1b = new TsRecord(20180101, 235959, attributes, new Values(createValue(1.0), createValue(2.0)));
        TsRecord record2b = new TsRecord(20180102, 235959, attributes, new Values(createValue(1.5), createValue(2.5)));
        TsRecords recordsB = new TsRecords(Arrays.asList(record1b, record2b));

        assertThat(recordsA.equals(recordsB), is(true));
    }

    @Test
    public void equalsWithStatusReturnsTrueForEqualRecordsWithSameStatus() throws Exception {
        TsRecords recordsA = new TsRecords(Arrays.asList(record1, record2));
        TsRecord record1b = new TsRecord(20180101, 235959, attributes,
                new Values(createValue(1.0).withStatus(1), createValue(2.0).withStatus(1)));
        TsRecord record2b = new TsRecord(20180102, 235959, attributes,
                new Values(createValue(1.5).withStatus(1), createValue(2.5).withStatus(1)));
        TsRecords recordsB = new TsRecords(Arrays.asList(record1b, record2b));

        assertThat(recordsA.equalsWithStatus(recordsB), is(true));
    }

    @Test
    public void equalsWithStatusReturnsFalseForEqualRecordsWithDiffStatus() throws Exception {
        TsRecords recordsA = new TsRecords(Arrays.asList(record1, record2));
        TsRecord record1b = new TsRecord(20180101, 235959, attributes,
                new Values(createValue(1.0).withStatus(1), createValue(2.0).withStatus(1)));
        TsRecord record2b = new TsRecord(20180102, 235959, attributes,
                new Values(createValue(1.5).withStatus(999), createValue(2.5).withStatus(1)));
        TsRecords recordsB = new TsRecords(Arrays.asList(record1b, record2b));

        assertThat(recordsA.equalsWithStatus(recordsB), is(false));
    }

    @Test
    public void equalsReturnsFalseForRecordsWithSameAttributesButDifferentValues() throws Exception {
        TsRecords recordsA = new TsRecords(Arrays.asList(record1, record2));
        TsRecord record1b = new TsRecord(20180101, 235959, attributes, new Values(createValue(99.0), createValue(2.0)));
        TsRecord record2b = new TsRecord(20180102, 235959, attributes, new Values(createValue(1.5), createValue(2.5)));
        TsRecords recordsB = new TsRecords(Arrays.asList(record1b, record2b));

        assertThat(recordsA.equals(recordsB), is(false));
    }

    @Test
    public void equalsReturnsFalseForRecordsWithDifferentAttributes() throws Exception {
        TsRecords recordsA = new TsRecords(Arrays.asList(record1, record2));
        Attributes attributesB = new Attributes("ATTR1b", "ATTR2b");
        TsRecord record1b = new TsRecord(20180101, 235959, attributesB, new Values(createValue(1.0), createValue(2.0)));
        TsRecord record2b = new TsRecord(20180102, 235959, attributesB, new Values(createValue(1.5), createValue(2.5)));
        TsRecords recordsB = new TsRecords(Arrays.asList(record1b, record2b));

        assertThat(recordsA.equals(recordsB), is(false));
    }

    @Test
    public void equalsReturnsFalseIfOtherIsNull() throws Exception {
        TsRecords records = new TsRecords(Arrays.asList(record1, record2));
        assertThat(records.equals(null), is(false));
    }

    @Test
    public void equalsReturnsFalseIfOtherIsDifferentType() throws Exception {
        TsRecords records = new TsRecords(Arrays.asList(record1, record2));
        assertThat(records.equals("other"), is(false));
    }

    @Test
    public void hashCodeCombinesAttributesAndRecordsHashCode() throws Exception {
        TsRecords records = new TsRecords(Arrays.asList(record1, record2));

        int expectedHashCode = 31 * attributes.hashCode() + records.getRecords().hashCode();
        assertThat(records.hashCode(), is(expectedHashCode));
    }
}