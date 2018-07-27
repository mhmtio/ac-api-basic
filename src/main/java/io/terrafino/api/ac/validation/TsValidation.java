package io.terrafino.api.ac.validation;

import com.ac.api.data.timeseries.CheckFunctionMask;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class TsValidation {

    public static List<CheckFunctionMask.CheckFunction> VAL_FUNCS = Arrays.asList(
            CheckFunctionMask.CheckFunction.OHLC,
            CheckFunctionMask.CheckFunction.BMA,
            CheckFunctionMask.CheckFunction.NON_POSITIVE,
            CheckFunctionMask.CheckFunction.PERCENTAGE,
            CheckFunctionMask.CheckFunction.STDDEV,
            CheckFunctionMask.CheckFunction.STDEV_LOG,
            CheckFunctionMask.CheckFunction.FORMULA
    );

    public abstract TsValidationInfo mergeWith(TsValidationInfo existingValidationInfo,
                                               BiFunction<String, String, String> amendFormula,
                                               BiFunction<CheckFunctionMask, CheckFunctionMask.CheckFunction, CheckFunctionMask> amendChecks);

    public static String removeAllFormulas(String formulaStr1, String formulaStr2) {
        return null;
    }

    public static String removeFormula(String formulaStr1, String formulaStr2) {
        List<String> formulas1 = (formulaStr1 != null) ? Arrays.asList(formulaStr1.split(";")) : Collections.emptyList();
        List<String> formulas2 = (formulaStr2 != null) ? Arrays.asList(formulaStr2.split(";")) : Collections.emptyList();
        List<String> remaining = formulas1.stream().filter(f -> !formulas2.contains(f)).collect(Collectors.toList());
        return (remaining.size() > 0) ? remaining.stream().collect(Collectors.joining(";"))+";" : "";
    }

    public static String appendFormula(String formulaStr1, String formulaStr2) {
        List<String> formulas = (formulaStr1 != null) ? Arrays.asList(formulaStr1.split(";")) : new ArrayList<>();
        List<String> formulas2 = (formulaStr2 != null) ? Arrays.asList(formulaStr2.split(";")) : new ArrayList<>();
        List<String> formulasToAdd = formulas2.stream().filter(f -> !formulas.contains(f)).collect(Collectors.toList());
        List<String> allFormulas = new ArrayList<>();
        allFormulas.addAll(formulas);
        allFormulas.addAll(formulasToAdd);
        List<String> resulting = allFormulas.stream().filter(f -> !f.isEmpty()).collect(Collectors.toList());
        return (resulting.size() > 0) ? resulting.stream().collect(Collectors.joining(";"))+";" : "";
    }

    public static CheckFunctionMask removeAllChecks(CheckFunctionMask mask, CheckFunctionMask.CheckFunction valFunc) {
        return CheckFunctionMask.getInstance(0);
    }

    public static CheckFunctionMask removeCheck(CheckFunctionMask mask, CheckFunctionMask.CheckFunction checkToRemove) {
        List<CheckFunctionMask.CheckFunction> checks = new ArrayList<>();
        VAL_FUNCS.forEach(c -> { if (mask.hasCheck(c) && !c.equals(checkToRemove)) checks.add(c);});
        return CheckFunctionMask.getInstance(checks.toArray(new CheckFunctionMask.CheckFunction[checks.size()]));

    }

    public static CheckFunctionMask appendCheck(CheckFunctionMask mask, CheckFunctionMask.CheckFunction checkToAdd) {
        if (!Optional.ofNullable(checkToAdd).isPresent()) {
            return mask;
        }
        if (mask.hasCheck(checkToAdd)) {
            return mask;
        } else {
            List<CheckFunctionMask.CheckFunction> checks = new ArrayList<>();
            checks.add(checkToAdd);
            VAL_FUNCS.forEach(c -> { if (mask.hasCheck(c)) checks.add(c);});
            return CheckFunctionMask.getInstance(checks.toArray(new CheckFunctionMask.CheckFunction[checks.size()]));
        }
    }

}
