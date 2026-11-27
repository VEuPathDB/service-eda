package org.veupathdb.service.eda.merge.plugins.transforms;

import org.gusdb.fgputil.validation.ValidationException;
import org.veupathdb.service.eda.generated.model.APIVariableDataShape;
import org.veupathdb.service.eda.generated.model.APIVariableType;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpec;
import org.veupathdb.service.eda.generated.model.DerivedVariableSpecImpl;
import org.veupathdb.service.eda.generated.model.FilterDefinedGroup;
import org.veupathdb.service.eda.generated.model.FilterDefinedGroupsConfig;
import org.veupathdb.service.eda.generated.model.VariableSpec;
import org.veupathdb.service.eda.common.model.VariableDef;
import org.veupathdb.service.eda.merge.core.derivedvars.Transform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Produces a categorical variable from an ordered list of filter-defined
 * groups: each row gets the label of the first group whose filters it matches.
 * Each group is backed by a subsetMembership helper derived variable.
 */
public class FilterDefinedGroups extends Transform<FilterDefinedGroupsConfig> {

  public static final String FUNCTION_NAME = "filterDefinedGroups";
  private static final String MEMBERSHIP_FUNCTION_NAME = "subsetMembership";

  private List<String> _labels;
  private String _unmappedValue;
  private List<DerivedVariableSpec> _helperSpecs;
  private List<String> _helperColumns;

  @Override
  protected Class<FilterDefinedGroupsConfig> getConfigClass() {
    return FilterDefinedGroupsConfig.class;
  }

  @Override
  protected void acceptConfig(FilterDefinedGroupsConfig config) throws ValidationException {
    List<FilterDefinedGroup> groups = config.getGroups();
    if (groups == null || groups.isEmpty()) {
      throw new ValidationException("At least one group is required");
    }
    Set<String> labels = new HashSet<>();
    for (FilterDefinedGroup group : groups) {
      String label = group.getLabel();
      if (label == null || label.isEmpty()) {
        throw new ValidationException("Group labels must be non-empty");
      }
      if (!labels.add(label)) {
        throw new ValidationException("Group labels must be unique");
      }
      if (group.getFilters() == null || group.getFilters().isEmpty()) {
        throw new ValidationException("Group '" + label + "' has no filters");
      }
    }
    String unmappedValue = config.getUnmappedValue();
    if (unmappedValue != null && labels.contains(unmappedValue)) {
      throw new ValidationException("unmappedValue must differ from every group label");
    }
    _unmappedValue = unmappedValue == null || unmappedValue.isEmpty() ? EMPTY_VALUE : unmappedValue;

    _labels = new ArrayList<>();
    _helperSpecs = new ArrayList<>();
    _helperColumns = new ArrayList<>();
    int n = 1;
    for (FilterDefinedGroup group : groups) {
      DerivedVariableSpec helper = new DerivedVariableSpecImpl();
      helper.setEntityId(getEntityId());
      helper.setVariableId(getVariableId() + "_group" + n++);
      helper.setFunctionName(MEMBERSHIP_FUNCTION_NAME);
      helper.setDisplayName(group.getLabel());
      helper.setConfig(Map.of("subsetFilters", group.getFilters()));
      _labels.add(group.getLabel());
      _helperSpecs.add(helper);
      _helperColumns.add(VariableDef.toDotNotation(helper));
    }
  }

  @Override
  protected void performSupplementalDependedVariableValidation() {
    // nothing to do here
  }

  @Override
  public String getFunctionName() {
    return FUNCTION_NAME;
  }

  @Override
  public List<DerivedVariableSpec> getDependedDerivedVarSpecs() {
    return _helperSpecs;
  }

  @Override
  public List<VariableSpec> getRequiredInputVars() {
    return new ArrayList<>(_helperSpecs);
  }

  @Override
  public APIVariableType getVariableType() {
    return APIVariableType.STRING;
  }

  @Override
  public APIVariableDataShape getDataShape() {
    return APIVariableDataShape.CATEGORICAL;
  }

  @Override
  public List<String> getVocabulary() {
    List<String> vocab = new ArrayList<>(_labels);
    if (!_unmappedValue.isEmpty()) {
      vocab.add(_unmappedValue);
    }
    return vocab;
  }

  @Override
  public String getValue(Map<String, String> row) {
    for (int i = 0; i < _helperColumns.size(); i++) {
      if ("1".equals(row.get(_helperColumns.get(i)))) {
        return _labels.get(i);
      }
    }
    return _unmappedValue;
  }
}
