package org.veupathdb.service.eda.common.derivedvars.plugin.reductions;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.derivedvars.plugin.Reduction;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.RelatedObservationMinTimeIntervalConfig;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.List;

public class RelativeObservationMinTimeIntervalHelper extends Reduction<RelatedObservationMinTimeIntervalConfig> {

  public static final String FUNCTION_NAME = "relativeObservationMinTimeIntervalHelper";

  @Override
  protected Class<RelatedObservationMinTimeIntervalConfig> getConfigClass() {
    return RelatedObservationMinTimeIntervalConfig.class;
  }

  @Override
  protected void acceptConfig(RelatedObservationMinTimeIntervalConfig config) throws ValidationException {

  }

  @Override
  protected void performSupplementalDependedVariableValidation() throws ValidationException {

  }

  @Override
  public String getFunctionName() {
    return FUNCTION_NAME;
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
  public Reducer createReducer() {
    return null;
  }
}
