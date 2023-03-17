package org.veupathdb.service.eda.common.derivedvars.plugin.reductions;

import jakarta.ws.rs.BadRequestException;
import org.veupathdb.service.eda.common.derivedvars.plugin.Reduction;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class SingleNumericVarReduction extends Reduction<SingleNumericVarReductionConfig> {

  protected abstract class SingleNumericVarReducer implements Reducer {

    protected abstract void processValue(double d);

    @Override
    public void addRow(Map<String, String> nextRow) {
      String s = nextRow.get(_inputColumnName);
      if (s.isBlank()) {
        if (_imputeZero) {
          processValue(0);
        }
        // otherwise, skip empty values (TBD: is this a good API?)
      }
      else {
        processValue(Double.parseDouble(s));
      }
    }
  }

  protected VariableSpec _inputColumn;
  protected String _inputColumnName;
  protected boolean _imputeZero;

  @Override
  protected Class<SingleNumericVarReductionConfig> getConfigClass() {
    return SingleNumericVarReductionConfig.class;
  }

  @Override
  protected void acceptConfig(SingleNumericVarReductionConfig config) {
    _inputColumn = config.getInputVariable();
    _inputColumnName = VariableDef.toDotNotation(_inputColumn);
    _imputeZero = Optional.ofNullable(config.getImputeZero()).orElse(false);
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return List.of(_inputColumn);
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
