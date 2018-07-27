package io.terrafino.cucumber.converters;

import cucumber.api.DataTable;
import io.terrafino.api.ac.value.Value;
import io.terrafino.api.ac.value.ValueFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AttributesAndValuesConverterTest {

    private AttributesAndValuesConverter converter = new AttributesAndValuesConverter();

    @Test
    public void convertsASingleRowTable() throws Exception {
        DataTable dataTable = DataTable.create(Collections.singletonList(Arrays.asList("attribute", "value")));
        Map<String, Value> attributesAndValues = converter.convert(dataTable);
        assertThat(attributesAndValues.keySet().size(), is(1));
        assertThat(attributesAndValues.get("attribute"), is(ValueFactory.createValue("value")));
    }
}