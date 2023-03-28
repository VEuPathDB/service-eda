package org.veupathdb.service.eda.common.derivedvars.plugin.transforms;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.derivedvars.plugin.Transform;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.EcmaScriptExpressionEvalConfig;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.List;
import java.util.Map;

public class EcmaScriptExpressionEval extends Transform<EcmaScriptExpressionEvalConfig> {

  @Override
  protected Class<EcmaScriptExpressionEvalConfig> getConfigClass() {
    return EcmaScriptExpressionEvalConfig.class;
  }

  @Override
  protected void acceptConfig(EcmaScriptExpressionEvalConfig config) throws ValidationException {

  }

  @Override
  protected void performSupplementalDependedVariableValidation() throws ValidationException {

  }

  @Override
  public String getFunctionName() {
    return "ecmaScriptExpressionEval";
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return null;
  }

  @Override
  public APIVariableType getVariableType() {
    return null;
  }

  @Override
  public APIVariableDataShape getVariableDataShape() {
    return null;
  }

  @Override
  public String getValue(Map<String, String> row) {
    return null;
  }
}
