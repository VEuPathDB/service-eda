package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = CategoricalDistributionPostResponseImpl.class
)
public interface CategoricalDistributionPostResponse {
  @JsonProperty("countDistribution")
  List<CategoricalDistributionBin> getCountDistribution();

  @JsonProperty("countDistribution")
  void setCountDistribution(List<CategoricalDistributionBin> countDistribution);

  @JsonProperty("proportionDistribution")
  List<CategoricalDistributionBin> getProportionDistribution();

  @JsonProperty("proportionDistribution")
  void setProportionDistribution(List<CategoricalDistributionBin> proportionDistribution);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
