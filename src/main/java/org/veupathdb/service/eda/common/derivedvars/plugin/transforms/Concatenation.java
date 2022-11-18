package org.veupathdb.service.eda.common.derivedvars.plugin.transforms;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.derivedvars.plugin.Transform;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;

public class Concatenation extends Transform {

  @Override
  protected void receiveInputVariables(List<VariableDef> inputVariables) throws ValidationException {
    // no validation needed; all number and type of vars allowed
  }

  @Override
  public String getValue(Map<String, String> row) {
    return _inputColumnNames.stream()
      .map(col -> row.get(col))
      .collect(Collectors.joining());
  }

  @Override
  public APIVariableType getVariableType() {
    return APIVariableType.STRING;
  }

  @Override
  public APIVariableDataShape getVariableDataShape() {
    return APIVariableDataShape.CONTINUOUS;
  }
}
