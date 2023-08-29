package org.veupathdb.service.eda.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.veupathdb.service.eda.generated.model.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CollectionDef extends CollectionSpecImpl {

  @JsonIgnore
  private final EntityDef _entity;

  @JsonIgnore
  private final String _displayName;

  @JsonIgnore
  private final APICollectionType _dataType;

  @JsonIgnore
  private final APIVariableDataShape _dataShape;

  @JsonIgnore
  private final boolean _isImputeZero;

  @JsonIgnore
  private final Long _distinctValuesCount;

  @JsonIgnore
  private final List<String> _vocabulary;

  @JsonIgnore
  private final List<String> _memberVariableIds;

  @JsonIgnore
  private final Optional<DataRanges> _dataRanges;

  @JsonIgnore
  private final boolean _hasStudyDependentVocabulary;

  @JsonIgnore
  private final VariableSpec _variableSpecToImputeZeroesFor;

  public CollectionDef(
      EntityDef entity,
      String id,
      String displayName,
      APICollectionType dataType,
      APIVariableDataShape dataShape,
      boolean isImputeZero,
      Long distinctValuesCount,
      List<String> vocabulary,
      List<String> memberVariableIds,
      Optional<DataRanges> dataRanges,
      boolean hasStudyDependentVocabulary,
      VariableSpec variableSpecToImputeZeroesFor) {
    setEntityId(entity.getId());
    setCollectionId(id);
    _entity = entity;
    _displayName = displayName;
    _dataType = dataType;
    _dataShape = dataShape;
    _isImputeZero = isImputeZero;
    _distinctValuesCount = distinctValuesCount;
    _vocabulary = vocabulary;
    _memberVariableIds = memberVariableIds;
    _dataRanges = dataRanges;
    _hasStudyDependentVocabulary = hasStudyDependentVocabulary;
    _variableSpecToImputeZeroesFor = variableSpecToImputeZeroesFor;
  }

  @JsonIgnore
  public String getDisplayName() {
    return _displayName;
  }

  @JsonIgnore
  public APICollectionType getDataType() {
    return _dataType;
  }

  @JsonIgnore
  public APIVariableDataShape getDataShape() {
    return _dataShape;
  }

  @JsonIgnore
  public boolean isImputeZero() {
    return _isImputeZero;
  }

  @JsonIgnore
  public Long getDistinctValuesCount() {
    return _distinctValuesCount;
  }

  @JsonIgnore
  public List<String> getVocabulary() {
    return _vocabulary;
  }

  @JsonIgnore
  public Optional<DataRanges> getDataRanges() {
    return _dataRanges;
  }

  @JsonIgnore
  public boolean getHasStudyDependentVocabulary() {
    return _hasStudyDependentVocabulary;
  }

  @JsonIgnore
  public VariableSpec getVariableSpecToImputeZeroesFor() {
    return _variableSpecToImputeZeroesFor;
  }

  @JsonIgnore
  public List<VariableDef> getMemberVariables() {
    return _memberVariableIds.stream()
        .map(id -> _entity.getVariable(VariableDef.newVariableSpec(getEntityId(), id))
            .orElseThrow(() -> new RuntimeException("Collection " + getCollectionId() + " in entity " +
                _entity.getId() + " has variable " + id + " which does not belong to " + _entity.getId())))
        .collect(Collectors.toList());
  }

  public static boolean isSameCollection(CollectionSpec c1, CollectionSpec c2) {
    return c1.getEntityId().equals(c2.getEntityId()) && c1.getCollectionId().equals(c2.getCollectionId());
  }
}
