package org.veupathdb.service.eda.merge.plugins.transforms;

import org.gusdb.fgputil.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that user-supplied expressions can perform inline operations but cannot
 * escape the sandbox or consume unbounded resources.
 */
@Timeout(value = 30, unit = TimeUnit.SECONDS)
public class SandboxedJsFunctionTest {

  private static final List<String> PARAMS = List.of("x", "s");

  private static String eval(String expression, Object x, Object s) throws ValidationException {
    try (SandboxedJsFunction function = new SandboxedJsFunction(PARAMS, expression)) {
      return function.invoke(x, s);
    }
  }

  private static void assertRejectedAtValidation(String expression, String expectedMessagePart) {
    ValidationException e = assertThrows(ValidationException.class, () -> new SandboxedJsFunction(PARAMS, expression));
    assertTrue(e.getMessage().contains(expectedMessagePart), "Unexpected message: " + e.getMessage());
  }

  private static RuntimeException assertFailsAtEvaluation(String expression) throws ValidationException {
    try (SandboxedJsFunction function = new SandboxedJsFunction(PARAMS, expression)) {
      return assertThrows(RuntimeException.class, () -> function.invoke(1, "abc"));
    }
  }

  private static String fullMessage(Throwable t) {
    StringBuilder sb = new StringBuilder();
    for (; t != null; t = t.getCause()) sb.append(t.getMessage()).append(" | ");
    return sb.toString();
  }

  //////////////////////////////////////////////////////////////////
  // legitimate expressions
  //////////////////////////////////////////////////////////////////

  @Test
  public void arithmetic() throws ValidationException {
    assertEquals("4.5", eval("x + 1.5", 3, "abcd"));
    assertEquals("6", eval("x * 2", 3, "abcd"));
  }

  @Test
  public void mathFunctions() throws ValidationException {
    // numeric results are Java Doubles
    assertEquals("3.0", eval("Math.sqrt(x)", 9, "abcd"));
    assertEquals(String.valueOf(Math.exp(1) * 3 + 9), eval("Math.exp(1) * Math.sqrt(x) + Math.pow(x, 2) / 9", 9.0, "abcd"));
  }

  @Test
  public void stringFunctions() throws ValidationException {
    assertEquals("bc", eval("s.toUpperCase().startsWith('AB') ? s.substr(1, 2) : s.indexOf('c')", 1, "abcd"));
    assertEquals("2", eval("s.toUpperCase().startsWith('XY') ? s.substr(1, 2) : s.indexOf('c')", 1, "abcd"));
    assertEquals("abcd-9", eval("s + '-' + x", 9, "abcd"));
  }

  @Test
  public void nullArguments() throws ValidationException {
    assertEquals("missing", eval("s === null ? 'missing' : s", 1, null));
  }

  @Test
  public void multipleCallsEachGetFreshStatementBudget() throws ValidationException {
    try (SandboxedJsFunction function = new SandboxedJsFunction(PARAMS, "x + 1")) {
      for (int i = 0; i < 1000; i++) {
        assertEquals(String.valueOf(i + 1), function.invoke(i, ""));
      }
    }
  }

  //////////////////////////////////////////////////////////////////
  // rejected during validation
  //////////////////////////////////////////////////////////////////

  @ParameterizedTest
  @ValueSource(strings = { "", "   " })
  public void blankExpressionRejected(String expression) {
    assertRejectedAtValidation(expression, "required");
  }

  @Test
  public void nullExpressionRejected() {
    assertRejectedAtValidation(null, "required");
  }

  @Test
  public void overlongExpressionRejected() {
    String expression = "x" + " + 1".repeat(SandboxedJsFunction.MAX_EXPRESSION_LENGTH);
    assertRejectedAtValidation(expression, "may not exceed");
  }

  @Test
  public void syntaxErrorRejected() {
    assertRejectedAtValidation("x +", "not valid");
  }

  @Test
  public void injectionOutsideFunctionBodyRejected() {
    // closes the function body early and runs a top-level infinite loop at definition time
    assertRejectedAtValidation("0; } while(true){} function zz(){ return 0", "not valid");
  }

  @ParameterizedTest
  @ValueSource(strings = { "x) { while(true){} } function y(", "1x", "a-b", "a b", "" })
  public void invalidParameterNamesRejected(String name) {
    ValidationException e = assertThrows(ValidationException.class,
      () -> new SandboxedJsFunction(List.of("x", name), "x"));
    assertTrue(e.getMessage().contains("Invalid expression variable name"), "Unexpected message: " + e.getMessage());
  }

  //////////////////////////////////////////////////////////////////
  // no access to host (Java) or code generation
  //////////////////////////////////////////////////////////////////

  @ParameterizedTest
  @ValueSource(strings = {
    "Java.type('java.lang.Runtime').getRuntime().exec('id')",
    "Packages.java.lang.System.exit(1)",
    "java.lang.System.getenv()",
    "Polyglot.eval('js', '1')",
    "load('/etc/passwd')",
  })
  public void hostAccessUnavailable(String expression) throws ValidationException {
    assertFailsAtEvaluation(expression);
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "eval('1 + 1')",
    "Function('return 1')()",
    "(() => 0).constructor('return 1')()",
  })
  public void codeGenerationFromStringsDisabled(String expression) throws ValidationException {
    RuntimeException e = assertFailsAtEvaluation(expression);
    assertTrue(fullMessage(e).contains("dynamic evaluation of code is disabled"), fullMessage(e));
  }

  //////////////////////////////////////////////////////////////////
  // resource limits
  //////////////////////////////////////////////////////////////////

  @ParameterizedTest
  @ValueSource(strings = {
    "(() => { while(true){} })()",
    "(() => { for(;;){} })()",
    "s.split('').map(c => c + c).join('')",
    "Array(10).fill(0).map(v => v).length",
  })
  public void statementLimitPreventsLoopsAndCallbacks(String expression) throws ValidationException {
    RuntimeException e = assertFailsAtEvaluation(expression);
    assertTrue(fullMessage(e).contains("Statement count limit"), fullMessage(e));
  }

  @ParameterizedTest
  @ValueSource(strings = {
    // backreference forces backtracking regex engine; runs ~30s uninterrupted
    "/^(a+)+\\1b$/.test('a'.repeat(28))",
    // builtin work does not count as statements
    "Array(2e6).fill(1.5).sort().length",
  })
  public void slowBuiltinsCancelledByTimeLimit(String expression) throws ValidationException {
    try (SandboxedJsFunction function = new SandboxedJsFunction(PARAMS, expression)) {
      long start = System.currentTimeMillis();
      RuntimeException e = assertThrows(RuntimeException.class, () -> function.invoke(1, "abc"));
      long elapsed = System.currentTimeMillis() - start;
      assertTrue(e.getMessage().contains("exceeded maximum time"), fullMessage(e));
      assertTrue(elapsed < 5_000, "Cancellation took too long: " + elapsed + "ms");
      // a cancelled function cannot be reused
      assertThrows(RuntimeException.class, () -> function.invoke(1, "abc"));
    }
  }

  @Test
  public void overlongResultRejected() throws ValidationException {
    RuntimeException e = assertFailsAtEvaluation("'x'.repeat(" + (SandboxedJsFunction.MAX_RESULT_LENGTH + 1) + ")");
    assertTrue(e.getMessage().contains("exceeded maximum length"), fullMessage(e));
  }

  @Test
  public void undefinedResultRejected() throws ValidationException {
    RuntimeException e = assertFailsAtEvaluation("undefined");
    assertTrue(e.getMessage().contains("produced no value"), fullMessage(e));
  }
}
