package org.veupathdb.service.eda.common.derivedvars.plugin.transforms;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.derivedvars.plugin.Transform;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.RelatedObservationMinTimeIntervalConfig;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.List;
import java.util.Map;

/**
 * Related Observation Operations (Original Requirements)
 *
 * We implement this feature by allowing the user to create a Time to Next variable on repeated measures entities.
 *   Ror example, on the Participant Repeated Measure entity, create a variable that captures, for all
 *   observations of PCR positive, the time to the nearest observation of a high fever
 *
 * The user flow is:
 *   - choose a repeated measures entity (eg, Participant Repeated Measures)
 *   - choose a boolean variable to identify anchor entities
 *     - Remind the user that they can create a membership variable if no existing boolean var is sufficient
 *     - the ‘yes’ guys are the entities on which we will provide values for this derived variables.  the ‘no’ guys get null values.
 *   - similar for identifying a target variable
 *   - the user chooses a ‘time’ variable
 *   - optionally specifies a minimal interval to consider
 *
 * The computation:
 *   - for each anchor entity, we discover its parent entity
 *   - we scan all target RMs belonging to that parent
 *   - identify the one that has the nearest time variable value to the anchor (above the minimum interval, if specified)
 *   - assign the discovered time interval value as the derived variable value
 *
 * An advanced version of this feature (possibly in a follow up phase) is to allow anchor and target to be in different entities.  For example find the time between installation of bed nets and reduction of mosquito bites.
 */
public class RelativeObservationMinTimeInterval extends Transform<RelatedObservationMinTimeIntervalConfig> {

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
    return "relativeObservationMinTimeInterval";
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
