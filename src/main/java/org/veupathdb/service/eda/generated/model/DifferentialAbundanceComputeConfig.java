package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = DifferentialAbundanceComputeConfigImpl.class
)
public interface DifferentialAbundanceComputeConfig {
  @JsonProperty("collectionVariable")
  VariableSpec getCollectionVariable();

  @JsonProperty("collectionVariable")
  void setCollectionVariable(VariableSpec collectionVariable);

  @JsonProperty("comparisonVariable")
  VariableSpec getComparisonVariable();

  @JsonProperty("comparisonVariable")
  void setComparisonVariable(VariableSpec comparisonVariable);

  @JsonProperty("differentialAbundanceGroupA")
  List<String> getDifferentialAbundanceGroupA();

  @JsonProperty("differentialAbundanceGroupA")
  void setDifferentialAbundanceGroupA(List<String> differentialAbundanceGroupA);

  @JsonProperty("differentialAbundanceGroupB")
  List<String> getDifferentialAbundanceGroupB();

  @JsonProperty("differentialAbundanceGroupB")
  void setDifferentialAbundanceGroupB(List<String> differentialAbundanceGroupB);

  @JsonProperty("differentialAbundanceMethod")
  DifferentialAbundanceMethod getDifferentialAbundanceMethod();

  @JsonProperty("differentialAbundanceMethod")
  void setDifferentialAbundanceMethod(DifferentialAbundanceMethod differentialAbundanceMethod);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
