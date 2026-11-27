package org.veupathdb.service.eda.merge.plugins.transforms;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.merge.core.derivedvars.Transform;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoricalRecoding extends Transform<CategoricalRecodingConfig> {

  private VariableSpec _inputVar;
  private String _inputColumn;
  private List<CategoricalRecodingRule> _recodingRules;
  private String _unmappedValue;

  @Override
  protected Class<CategoricalRecodingConfig> getConfigClass() {
    return CategoricalRecodingConfig.class;
  }

  @Override
  protected void acceptConfig(CategoricalRecodingConfig config) throws ValidationException {
    for (CategoricalRecodingRule rule : config.getRules()) {
      if (isNullOrEmpty(rule.getInputValues()) && isNullOrEmpty(rule.getInputPrefixes())) {
        throw new ValidationException("Each rule needs inputValues or inputPrefixes");
      }
    }
    _inputVar = config.getInputVariable();
    _inputColumn = VariableDef.toDotNotation(_inputVar);
    _recodingRules = config.getRules();
    _unmappedValue = Optional.ofNullable(config.getUnmappedValue()).orElse(EMPTY_VALUE);
  }

  @Override
  protected void performSupplementalDependedVariableValidation() throws ValidationException {
    checkVariable("Input", _inputVar, null, List.of(APIVariableDataShape.CATEGORICAL, APIVariableDataShape.BINARY));
    checkNotMultiValued(_metadata.getVariable(_inputVar).orElseThrow());
  }

  public static void checkNotMultiValued(VariableDef var) throws ValidationException {
    if (var.isMultiValue()) {
      throw new ValidationException("Input variable must not be multi-valued");
    }
  }

  private static boolean isNullOrEmpty(List<String> list) {
    return list == null || list.isEmpty();
  }

  @Override
  public String getFunctionName() {
    return "categoricalRecoding";
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return List.of(_inputVar);
  }

  @Override
  public APIVariableType getVariableType() {
    return APIVariableType.STRING;
  }

  @Override
  public APIVariableDataShape getDataShape() {
    return APIVariableDataShape.CATEGORICAL;
  }

  @Override
  public List<String> getVocabulary() {
    return RecodingVocabulary.of(
      _recodingRules.stream().map(CategoricalRecodingRule::getOutputValue).toList(), _unmappedValue);
  }

  @Override
  public String getValue(Map<String, String> row) {
    String v = row.get(_inputColumn);
    if (v != null && !v.isEmpty()) {
      for (CategoricalRecodingRule rule : _recodingRules) {
        if (matches(rule, v)) {
          // return output value for the first match
          return rule.getOutputValue();
        }
      }
    }
    return _unmappedValue;
  }

  private static boolean matches(CategoricalRecodingRule rule, String v) {
    List<String> values = rule.getInputValues();
    List<String> prefixes = rule.getInputPrefixes();
    return (values != null && values.contains(v)) ||
      (prefixes != null && prefixes.stream().anyMatch(v::startsWith));
  }
}
