package io.terrafino.cucumber.converters;

import cucumber.api.DataTable;
import io.terrafino.api.ac.value.Value;
import io.terrafino.api.ac.value.ValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesAndValuesConverter {

    public Map<String, Value> convert(DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        Map<String, Value> res = new HashMap<>();
        rows.forEach(row -> res.put(row.get(0), ValueFactory.createValue(row.get(1))));
        return res;
    }
}
