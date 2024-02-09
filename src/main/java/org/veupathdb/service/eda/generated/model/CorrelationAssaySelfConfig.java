package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = CorrelationAssaySelfConfigImpl.class
)
public interface CorrelationAssaySelfConfig {
  @JsonProperty("correlationMethod")
  CorrelationMethodType getCorrelationMethod();

  @JsonProperty("correlationMethod")
  void setCorrelationMethod(CorrelationMethodType correlationMethod);

  @JsonProperty("prefilterThresholds")
  FeaturePrefilterThresholds getPrefilterThresholds();

  @JsonProperty("prefilterThresholds")
  void setPrefilterThresholds(FeaturePrefilterThresholds prefilterThresholds);

  @JsonProperty("collectionVariable")
  CollectionSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(CollectionSpec collectionVariable);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);

  enum CorrelationMethodType {
    @JsonProperty("spearman")
    SPEARMAN("spearman"),

    @JsonProperty("pearson")
    PEARSON("pearson"),

    @JsonProperty("sparcc")
    SPARCC("sparcc");

    public final String value;

    public String getValue() {
      return this.value;
    }

    CorrelationMethodType(String name) {
      this.value = name;
    }
  }
}
