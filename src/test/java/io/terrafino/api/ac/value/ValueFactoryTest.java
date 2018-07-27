package io.terrafino.api.ac.value;


import com.ac.api.data.value.StringValue;
import com.ac.api.data.value.ValueList;
import org.junit.Test;

import java.util.Iterator;

import static io.terrafino.api.ac.value.ValueFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ValueFactoryTest extends ValueFactory {

    @Test
    public void canCreateNAValue() throws Exception {
        Value value = ValueFactory.createNA();
        assertThat(value.isAvailable(), is(false));
    }

    @Test
    public void canCreateLongValueThatIsAvailable() throws Exception {
        Value value = ValueFactory.createValue(1);
        assertThat(value.isAvailable(), is(true));
    }

    @Test
    public void canCreateLongValueOk() throws Exception {
        Value value = ValueFactory.createValue(1);
        assertThat(value.toLong(), is(1L));
    }

    @Test
    public void canCreateLongValueWithStatus() throws Exception {
        Value value = ValueFactory.createValue(1).withStatus(144);
        assertThat(value.getStatus(), is(144));
    }

    @Test
    public void canCreateStringValueThatIsAvailable() throws Exception {
        Value value = ValueFactory.createValue("abc");
        assertThat(value.isAvailable(), is(true));
    }

    @Test
    public void canCreateStringValueOk() throws Exception {
        Value value = ValueFactory.createValue("abc");
        assertThat(value.toString(), is("abc"));
    }

    @Test
    public void canCreateStringValueWithStatus() throws Exception {
        Value value = ValueFactory.createValue("abc").withStatus(144);
        assertThat(value.getStatus(), is(144));
    }

    @Test
    public void canCreateDoubleValueThatIsAvailable() throws Exception {
        Value value = ValueFactory.createValue(1.2);
        assertThat(value.isAvailable(), is(true));
    }

    @Test
    public void canCreateDoubleValueOk() throws Exception {
        Value value = ValueFactory.createValue(1.2);
        assertThat(value.toDouble(), is(1.2));
    }

    @Test
    public void canCreateDoubleValueWithStatus() throws Exception {
        Value value = ValueFactory.createValue(1.2).withStatus(144);
        assertThat(value.getStatus(), is(144));
    }

    @Test
    public void canConvertValuesToAcValueList() throws Exception {
        Values values = new Values(createValue("A"), createValue("B"));
        ValueList<com.ac.api.data.value.Value> acValueList = ValueFactory.toAcValueList(values);
        assertThat(acValueList.size(), is(2));
        assertThat(acValueList.getValue(0), is(StringValue.create("A")));
        assertThat(acValueList.getValue(1), is(StringValue.create("B")));
    }

    @Test
    public void canConvertValuesToAcValueListWithCorrectIterator() throws Exception {
        Values values = new Values(createValue("A"), createValue("B"));
        ValueList<com.ac.api.data.value.Value> acValueList = ValueFactory.toAcValueList(values);
        Iterator<com.ac.api.data.value.Value> iterator = acValueList.iterator();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(true));
        iterator.next();
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void canCreateNAValueFromString() throws Exception {
        Value value = createValueFromString("");
        assertThat(value.isAvailable(), is(false));
        assertThat(value.getStatus(), is(0));
    }

    @Test
    public void canCreateDoubleValueFromString() throws Exception {
        Value value = createValueFromString("1.0");
        assertThat(value, is(createValue(1.0).withStatus(1)));
    }

    @Test
    public void canCreateDoubleValueWithStatusFromString() throws Exception {
        Value value = createValueFromString("1.0 [130]");
        assertThat(value, is(createValue(1.0).withStatus(130)));
    }

    @Test
    public void canCreateDoubleValueFromIntegerString() throws Exception {
        Value value = createValueFromString("1");
        assertThat(value, is(createValue(1.0).withStatus(1)));
    }

    @Test
    public void canCreateDoubleValueWithStatusFromIntegerString() throws Exception {
        Value value = createValueFromString("1 [130]");
        assertThat(value, is(createValue(1.0).withStatus(130)));
    }

    @Test
    public void canCreateStringValueFromString() throws Exception {
        Value value = createValueFromString("abc");
        assertThat(value, is(createValue("abc").withStatus(1)));
    }

    @Test
    public void canCreateStringValueWithStatusFromString() throws Exception {
        Value value = createValueFromString("abc [5]");
        assertThat(value, is(createValue("abc").withStatus(5)));
    }
}