package io.terrafino.api.ac.attribute;

import java.util.Arrays;
import java.util.List;

public class Attributes {

    private List<String> attributes;

    public Attributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public Attributes(String... attributes) {
        this.attributes = Arrays.asList(attributes);
    }

    public List<String> getAll() {
        return attributes;
    }

    public String get(int index) {
        return attributes.get(index);
    }

    public int size() {
        return attributes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attributes that = (Attributes) o;

        return attributes.equals(that.attributes);
    }

    @Override
    public int hashCode() {
        return attributes.hashCode();
    }

    @Override
    public String toString() {
        return String.join(", ", attributes);
    }
}
