package io.terrafino.api.ac.value;

import com.ac.api.data.value.FloatValue;
import com.ac.api.data.value.IntegerValue;
import com.ac.api.data.value.StringValue;
import com.ac.api.data.value.ValueStatus;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class ValueTest extends Value {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void canCreateFromAcValue() throws Exception {
        StringValue acValue = StringValue.create("abc");
        Value value = new Value(acValue);
        assertThat(value.getAcValue(), is(acValue));
    }

    @Test
    public void canCreateFromAcValueWithExplicitStatus() throws Exception {
        StringValue acValue = StringValue.create("abc");
        Value value = new Value(acValue, 144);
        assertThat(value.getAcValue().toString(), is(acValue.toString()));
        assertThat(value.getStatus(), is(144));
    }

    @Test
    public void canAmendValueWithStatus() throws Exception {
        StringValue acValue = StringValue.create("abc");
        Value value = new Value(acValue).withStatus(144);
        assertThat(value.getAcValue().toString(), is(acValue.toString()));
        assertThat(value.getStatus(), is(144));
    }

    @Test
    public void informsOfAvailableValueOk() throws Exception {
        StringValue acValue = StringValue.create("abc");
        Value value = new Value(acValue);
        assertThat(value.isAvailable(), is(true));
    }

    @Test
    public void informsOfInavailableValueOk() throws Exception {
        Value value = new Value(null);
        assertThat(value.isAvailable(), is(false));
    }

    @Test
    public void toLongConvertsIntegerOk() throws Exception {
        IntegerValue acValue = IntegerValue.create(1);
        Value value = new Value(acValue);
        assertThat(value.toLong(), is(1L));
    }

    @Test
    public void toLongThrowsExceptionIfNotOnInteger() throws Exception {
        StringValue acValue = StringValue.create("abc");
        Value value = new Value(acValue);

        thrown.expect(UnsupportedOperationException.class);
        value.toLong();
    }

    @Test
    public void toDoubleConvertsFloatOk() throws Exception {
        FloatValue acValue = FloatValue.create(1.2);
        Value value = new Value(acValue);
        assertThat(value.toDouble(), is(1.2));
    }

    @Test
    public void toDoubleConvertsIntegerOk() throws Exception {
        IntegerValue acValue = IntegerValue.create(1);
        Value value = new Value(acValue);
        assertThat(value.toDouble(), is(1.0));
    }

    @Test
    public void toDoubleThrowsExceptionIfNotOnDouble() throws Exception {
        StringValue acValue = StringValue.create("abc");
        Value value = new Value(acValue);

        thrown.expect(UnsupportedOperationException.class);
        value.toDouble();
    }

    @Test
    public void canTestEqualityWithItself() throws Exception {
        StringValue acValue1 = StringValue.create("abc");
        Value value1 = new Value(acValue1);
        assertThat(value1.equals(value1), is(true));
    }

    @Test
    public void canTestEqualityWithEqualValue() throws Exception {
        StringValue acValue1 = StringValue.create("abc");
        Value value1 = new Value(acValue1);
        StringValue acValue2 = StringValue.create("abc");
        Value value2 = new Value(acValue2);
        assertThat(value1.equals(value2), is(true));
    }

    @Test
    public void canTestInqualityWithNull() throws Exception {
        StringValue acValue1 = StringValue.create("abc");
        Value value1 = new Value(acValue1);
        assertThat(value1.equals(null), is(false));
    }

    @Test
    public void canTestInqualityWithOtherType() throws Exception {
        StringValue acValue1 = StringValue.create("abc");
        Value value1 = new Value(acValue1);
        assertThat(value1.equals("abc"), is(false));
    }


    @Test
    public void hashCodeFallsBackOnUnderlying() throws Exception {
        StringValue acValue = StringValue.create("abc");
        Value value = new Value(acValue);
        assertThat(value.hashCode(), is(acValue.hashCode()));
    }
}