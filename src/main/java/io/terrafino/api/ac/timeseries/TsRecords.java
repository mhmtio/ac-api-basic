package io.terrafino.api.ac.timeseries;

import io.terrafino.api.ac.attribute.Attributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TsRecords {

    private Attributes attributes;
    private List<TsRecord> records;

    public TsRecords(TsRecord... records) {
        this(Arrays.asList(records));
    }

    public TsRecords(List<TsRecord> records) {
        if (Optional.ofNullable(records).isPresent() && records.size() > 0) {
            this.attributes = records.get(0).getAttributes();
            if (records.stream().anyMatch(r -> !r.getAttributes().equals(attributes))) {
                throw new IllegalArgumentException("Attributes have to be the same on all records!");
            }
        } else {
            this.attributes = new Attributes();
            this.records = Collections.emptyList();
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
