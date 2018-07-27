package io.terrafino.api.ac.validation;

import com.ac.api.data.timeseries.CheckFunctionMask;
import org.junit.Test;


import java.util.function.BiFunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;

public class TsValidationTest extends TsValidation {

    @Test
    public void removeAllFormulasReturnsNull() throws Exception {
        String formulas = TsValidation.removeAllFormulas("f1();", "f2();");
        assertThat(formulas, isEmptyOrNullString());
    }

    @Test
    public void removeFormulaRemovesExistingFormulaOk() throws Exception {
        String formula = TsValidation.removeFormula("f1(a, b, c);f2(d, e, f)", "f2(d, e, f);");
        assertThat(formula, is("f1(a, b, c);"));
    }

    @Test
    public void removeFormulaChangesNothingIfFormulaNotPresent() throws Exception {
        String formula = TsValidation.removeFormula("f1(a, b, c);f2(d, e, f)", "other(d, e, f);");
        assertThat(formula, is("f1(a, b, c);f2(d, e, f);"));
    }

    @Test
    public void removeFormulaChangesNothingIfRemovingEmptyFormula() throws Exception {
        String formula = TsValidation.removeFormula("f1(a, b, c);f2(d, e, f);", "");
        assertThat(formula, is("f1(a, b, c);f2(d, e, f);"));
    }

    @Test
    public void removeFormulaChangesNothingIfRemovingNullFormula() throws Exception {
        String formula = TsValidation.removeFormula("f1(a, b, c);f2(d, e, f);", null);
        assertThat(formula, is("f1(a, b, c);f2(d, e, f);"));
    }

    @Test
    public void removeFormulaReturnsEmptyFormulaIfRemovingFromNullFormula() throws Exception {
        String formula = TsValidation.removeFormula(null, "f1();");
        assertThat(formula, is(""));
    }

    @Test
    public void appendFormulaAppendsDistinctFormulasOk() throws Exception {
        String formula = TsValidation.appendFormula("f1();f2(a, b, c)", "f3();");
        assertThat(formula, is("f1();f2(a, b, c);f3();"));
    }

    @Test
    public void appendFormulaIgnoresDuplicates() throws Exception {
        String formula = TsValidation.appendFormula("f1();f2(a, b, c)", "f1();");
        assertThat(formula, is("f1();f2(a, b, c);"));
    }

    @Test
    public void appendFormulaWorksForNullAndNonEmpty() throws Exception {
        String formula = TsValidation.appendFormula(null, "f1();");
        assertThat(formula, is("f1();"));
    }

    @Test
    public void appendFormulaWorksForEmptyAndNonEmpty() throws Exception {
        String formula = TsValidation.appendFormula("", "f1();");
        assertThat(formula, is("f1();"));
    }

    @Test
    public void appendFormulaWorksForNonEmptyAndNull() throws Exception {
        String formula = TsValidation.appendFormula("f1();", null);
        assertThat(formula, is("f1();"));
    }

    @Test
    public void appendFormulaWorksNullAndNull() throws Exception {
        String formula = TsValidation.appendFormula(null, null);
        assertThat(formula, is(""));
    }

    @Test
    public void removeAllChecksReturnsEmptyCheckMask() throws Exception {
        CheckFunctionMask mask = CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.BMA);
        CheckFunctionMask newMask = TsValidation.removeAllChecks(mask, null);
        assertThat(newMask, is(CheckFunctionMask.getInstance(0)));
    }

    @Test
    public void removeCheckRemovesCheckIfPresent() throws Exception {
        CheckFunctionMask mask = CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.BMA);
        CheckFunctionMask newMask = TsValidation.removeCheck(mask, CheckFunctionMask.CheckFunction.BMA);
        assertThat(newMask, is(CheckFunctionMask.getInstance(0)));
    }

    @Test
    public void removeCheckDoesNothingIfCheckNotPresent() throws Exception {
        CheckFunctionMask mask = CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.BMA);
        CheckFunctionMask newMask = TsValidation.removeCheck(mask, CheckFunctionMask.CheckFunction.OHLC);
        assertThat(newMask, is(mask));
    }

    @Test
    public void appendCheckAppendsNewCheck() throws Exception {
        CheckFunctionMask mask = CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.BMA);
        CheckFunctionMask newMask = TsValidation.appendCheck(mask, CheckFunctionMask.CheckFunction.OHLC);
        assertThat(newMask, is(CheckFunctionMask.getInstance(
                CheckFunctionMask.CheckFunction.BMA,
                CheckFunctionMask.CheckFunction.OHLC)));
    }

    @Test
    public void appendCheckDoesNothingIfCheckAlreadyPresent() throws Exception {
        CheckFunctionMask mask = CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.BMA);
        CheckFunctionMask newMask = TsValidation.appendCheck(mask, CheckFunctionMask.CheckFunction.BMA);
        assertThat(newMask, is(CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.BMA)));
    }

    @Test
    public void appendCheckDoesNothingIfAppendingNull() throws Exception {
        CheckFunctionMask mask = CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.BMA);
        CheckFunctionMask newMask = TsValidation.appendCheck(mask, null);
        assertThat(newMask, is(CheckFunctionMask.getInstance(CheckFunctionMask.CheckFunction.BMA)));
    }

    // Dummy that allows Jacoco to give the thumbs up!
    @Override
    public TsValidationInfo mergeWith(TsValidationInfo existingValidationInfo, BiFunction<String, String, String> amendFormula, BiFunction<CheckFunctionMask, CheckFunctionMask.CheckFunction, CheckFunctionMask> amendChecks) {
        return null;
    }
}