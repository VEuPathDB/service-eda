package org.veupathdb.service.eda.common.derivedvars.plugin.transforms;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.derivedvars.plugin.Transform;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.UnitConversionConfig;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.List;
import java.util.Map;

public class UnitConversion extends Transform<UnitConversionConfig> {

  private VariableSpec _inputVariable;
  private String _outputUnits;

  @Override
  protected Class<UnitConversionConfig> getConfigClass() {
    return UnitConversionConfig.class;
  }

  @Override
  protected void acceptConfig(UnitConversionConfig config) throws ValidationException {
    _inputVariable = config.getInputVariable();
    _outputUnits = config.getOutputUnits();
  }

  @Override
  protected void performSupplementalDependedVariableValidation() throws ValidationException {
    if (_metadata.getVariable(_inputVariable).orElseThrow().getUnits().isEmpty()) {
      throw new ValidationException("Input variable must have units to convert to a different units.");
    }
  }

  @Override
  public String getFunctionName() {
    return "unitConversion";
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return List.of(_inputVariable);
  }

  @Override
  public APIVariableType getVariableType() {
    return _metadata.getVariable(_inputVariable).orElseThrow().getType();
  }

  @Override
  public APIVariableDataShape getVariableDataShape() {
    return _metadata.getVariable(_inputVariable).orElseThrow().getDataShape();
  }

  @Override
  public String getValue(Map<String, String> row) {
    return null;
  }
}
