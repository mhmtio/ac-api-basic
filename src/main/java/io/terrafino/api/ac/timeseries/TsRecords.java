package io.terrafino.api.ac.timeseries;

import io.terrafino.api.ac.attribute.Attributes;

import java.util.Arrays;
import java.util.List;

public class TsRecords {

    private Attributes attributes;
    private List<TsRecord> records;

    public TsRecords(Attributes attributes, TsRecord... records) {
        this(attributes, Arrays.asList(records));
    }

    public TsRecords(Attributes attributes, List<TsRecord> records) {
        this.attributes = attributes;
        if (records.stream().anyMatch(r -> !r.getAttributes().equals(attributes))) {
            throw new IllegalArgumentException("Given arguments must match those of all records!");
        }
        this.records = records;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public List<TsRecord> getRecords() {
        return records;
    }

    public TsRecord get(int index) {
        return records.get(index);
    }

    public int size() {
        return records.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TsRecords tsRecords = (TsRecords) o;

        return attributes.equals(tsRecords.attributes) && records.equals(tsRecords.records);
    }

    @Override
    public int hashCode() {
        int result = attributes.hashCode();
        result = 31 * result + records.hashCode();
        return result;
    }

    public boolean equalsWithStatus(TsRecords others) {
        if (this.attributes.equals(others.attributes) && this.records.size() == others.records.size()) {
            for (int i=0; i<this.records.size(); i++) {
                if (!this.records.get(i).equalsWithStatus(others.records.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
