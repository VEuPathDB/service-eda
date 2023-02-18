package org.veupathdb.service.eda.common.derivedvars.plugin;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.common.model.DataRanges;
import org.veupathdb.service.eda.common.model.EntityDef;
import org.veupathdb.service.eda.common.model.ReferenceMetadata;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec;
import org.veupathdb.service.eda.generated.model.VariableSpec;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DerivedVariable extends VariableSpec {

  /**
   * @return name of this derived variable plugin; this is the
   * name used by clients to identify the desired plugin
   */
  String getFunctionName();

  /**
   * Initializes this derived variable instance and assigns and validates config
   *
   * @param metadata metadata for the appropriate study
   * @param spec configuration for this derived variable instance
   * @return this object
   */
  void init(ReferenceMetadata metadata, DerivedVariableSpec spec) throws ValidationException;

  /**
   * @return entity of this derived variable
   */
  EntityDef getEntity();

  /**
   * @return column name for this derived variable (dot notation)
   */
  String getColumnName();

  /**
   * @return all required input vars for this derived variable
   */
  List<VariableSpec> getRequiredInputVars();

  /**
   * @return required column names for this derived variable
   */
  List<String> getRequiredInputColumnNames();

  /**
   * Validate that the variable specs this derived variable
   * depends on exist and are available on a permitted entity
   */
  void validateDependedVariables();

  /**
   * @return any additional derived variables that need to be
   * generated to support this derived var
   */
  List<DerivedVariableSpec> getDependedDerivedVarSpecs();

  /**
   * @return the type of the variable this instance will return
   */
  APIVariableType getVariableType();

  /**
   * @return the shape of the variable this instance will return
   */
  APIVariableDataShape getVariableDataShape();

  /**
   * @return the vocabulary of the variable this instance will return
   */
  default Optional<List<String>> getVocabulary() {
    return Optional.empty();
  }

  /**
   * @return the data ranges of the variable this instance will return
   */
  default Optional<DataRanges> getDataRanges() {
    return Optional.empty();
  }

  default boolean allRequiredColsPresent(Map<String, String> row) {
    Collection<String> availableCols = row.keySet();
    for (String requiredCol : getRequiredInputColumnNames()) {
      if (!availableCols.contains(requiredCol)) {
        return false;
      }
    }
    return true;
  }
}
