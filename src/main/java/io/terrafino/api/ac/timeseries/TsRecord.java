package io.terrafino.api.ac.timeseries;

import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.utils.TimeUtils;
import io.terrafino.api.ac.value.Value;
import io.terrafino.api.ac.value.Values;

public class TsRecord {

    private int date;
    private int time;
    private Attributes attributes;
    private Values values;

    public TsRecord(int date, int time, Attributes attributes, Values values) {
        this.date = date;
        this.time = time;
        if (attributes.size() != values.size()) {
            throw new IllegalArgumentException("Number of attributes has to match number of values!");
        }
        this.attributes = attributes;
        this.values = values;
    }

    public int getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public long getTimestamp() {
        return TimeUtils.getTimestamp(date, time);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public Values getValues() {
        return values;
    }

    public Value get(int index) {
        return values.get(index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TsRecord record = (TsRecord) o;

        if (date != record.date) return false;
        if (time != record.time) return false;
        return attributes.equals(record.attributes) && values.equals(record.values);
    }

    @Override
    public int hashCode() {
        int result = date;
        result = 31 * result + time;
        result = 31 * result + attributes.hashCode();
        result = 31 * result + values.hashCode();
        return result;
    }

    public boolean equalsWithStatus(TsRecord other) {
        if (this.attributes.equals(other.attributes) && this.values.size() == other.values.size()) {
            for (int i = 0; i < this.values.size(); i++) {
                if (!this.values.get(i).equals(other.values.get(i)) ||
                     this.values.get(i).getStatus() != other.values.get(i).getStatus()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
