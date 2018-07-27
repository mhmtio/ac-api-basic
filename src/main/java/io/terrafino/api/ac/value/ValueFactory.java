package io.terrafino.api.ac.value;

import com.ac.api.data.value.*;
import com.google.common.base.Strings;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValueFactory {

    public static Value fromAcValue(com.ac.api.data.value.Value acValue) {
        return new Value(acValue);
    }

    public static Value createNA() {
        return fromAcValue(com.ac.api.data.value.Value.createNaValueOfType(ValueStatus.NOT_AVAILABLE, ValueType.STRING));
    }

    public static Value createValue(long value) {
        return fromAcValue(com.ac.api.data.value.IntegerValue.create(ValueStatus.getInstance(1), value));
    }

    public static Value createValue(String value) {
        return fromAcValue(com.ac.api.data.value.StringValue.create(ValueStatus.getInstance(1), value));
    }

    public static Value createValue(double value) {
        return fromAcValue(com.ac.api.data.value.FloatValue.create(ValueStatus.getInstance(1), value));
    }

    public static ValueList<com.ac.api.data.value.Value> toAcValueList(Values values) {
        return new ValueList<com.ac.api.data.value.Value>() {
            @Override
            public int size() {
                return values.size();
            }

            @Override
            public com.ac.api.data.value.Value getValue(int index) {
                return values.get(index).getAcValue();
            }

            @Override
            public Iterator<com.ac.api.data.value.Value> iterator() {
                return values.getAll().stream().map(v -> v.getAcValue()).iterator();
            }
        };
    }

    public static Value createValueFromString(String input) {
        Pattern valueAndStatus = Pattern.compile("^([^ ]+) *\\[([0-9]+)]$");
        Matcher valueAndStatusMatcher = valueAndStatus.matcher(input);
        if (Strings.isNullOrEmpty(input)) {
            return createNA();
        } else if (valueAndStatusMatcher.matches()) {
            String value = valueAndStatusMatcher.group(1);
            int status = Integer.parseInt(valueAndStatusMatcher.group(2));
            return createValueFromString(value).withStatus(status);
        } else if (input.trim().matches("^[-+]?[0-9]+\\.?[0-9]*$")) {
            return createValue(Double.parseDouble(input));
        } else {
            return createValue(input);
        }
    }

}
