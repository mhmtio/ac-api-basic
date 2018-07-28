package io.terrafino.api.ac.value;

import com.ac.api.data.value.ValueList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Values {

    private List<Value> values;

    public Values(List<Value> values) {
        this.values = values;
    }

    public Values(Value... values) {
        this(Arrays.asList(values));
    }

    public Values(Double... values) {
        this(Arrays.stream(values).map(ValueFactory::createValue).collect(Collectors.toList()));
    }

    public Values(Long... values) {
        this(Arrays.stream(values).map(ValueFactory::createValue).collect(Collectors.toList()));
    }

    public Values(String... values) {
        this(Arrays.stream(values).map(ValueFactory::createValue).collect(Collectors.toList()));
    }

    public List<Value> getAll() {
        return values;
    }

    public Value get(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Values values1 = (Values) o;

        return values.equals(values1.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
