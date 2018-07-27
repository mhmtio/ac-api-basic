package io.terrafino.api.ac.value;

import com.ac.api.data.value.FloatValue;
import com.ac.api.data.value.IntegerValue;
import com.ac.api.data.value.ValueStatus;
import com.ac.api.data.value.ValueType;

import java.util.Optional;

public class Value {

    private com.ac.api.data.value.Value acValue;

    Value() {
    }

    public Value(final com.ac.api.data.value.Value acValue) {
        this(acValue, (Optional.ofNullable(acValue).isPresent()) ? acValue.getStatus().getValue() : 0);
    }

    public Value(final com.ac.api.data.value.Value acValue, int status) {
        if (Optional.ofNullable(acValue).isPresent()) {
            this.acValue = acValue.withStatus(ValueStatus.getInstance(status));
        } else {
            this.acValue = com.ac.api.data.value.Value.createNaValueOfType(ValueStatus.getInstance(status), ValueType.STRING);
        }
    }

    public Value withStatus(int status) {
        return new Value(this.acValue, status);
    }

    public boolean isAvailable() {
        return Optional.ofNullable(this.acValue).isPresent() && this.acValue.isAvailable();
    }

    public long toLong() {
        switch (this.acValue.getType()) {
            case INTEGER:
                return ((IntegerValue) acValue).getLongValue();
            default:
                throw new UnsupportedOperationException();
        }
    }

    public double toDouble() {
        switch (this.acValue.getType()) {
            case FLOAT:
                return ((FloatValue) acValue).getValue();
            case INTEGER:
                return ((IntegerValue) acValue).getLongValue();
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        return acValue.toString();
    }

    public int getStatus() {
        return this.acValue.getStatus().getValue();
    }

    public com.ac.api.data.value.Value getAcValue() {
        return this.acValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value value = (Value) o;

        return acValue.equals(value.acValue);
    }

    @Override
    public int hashCode() {
        return acValue.hashCode();
    }
}
