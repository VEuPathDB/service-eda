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
@JsonTypeName("integer")
@JsonPropertyOrder({
    "id",
    "displayName",
    "type",
    "dataShape",
    "vocabulary",
    "distinctValuesCount",
    "memberVariableIds",
    "imputeZero",
    "hasStudyDependentVocabulary",
    "variableSpecToImputeZeroesFor",
    "normalizationMethod",
    "isCompositional",
    "isProportion",
    "member",
    "memberPlural",
    "distributionDefaults",
    "units"
})
public class APIIntegerCollectionImpl implements APIIntegerCollection {
  @JsonProperty("id")
  private String id;

  @JsonProperty("displayName")
  private String displayName;

  @JsonProperty("type")
  private final APICollectionType type = _DISCRIMINATOR_TYPE_NAME;

  @JsonProperty("dataShape")
  private APIVariableDataShape dataShape;

  @JsonProperty("vocabulary")
  private List<String> vocabulary;

  @JsonProperty("distinctValuesCount")
  private Long distinctValuesCount;

  @JsonProperty("memberVariableIds")
  private List<String> memberVariableIds;

  @JsonProperty("imputeZero")
  private Boolean imputeZero;

  @JsonProperty("hasStudyDependentVocabulary")
  private Boolean hasStudyDependentVocabulary;

  @JsonProperty("variableSpecToImputeZeroesFor")
  private VariableSpec variableSpecToImputeZeroesFor;

  @JsonProperty("normalizationMethod")
  private String normalizationMethod;

  @JsonProperty("isCompositional")
  private Boolean isCompositional;

  @JsonProperty("isProportion")
  private Boolean isProportion;

  @JsonProperty("member")
  private String member;

  @JsonProperty("memberPlural")
  private String memberPlural;

  @JsonProperty("distributionDefaults")
  private APIIntegerDistributionDefaults distributionDefaults;

  @JsonProperty("units")
  private String units;

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

  @JsonProperty("displayName")
  public String getDisplayName() {
    return this.displayName;
  }

  @JsonProperty("displayName")
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("type")
  public APICollectionType getType() {
    return this.type;
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

  @JsonProperty("memberVariableIds")
  public List<String> getMemberVariableIds() {
    return this.memberVariableIds;
  }

  @JsonProperty("memberVariableIds")
  public void setMemberVariableIds(List<String> memberVariableIds) {
    this.memberVariableIds = memberVariableIds;
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

  @JsonProperty("normalizationMethod")
  public String getNormalizationMethod() {
    return this.normalizationMethod;
  }

  @JsonProperty("normalizationMethod")
  public void setNormalizationMethod(String normalizationMethod) {
    this.normalizationMethod = normalizationMethod;
  }

  @JsonProperty("isCompositional")
  public Boolean getIsCompositional() {
    return this.isCompositional;
  }

  @JsonProperty("isCompositional")
  public void setIsCompositional(Boolean isCompositional) {
    this.isCompositional = isCompositional;
  }

  @JsonProperty("isProportion")
  public Boolean getIsProportion() {
    return this.isProportion;
  }

  @JsonProperty("isProportion")
  public void setIsProportion(Boolean isProportion) {
    this.isProportion = isProportion;
  }

  @JsonProperty("member")
  public String getMember() {
    return this.member;
  }

  @JsonProperty("member")
  public void setMember(String member) {
    this.member = member;
  }

  @JsonProperty("memberPlural")
  public String getMemberPlural() {
    return this.memberPlural;
  }

  @JsonProperty("memberPlural")
  public void setMemberPlural(String memberPlural) {
    this.memberPlural = memberPlural;
  }

  @JsonProperty("distributionDefaults")
  public APIIntegerDistributionDefaults getDistributionDefaults() {
    return this.distributionDefaults;
  }

  @JsonProperty("distributionDefaults")
  public void setDistributionDefaults(APIIntegerDistributionDefaults distributionDefaults) {
    this.distributionDefaults = distributionDefaults;
  }

  @JsonProperty("units")
  public String getUnits() {
    return this.units;
  }

  @JsonProperty("units")
  public void setUnits(String units) {
    this.units = units;
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
