package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "countDistribution",
    "proportionDistribution"
})
public class CategoricalDistributionPostResponseImpl implements CategoricalDistributionPostResponse {
  @JsonProperty("countDistribution")
  private List<CategoricalDistributionBin> countDistribution;

  @JsonProperty("proportionDistribution")
  private List<CategoricalDistributionBin> proportionDistribution;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("countDistribution")
  public List<CategoricalDistributionBin> getCountDistribution() {
    return this.countDistribution;
  }

  @JsonProperty("countDistribution")
  public void setCountDistribution(List<CategoricalDistributionBin> countDistribution) {
    this.countDistribution = countDistribution;
  }

  @JsonProperty("proportionDistribution")
  public List<CategoricalDistributionBin> getProportionDistribution() {
    return this.proportionDistribution;
  }

  @JsonProperty("proportionDistribution")
  public void setProportionDistribution(List<CategoricalDistributionBin> proportionDistribution) {
    this.proportionDistribution = proportionDistribution;
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
