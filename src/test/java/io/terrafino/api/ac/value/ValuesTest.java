package io.terrafino.api.ac.value;

import org.junit.Test;

import java.util.Arrays;

import static io.terrafino.api.ac.value.ValueFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ValuesTest {

    @Test
    public void canCreateValues() throws Exception {
        Values values = new Values(createValue("VALUE1"), createValue("VALUE2"));
        assertThat(values.getAll(), is(Arrays.asList(createValue("VALUE1"), createValue("VALUE2"))));
    }

    @Test
    public void canCreateDoubleValues() throws Exception {
        Values values = new Values(100L, 200L);
        assertThat(values.getAll(), is(Arrays.asList(createValue(100L), createValue(200L))));
    }

    @Test
    public void canCreateLongValues() throws Exception {
        Values values = new Values("ABC", "DEF");
        assertThat(values.getAll(), is(Arrays.asList(createValue("ABC"), createValue("DEF"))));
    }

    @Test
    public void canCreateStringValues() throws Exception {
        Values values = new Values(100.0, 200.0);
        assertThat(values.getAll(), is(Arrays.asList(createValue(100.0), createValue(200.0))));
    }

    @Test
    public void canGetSizeOfValues() throws Exception {
        Values values = new Values(createValue("VALUE1"), createValue("VALUE2"));
        assertThat(values.size(), is(2));
    }

    @Test
    public void canGetSingleValueWithValidIndex() throws Exception {
        Values values = new Values(createValue("VALUE1"), createValue("VALUE2"));
        assertThat(values.get(0), is(createValue("VALUE1")));
    }

    @Test
    public void canTestEquality() throws Exception {
        Values values1 = new Values(createValue("VALUE1"), createValue("VALUE2"));
        Values values2 = new Values(createValue("VALUE1"), createValue("VALUE2"));
        assertThat(values1.equals(values2), is(true));
    }

    @Test
    public void canTestEqualityWithItself() throws Exception {
        Values values = new Values(createValue("VALUE1"), createValue("VALUE2"));
        assertThat(values.equals(values), is(true));
    }

    @Test
    public void canTestInEqualityWithNull() throws Exception {
        Values values = new Values(createValue("VALUE1"), createValue("VALUE2"));
        assertThat(values.equals(null), is(false));
    }

    @Test
    public void canTestInEqualityWithOtherType() throws Exception {
        Values values = new Values(createValue("VALUE1"), createValue("VALUE2"));
        assertThat(values.equals("VALUE1"), is(false));
    }

    @Test
    public void canTestInEquality() throws Exception {
        Values values1 = new Values(createValue("VALUE1"), createValue("VALUE2"));
        Values values2 = new Values(createValue("VALUE1b"), createValue("VALUE2b"));
        assertThat(values1.equals(values2), is(false));
    }

    @Test
    public void hashCodeFallsBackOnUnderlying() throws Exception {
        Values values = new Values(createValue("VALUE1"), createValue("VALUE2"));
        assertThat(values.hashCode(), is(values.getAll().hashCode()));
    }

}