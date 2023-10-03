package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonTypeName("number")
@JsonDeserialize(
    as = APINumberCollectionImpl.class
)
public interface APINumberCollection extends APICollection {
  APICollectionType _DISCRIMINATOR_TYPE_NAME = APICollectionType.NUMBER;

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

  @JsonProperty("distributionDefaults")
  APINumberDistributionDefaults getDistributionDefaults();

  @JsonProperty("distributionDefaults")
  void setDistributionDefaults(APINumberDistributionDefaults distributionDefaults);

  @JsonProperty("units")
  String getUnits();

  @JsonProperty("units")
  void setUnits(String units);

  @JsonProperty("precision")
  Number getPrecision();

  @JsonProperty("precision")
  void setPrecision(Number precision);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
