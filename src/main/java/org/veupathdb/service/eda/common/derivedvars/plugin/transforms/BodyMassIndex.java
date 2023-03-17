package org.veupathdb.service.eda.common.derivedvars.plugin.transforms;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.*;

import java.util.List;
import java.util.Map;

public class BodyMassIndex extends org.veupathdb.service.eda.common.derivedvars.plugin.Transform<BodyMassIndexConfig> {

  private VariableSpec _heightVar;
  private String _heightColumn;
  private VariableSpec _weightVar;
  private String _weightColumn;

  @Override
  protected Class<BodyMassIndexConfig> getConfigClass() {
    return BodyMassIndexConfig.class;
  }

  @Override
  protected void acceptConfig(BodyMassIndexConfig config) throws ValidationException {
    _heightVar = config.getHeightVariable();
    _heightColumn = VariableDef.toDotNotation(_heightVar);
    _weightVar = config.getWeightVariable();
    _weightColumn = VariableDef.toDotNotation(_weightVar);
  }

  @Override
  public String getFunctionName() {
    return "bodyMassIndex";
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return List.of(_heightVar, _weightVar);
  }

  @Override
  public APIVariableType getVariableType() {
    return APIVariableType.NUMBER;
  }

  @Override
  public APIVariableDataShape getVariableDataShape() {
    return APIVariableDataShape.CONTINUOUS;
  }

  @Override
  public String getValue(Map<String, String> row) {
    // BMI = kg/(m^2), TBD: assume metric measures for now
    return String.valueOf(Double.parseDouble(row.get(_weightColumn)) / Math.pow(Double.parseDouble(row.get(_heightColumn)), 2));
  }
}
