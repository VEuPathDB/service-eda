package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonTypeName("date")
@JsonDeserialize(
    as = APIDateCollectionImpl.class
)
public interface APIDateCollection extends APICollection {
  APICollectionType _DISCRIMINATOR_TYPE_NAME = APICollectionType.DATE;

  @JsonProperty("id")
  String getId();

  @JsonProperty("id")
  void setId(String id);

  @JsonProperty("displayName")
  String getDisplayName();

  @JsonProperty("displayName")
  void setDisplayName(String displayName);

  @JsonProperty("type")
  APICollectionType getType();

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

  @JsonProperty("memberVariableIds")
  List<String> getMemberVariableIds();

  @JsonProperty("memberVariableIds")
  void setMemberVariableIds(List<String> memberVariableIds);

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

  @JsonProperty("normalizationMethod")
  String getNormalizationMethod();

  @JsonProperty("normalizationMethod")
  void setNormalizationMethod(String normalizationMethod);

  @JsonProperty("isCompositional")
  Boolean getIsCompositional();

  @JsonProperty("isCompositional")
  void setIsCompositional(Boolean isCompositional);

  @JsonProperty("isProportion")
  Boolean getIsProportion();

  @JsonProperty("isProportion")
  void setIsProportion(Boolean isProportion);

  @JsonProperty("member")
  String getMember();

  @JsonProperty("member")
  void setMember(String member);

  @JsonProperty("memberPlural")
  String getMemberPlural();

  @JsonProperty("memberPlural")
  void setMemberPlural(String memberPlural);

  @JsonProperty("distributionDefaults")
  APIDateDistributionDefaults getDistributionDefaults();

  @JsonProperty("distributionDefaults")
  void setDistributionDefaults(APIDateDistributionDefaults distributionDefaults);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
