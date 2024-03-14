package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIStringVariable.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIDateVariable.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIIntegerVariable.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APILongitudeVariable.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APINumberVariable.class),
    @JsonSubTypes.Type(org.veupathdb.service.eda.generated.model.APIVariableWithValues.class)
})
@JsonDeserialize(
    as = APIVariableWithValuesImpl.class
)
public interface APIVariableWithValues extends APIVariable {
  APIVariableType _DISCRIMINATOR_TYPE_NAME = null;

  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  void setId(String id);

  @JsonProperty("parentId")
  String getParentId();

  @JsonProperty("parentId")
  void setParentId(String parentId);

  @JsonProperty("providerLabel")
  String getProviderLabel();

  @JsonProperty("providerLabel")
  void setProviderLabel(String providerLabel);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("definition")
  String getDefinition();

  @JsonProperty("definition")
  void setDefinition(String definition);

  @JsonProperty("displayType")
  APIVariableDisplayType getDisplayType();

  @JsonProperty("displayType")
  void setDisplayType(APIVariableDisplayType displayType);

  @JsonProperty("displayOrder")
  Long getDisplayOrder();

  @JsonProperty("displayOrder")
  void setDisplayOrder(Long displayOrder);

  @JsonProperty("isCategory")
  String getIsCategory();

  @JsonProperty("isCategory")
  void setIsCategory(String isCategory);

  @JsonProperty("type")
  APIVariableType getType();

  @JsonProperty("hideFrom")
  List<String> getHideFrom();

  @JsonProperty("hideFrom")
  void setHideFrom(List<String> hideFrom);

  @JsonProperty("dataShape")
  APIVariableDataShape getDataShape();

  @JsonProperty("dataShape")
  void setDataShape(APIVariableDataShape dataShape);

  @JsonProperty("vocabulary")
  List<String> getVocabulary();

  @JsonProperty("vocabulary")
  void setVocabulary(List<String> vocabulary);

  @JsonProperty("distinctValuesCount")
  Long getDistinctValuesCount();

  @JsonProperty("distinctValuesCount")
  void setDistinctValuesCount(Long distinctValuesCount);

  @JsonProperty("isTemporal")
  Boolean getIsTemporal();

  @JsonProperty("isTemporal")
  void setIsTemporal(Boolean isTemporal);

  @JsonProperty("isFeatured")
  Boolean getIsFeatured();

  @JsonProperty("isFeatured")
  void setIsFeatured(Boolean isFeatured);

  @JsonProperty("isMergeKey")
  Boolean getIsMergeKey();

  @JsonProperty("isMergeKey")
  void setIsMergeKey(Boolean isMergeKey);

  @JsonProperty("isMultiValued")
  Boolean getIsMultiValued();

  @JsonProperty("isMultiValued")
  void setIsMultiValued(Boolean isMultiValued);

  @JsonProperty("imputeZero")
  Boolean getImputeZero();

  @JsonProperty("imputeZero")
  void setImputeZero(Boolean imputeZero);

  @JsonProperty("hasStudyDependentVocabulary")
  Boolean getHasStudyDependentVocabulary();

  @JsonProperty("hasStudyDependentVocabulary")
  void setHasStudyDependentVocabulary(Boolean hasStudyDependentVocabulary);

  @JsonProperty("variableSpecToImputeZeroesFor")
  VariableSpec getVariableSpecToImputeZeroesFor();

  @JsonProperty("variableSpecToImputeZeroesFor")
  void setVariableSpecToImputeZeroesFor(VariableSpec variableSpecToImputeZeroesFor);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
