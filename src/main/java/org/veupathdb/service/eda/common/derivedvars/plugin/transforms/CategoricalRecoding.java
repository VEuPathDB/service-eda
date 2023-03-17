package org.veupathdb.service.eda.common.derivedvars.plugin.transforms;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.derivedvars.plugin.Transform;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;

import java.util.*;

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
    _inputVar = config.getInputVariable();
    _inputColumn = VariableDef.toDotNotation(_inputVar);
    _recodingRules = config.getRules();
    _unmappedValue = Optional.ofNullable(config.getUnmappedValue()).orElse("");
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
  public APIVariableDataShape getVariableDataShape() {
    return APIVariableDataShape.CATEGORICAL;
  }

  @Override
  public Optional<List<String>> getVocabulary() {
    List<String> vocab = new ArrayList<>(_recodingRules.stream().map(CategoricalRecodingRule::getOutputValue).toList());
    vocab.add(_unmappedValue);
    return Optional.of(vocab);
  }

  @Override
  public String getValue(Map<String, String> row) {
    String inputValue = row.get(_inputColumn);
    for (CategoricalRecodingRule rule : _recodingRules) {
      if (rule.getInputValues().contains(inputValue)) {
        // return output value for the first match
        return rule.getOutputValue();
      }
    }
    return _unmappedValue;
  }
}
