package org.veupathdb.service.eda.merge.plugins.transforms;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.ResourceLimits;
import org.gusdb.fgputil.validation.ValidationException;

import javax.script.ScriptException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Evaluates a user-supplied JavaScript expression as the body of a function, i.e.
 * <code>function evalExpression(a, b) { return &lt;expression&gt;; }</code>, inside
 * a restricted GraalJS context.
 *
 * Security properties:
 * <ul>
 *   <li>No host access (Java interop), IO, threads, native access, or environment access.
 *       These are GraalVM defaults when a custom Context.Builder is supplied; do NOT add
 *       allowHostAccess, allowAllAccess, or allowHostClassLookup.</li>
 *   <li>No code generation from strings (eval(), Function(), etc.)</li>
 *   <li>One statement per call; prohibits loops, callbacks, and code injected outside
 *       the function body</li>
 *   <li>Wall-clock time limit per call; the context is cancelled if exceeded, after
 *       which all calls fail</li>
 *   <li>Limits on expression and result length; parameter names must be identifiers</li>
 * </ul>
 *
 * Instances are not thread-safe.  Unclosed contexts are reclaimed by GraalVM after
 * the instance is garbage collected, so closing is optional.
 */
public class SandboxedJsFunction implements AutoCloseable {

  static final int MAX_EXPRESSION_LENGTH = 500;
  static final long MAX_STATEMENTS_PER_CALL = 1;
  static final long MAX_MILLIS_PER_CALL = 100;
  static final int MAX_RESULT_LENGTH = 10_000;

  private static final String FUNCTION_NAME = "evalExpression";

  // parameter names are inserted into the function source, so restrict to simple identifiers
  private static final Pattern IDENTIFIER = Pattern.compile("^[A-Za-z_$][A-Za-z0-9_$]*$");

  // single engine shared by all contexts; thread-safe, shares code caches, makes context creation cheap
  private static final Engine SHARED_ENGINE = Engine.newBuilder("js").build();

  // shared watchdog which cancels evaluations that exceed MAX_MILLIS_PER_CALL
  private static final ScheduledThreadPoolExecutor WATCHDOG;
  static {
    WATCHDOG = new ScheduledThreadPoolExecutor(1, runnable -> {
      Thread thread = new Thread(runnable, "ecmascript-expression-watchdog");
      thread.setDaemon(true);
      return thread;
    });
    WATCHDOG.setRemoveOnCancelPolicy(true);
  }

  private final String _expression;
  private final GraalJSScriptEngine _engine;
  private final Context _context;

  /**
   * Creates the sandbox and compiles the expression.
   *
   * @param parameterNames names of the function parameters available to the expression
   * @param expression JavaScript expression to evaluate
   * @throws ValidationException if a parameter name or the expression is invalid
   */
  public SandboxedJsFunction(List<String> parameterNames, String expression) throws ValidationException {
    if (expression == null || expression.isBlank()) {
      throw new ValidationException("JavaScript expression is required.");
    }
    if (expression.length() > MAX_EXPRESSION_LENGTH) {
      throw new ValidationException("JavaScript expression may not exceed " + MAX_EXPRESSION_LENGTH + " characters.");
    }
    for (String name : parameterNames) {
      if (name == null || !IDENTIFIER.matcher(name).matches()) {
        throw new ValidationException("Invalid expression variable name: " + name);
      }
    }
    _expression = expression;
    _engine = GraalJSScriptEngine.create(SHARED_ENGINE,
      Context.newBuilder("js")
        // disallow code generation from strings (eval(), Function(), etc.)
        .option("js.allow-eval", "false")
        // the expression must evaluate as a single statement (the function's return); internal
        //   sources (the script engine's own setup scripts) are excluded from the count
        .resourceLimits(ResourceLimits.newBuilder()
          .statementLimit(MAX_STATEMENTS_PER_CALL, source -> !source.isInternal())
          .build())
    );
    _context = _engine.getPolyglotContext();
    try {
      _engine.eval("function " + FUNCTION_NAME + "(" + String.join(", ", parameterNames) + ") { return " + expression + "; }");
    }
    catch (ScriptException | PolyglotException e) {
      close();
      throw new ValidationException("JavaScript expression is not valid: " + expression);
    }
  }

  /**
   * Evaluates the expression with the passed arguments.
   *
   * @param args argument values (Integer, Double, String, or null) in parameter order
   * @return string value of the expression result
   * @throws RuntimeException if evaluation fails, produces no value, or exceeds limits
   */
  public String invoke(Object... args) {
    // each call gets a fresh statement budget
    _context.resetLimits();
    // cancel the context if this call takes too long
    AtomicBoolean timedOut = new AtomicBoolean(false);
    Context context = _context;
    ScheduledFuture<?> watchdog = WATCHDOG.schedule(() -> {
      timedOut.set(true);
      context.close(true);
    }, MAX_MILLIS_PER_CALL, TimeUnit.MILLISECONDS);
    try {
      Object result = _engine.invokeFunction(FUNCTION_NAME, args);
      if (result == null) {
        throw new RuntimeException("JavaScript expression produced no value (null or undefined): " + _expression);
      }
      String resultStr = result.toString();
      if (resultStr.length() > MAX_RESULT_LENGTH) {
        throw new RuntimeException("JavaScript expression result exceeded maximum length of " + MAX_RESULT_LENGTH + " characters.");
      }
      return resultStr;
    }
    catch (NoSuchMethodException e) {
      // should never happen since the function is defined in the constructor
      throw new RuntimeException("Function called that was not defined in script engine.", e);
    }
    catch (ScriptException | PolyglotException | IllegalStateException e) {
      if (timedOut.get()) {
        throw new RuntimeException("JavaScript expression evaluation exceeded maximum time of " + MAX_MILLIS_PER_CALL + "ms.", e);
      }
      throw new RuntimeException("Unable to perform expression evaluation with the following args: " +
        Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(", ")), e);
    }
    finally {
      watchdog.cancel(false);
    }
  }

  /**
   * Closes the underlying polyglot context.  Safe to call more than once.
   */
  @Override
  public void close() {
    try {
      _context.close(true);
    }
    catch (RuntimeException e) {
      // already closed or cancelled
    }
  }
}
