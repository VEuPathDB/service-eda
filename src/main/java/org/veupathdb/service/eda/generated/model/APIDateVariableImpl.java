package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("date")
@JsonPropertyOrder({
    "id",
    "parentId",
    "providerLabel",
    "displayName",
    "definition",
    "displayType",
    "displayOrder",
    "isCategory",
    "type",
    "hideFrom",
    "dataShape",
    "vocabulary",
    "distinctValuesCount",
    "isTemporal",
    "isFeatured",
    "isMergeKey",
    "isMultiValued",
    "imputeZero",
    "hasStudyDependentVocabulary",
    "variableSpecToImputeZeroesFor",
    "distributionDefaults"
})
public class APIDateVariableImpl implements APIDateVariable {
  @JsonProperty("id")
  private String id;

  @JsonProperty("parentId")
  private String parentId;

  @JsonProperty("providerLabel")
  private String providerLabel;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("definition")
  private String definition;

  @JsonProperty("displayType")
  private APIVariableDisplayType displayType;

  @JsonProperty("displayOrder")
  private Long displayOrder;

  @JsonProperty("isCategory")
  private String isCategory;

  @JsonProperty("type")
  private final APIVariableType type = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("hideFrom")
  private List<String> hideFrom;

  @JsonProperty("dataShape")
  private APIVariableDataShape dataShape;

  @JsonProperty("vocabulary")
  private List<String> vocabulary;

  @JsonProperty("distinctValuesCount")
  private Long distinctValuesCount;

  @JsonProperty("isTemporal")
  private Boolean isTemporal;

  @JsonProperty("isFeatured")
  private Boolean isFeatured;

  @JsonProperty("isMergeKey")
  private Boolean isMergeKey;

  @JsonProperty("isMultiValued")
  private Boolean isMultiValued;

  @JsonProperty("imputeZero")
  private Boolean imputeZero;

  @JsonProperty("hasStudyDependentVocabulary")
  private Boolean hasStudyDependentVocabulary;

  @JsonProperty("variableSpecToImputeZeroesFor")
  private VariableSpec variableSpecToImputeZeroesFor;

  @JsonProperty("distributionDefaults")
  private APIDateDistributionDefaults distributionDefaults;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("id")
  public String getId() {
    return this.id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("parentId")
  public String getParentId() {
    return this.parentId;
  }

  @JsonProperty("parentId")
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  @JsonProperty("providerLabel")
  public String getProviderLabel() {
    return this.providerLabel;
  }

  @JsonProperty("providerLabel")
  public void setProviderLabel(String providerLabel) {
    this.providerLabel = providerLabel;
  }

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("definition")
  public String getDefinition() {
    return this.definition;
  }

  @JsonProperty("definition")
  public void setDefinition(String definition) {
    this.definition = definition;
  }

  @JsonProperty("displayType")
  public APIVariableDisplayType getDisplayType() {
    return this.displayType;
  }

  @JsonProperty("displayType")
  public void setDisplayType(APIVariableDisplayType displayType) {
    this.displayType = displayType;
  }

  @JsonProperty("displayOrder")
  public Long getDisplayOrder() {
    return this.displayOrder;
  }

  @JsonProperty("displayOrder")
  public void setDisplayOrder(Long displayOrder) {
    this.displayOrder = displayOrder;
  }

  @JsonProperty("isCategory")
  public String getIsCategory() {
    return this.isCategory;
  }

  @JsonProperty("isCategory")
  public void setIsCategory(String isCategory) {
    this.isCategory = isCategory;
  }

  @JsonProperty("type")
  public APIVariableType getType() {
    return this.type;
  }

  @JsonProperty("hideFrom")
  public List<String> getHideFrom() {
    return this.hideFrom;
  }

  @JsonProperty("hideFrom")
  public void setHideFrom(List<String> hideFrom) {
    this.hideFrom = hideFrom;
  }

  @JsonProperty("dataShape")
  public APIVariableDataShape getDataShape() {
    return this.dataShape;
  }

  @JsonProperty("dataShape")
  public void setDataShape(APIVariableDataShape dataShape) {
    this.dataShape = dataShape;
  }

  @JsonProperty("vocabulary")
  public List<String> getVocabulary() {
    return this.vocabulary;
  }

  @JsonProperty("vocabulary")
  public void setVocabulary(List<String> vocabulary) {
    this.vocabulary = vocabulary;
  }

  @JsonProperty("distinctValuesCount")
  public Long getDistinctValuesCount() {
    return this.distinctValuesCount;
  }

  @JsonProperty("distinctValuesCount")
  public void setDistinctValuesCount(Long distinctValuesCount) {
    this.distinctValuesCount = distinctValuesCount;
  }

  @JsonProperty("isTemporal")
  public Boolean getIsTemporal() {
    return this.isTemporal;
  }

  @JsonProperty("isTemporal")
  public void setIsTemporal(Boolean isTemporal) {
    this.isTemporal = isTemporal;
  }

  @JsonProperty("isFeatured")
  public Boolean getIsFeatured() {
    return this.isFeatured;
  }

  @JsonProperty("isFeatured")
  public void setIsFeatured(Boolean isFeatured) {
    this.isFeatured = isFeatured;
  }

  @JsonProperty("isMergeKey")
  public Boolean getIsMergeKey() {
    return this.isMergeKey;
  }

  @JsonProperty("isMergeKey")
  public void setIsMergeKey(Boolean isMergeKey) {
    this.isMergeKey = isMergeKey;
  }

  @JsonProperty("isMultiValued")
  public Boolean getIsMultiValued() {
    return this.isMultiValued;
  }

  @JsonProperty("isMultiValued")
  public void setIsMultiValued(Boolean isMultiValued) {
    this.isMultiValued = isMultiValued;
  }

  @JsonProperty("imputeZero")
  public Boolean getImputeZero() {
    return this.imputeZero;
  }

  @JsonProperty("imputeZero")
  public void setImputeZero(Boolean imputeZero) {
    this.imputeZero = imputeZero;
  }

  @JsonProperty("hasStudyDependentVocabulary")
  public Boolean getHasStudyDependentVocabulary() {
    return this.hasStudyDependentVocabulary;
  }

  @JsonProperty("hasStudyDependentVocabulary")
  public void setHasStudyDependentVocabulary(Boolean hasStudyDependentVocabulary) {
    this.hasStudyDependentVocabulary = hasStudyDependentVocabulary;
  }

  @JsonProperty("variableSpecToImputeZeroesFor")
  public VariableSpec getVariableSpecToImputeZeroesFor() {
    return this.variableSpecToImputeZeroesFor;
  }

  @JsonProperty("variableSpecToImputeZeroesFor")
  public void setVariableSpecToImputeZeroesFor(VariableSpec variableSpecToImputeZeroesFor) {
    this.variableSpecToImputeZeroesFor = variableSpecToImputeZeroesFor;
  }

  @JsonProperty("distributionDefaults")
  public APIDateDistributionDefaults getDistributionDefaults() {
    return this.distributionDefaults;
  }

  @JsonProperty("distributionDefaults")
  public void setDistributionDefaults(APIDateDistributionDefaults distributionDefaults) {
    this.distributionDefaults = distributionDefaults;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
