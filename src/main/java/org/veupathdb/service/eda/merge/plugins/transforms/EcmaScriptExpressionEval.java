package org.veupathdb.service.eda.merge.plugins.transforms;

import org.gusdb.fgputil.Tuples.TwoTuple;
import org.gusdb.fgputil.functional.Functions;
import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;
import org.veupathdb.service.eda.merge.core.derivedvars.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EcmaScriptExpressionEval extends Transform<EcmaScriptExpressionEvalConfig> {

  private String _scriptExpression;
  private List<VariableReference> _variableRefs;
  private APIVariableType _expectedType;
  private APIVariableDataShape _expectedShape;
  private boolean _nullResultOnAnyMissingInput;
  private SandboxedJsFunction _function;
  private List<TwoTuple<String, APIVariableType>> _scriptParams;

  @Override
  protected Class<EcmaScriptExpressionEvalConfig> getConfigClass() {
    return EcmaScriptExpressionEvalConfig.class;
  }

  @Override
  protected void acceptConfig(EcmaScriptExpressionEvalConfig config) {
    _scriptExpression = config.getEcmaScriptExpression();
    _variableRefs = config.getInputVariables();
    _expectedType = config.getExpectedType();
    _expectedShape = config.getExpectedShape();
    _nullResultOnAnyMissingInput = config.getNullResultOnAnyMissingInput();
  }

  @Override
  protected void performSupplementalDependedVariableValidation() throws ValidationException {
    for (VariableSpec spec : getRequiredInputVars()) {
      checkVariable("Input variable", spec, List.of(
        // only allow these input types for now
        APIVariableType.INTEGER,
        APIVariableType.NUMBER,
        APIVariableType.STRING
      ), null); // any shape is fine
    }
    // need to wait and set up the script engine here since we need to know the types of the input vars
    _function = new SandboxedJsFunction(
      _variableRefs.stream().map(VariableReference::getName).toList(),
      _scriptExpression);
    _scriptParams = _variableRefs.stream()
      .map(ref -> _metadata.getVariable(ref.getVariable()).orElseThrow())
      .map(var -> new TwoTuple<>(VariableDef.toDotNotation(var), var.getType()))
      .toList();
  }

  @Override
  public String getFunctionName() {
    return "ecmaScriptExpressionEval";
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return _variableRefs.stream().map(VariableReference::getVariable).toList();
  }

  @Override
  public APIVariableType getVariableType() {
    return _expectedType;
  }

  @Override
  public APIVariableDataShape getDataShape() {
    return _expectedShape;
  }

  /*************************************************************
   * TODO: TBD what about range, vocab, units?
   *************************************************************/

  @Override
  public String getValue(Map<String, String> row) {
    // TODO: (maybe?) can coerce result the same way as getParameters() based on expected data type; this is not
    //   strictly necessary since we will always write strings, but would be an outgoing data integrity check.  Users
    //   should not be able to claim they will produce an integer with the expression and then produce non-integer
    //   strings.  Downside is conversion of every produced value to native type and back to String is expensive.
    return getParameters(row).map(args -> _function.invoke(args.toArray())).orElse(EMPTY_VALUE);
  }

  /**
   * Gathers the needed expression parameters from this row of data and converts them
   * into their native data types.  May return an empty optional if null result flag is
   * true and any columns have empty values.
   *
   * @param row row of data
   * @return parameters for the JS expression function
   */
  private Optional<List<Object>> getParameters(Map<String, String> row) {
    List<Object> parameters = new ArrayList<>();
    for (TwoTuple<String, APIVariableType> var : _scriptParams) {
      String valueStr = row.get(var.getKey());
      if (valueStr.isEmpty()) {
        if (_nullResultOnAnyMissingInput)
          return Optional.empty();
        else
          parameters.add(null);
      }
      else { // non-empty value; coerce to the correct object
        Object obj = switch (var.getSecond()) {
          case INTEGER -> Integer.parseInt(valueStr);
          case NUMBER -> Double.parseDouble(valueStr);
          case STRING -> valueStr;
          default -> Functions.doThrow(() -> new IllegalStateException("Should already have checked variable types."));
        };
        parameters.add(obj);
      }
    }
    return Optional.of(parameters);
  }
}
