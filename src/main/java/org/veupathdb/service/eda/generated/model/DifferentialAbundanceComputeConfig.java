package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;

@JsonDeserialize(
    as = DifferentialAbundanceComputeConfigImpl.class
)
public interface DifferentialAbundanceComputeConfig {
  @JsonProperty("collectionVariable")
  CollectionSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(CollectionSpec collectionVariable);

  @JsonProperty("comparator")
  ComparatorSpec getComparator();

  @JsonProperty("comparator")
  void setComparator(ComparatorSpec comparator);

  @JsonProperty("differentialAbundanceMethod")
  DifferentialAbundanceMethod getDifferentialAbundanceMethod();

  @JsonProperty("differentialAbundanceMethod")
  void setDifferentialAbundanceMethod(DifferentialAbundanceMethod differentialAbundanceMethod);

  @JsonProperty("pValueFloor")
  String getPValueFloor();

  @JsonProperty("pValueFloor")
  void setPValueFloor(String pValueFloor);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
