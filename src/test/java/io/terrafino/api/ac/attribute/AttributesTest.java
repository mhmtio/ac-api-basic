package io.terrafino.api.ac.attribute;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AttributesTest {

    @Test
    public void canCreateAttributes() throws Exception {
        Attributes attributes = new Attributes("ATTR1", "ATTR2");
        assertThat(attributes.getAll(), is(Arrays.asList("ATTR1", "ATTR2")));
    }

    @Test
    public void canGetSizeOfAttributes() throws Exception {
        Attributes attributes = new Attributes("ATTR1", "ATTR2");
        assertThat(attributes.size(), is(2));
    }

    @Test
    public void canGetSingleAttributeWithValidIndex() throws Exception {
        Attributes attributes = new Attributes("ATTR1", "ATTR2");
        assertThat(attributes.get(0), is("ATTR1"));
    }

    @Test
    public void canTestEquality() throws Exception {
        Attributes attributes1 = new Attributes("ATTR1", "ATTR2");
        Attributes attributes2 = new Attributes("ATTR1", "ATTR2");
        assertThat(attributes1.equals(attributes2), is(true));
    }

    @Test
    public void canTestInEqualityAgainstNull() throws Exception {
        Attributes attributes1 = new Attributes("ATTR1", "ATTR2");
        assertThat(attributes1.equals(null), is(false));
    }

    @Test
    public void canTestInEqualityAgainstOtherType() throws Exception {
        Attributes attributes1 = new Attributes("ATTR1", "ATTR2");
        assertThat(attributes1.equals("ATTR3"), is(false));
    }

    @Test
    public void canTestInEquality() throws Exception {
        Attributes attributes1 = new Attributes("ATTR1", "ATTR2");
        Attributes attributes2 = new Attributes("ATTR1b", "ATTR2b");
        assertThat(attributes1.equals(attributes2), is(false));
    }

    @Test
    public void hashCodeFallsBackOnUnderlying() throws Exception {
        Attributes attributes = new Attributes("ATTR1", "ATTR2");
        assertThat(attributes.hashCode(), is(attributes.getAll().hashCode()));
    }

    @Test
    public void toStringIsCorrectForSingleAttribute() throws Exception {
        Attributes attributes = new Attributes("ATTR1");
        assertThat(attributes.toString(), is("ATTR1"));
    }

    @Test
    public void toStringIsCorrectForTwoAttributes() throws Exception {
        Attributes attributes = new Attributes("ATTR1", "ATTR2");
        assertThat(attributes.toString(), is("ATTR1, ATTR2"));
    }
}