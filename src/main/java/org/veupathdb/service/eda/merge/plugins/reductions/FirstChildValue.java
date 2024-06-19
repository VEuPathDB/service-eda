package org.veupathdb.service.eda.merge.plugins.reductions;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.FirstChildValueConfig;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.merge.core.derivedvars.Reduction;

import java.util.List;
import java.util.Map;

public class FirstChildValue extends Reduction<FirstChildValueConfig> {

  private VariableSpec _childVariable;
  private String _childVariableColumnName;
  private VariableDef _childVariableDef;

  @Override
  public String getFunctionName() {
    return "firstChildValue";
  }

  @Override
  protected Class<FirstChildValueConfig> getConfigClass() {
    return FirstChildValueConfig.class;
  }

  @Override
  protected void acceptConfig(FirstChildValueConfig config) throws ValidationException {
    _childVariable = config.getInputVariable();
    _childVariableColumnName = VariableDef.toDotNotation(_childVariable);
  }

  @Override
  protected void performSupplementalDependedVariableValidation() throws ValidationException {
    // metadata populated now; get def to return type and shape
    _childVariableDef = _metadata.getVariable(_childVariable).get(); // already validated
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return List.of(_childVariable);
  }

  @Override
  public APIVariableType getVariableType() {
    return _childVariableDef.getType();
  }

  @Override
  public APIVariableDataShape getDataShape() {
    return _childVariableDef.getDataShape();
  }

  @Override
  public Reducer createReducer() {
    return new Reducer() {

      private String _firstValue;

      @Override
      public void addRow(Map<String, String> nextRow) {
        // only set the first value returned
        if (_firstValue == null)
          _firstValue = nextRow.get(_childVariableColumnName);
      }

      @Override
      public String getResultingValue() {
        return _firstValue == null ? "" : _firstValue;
      }
    };
  }
}
