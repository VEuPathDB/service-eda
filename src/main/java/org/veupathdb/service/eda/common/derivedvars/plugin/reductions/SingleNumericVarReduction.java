package org.veupathdb.service.eda.common.derivedvars.plugin.reductions;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.common.derivedvars.plugin.Reduction;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;

import java.util.Collections;
import java.util.List;

public abstract class SingleNumericVarReduction extends Reduction<SingleNumericVarReductionConfig> {

  protected VariableSpec _inputColumn;
  protected String _inputColumnName;

  @Override
  protected Class<SingleNumericVarReductionConfig> getConfigClass() {
    return SingleNumericVarReductionConfig.class;
  }

  @Override
  protected void acceptConfig(SingleNumericVarReductionConfig config) {
    _inputColumn = config.getInputVariable();
    _inputColumnName = VariableDef.toDotNotation(_inputColumn);
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return List.of(_inputColumn);
  }

  @Override
  public List<DerivedVariableSpec> getDependedDerivedVarSpecs() {
    return Collections.emptyList();
  }

  @Override
  public void validateDependedVariables() {
    VariableDef inputVar = _metadata.getVariable(_inputColumn).orElseThrow(() ->
        new BadRequestException("Variable " + VariableDef.toDotNotation(_inputColumn) + " does not exist."));
    if (!inputVar.getType().equals(APIVariableType.NUMBER)) {
      throw new BadRequestException(getFunctionName() + " reduction accepts only a single variable of type " + APIVariableType.NUMBER);
    }
  }

  @Override
  public APIVariableType getVariableType() {
    return APIVariableType.NUMBER;
  }

  @Override
  public APIVariableDataShape getVariableDataShape() {
    return APIVariableDataShape.CONTINUOUS;
  }
}
