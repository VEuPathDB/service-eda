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
@JsonPropertyOrder("pcoaVariance")
public class BetaDivStatsImpl implements BetaDivStats {
  @JsonProperty("pcoaVariance")
  private List<Number> pcoaVariance;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("pcoaVariance")
  public List<Number> getPcoaVariance() {
    return this.pcoaVariance;
  }

  @JsonProperty("pcoaVariance")
  public void setPcoaVariance(List<Number> pcoaVariance) {
    this.pcoaVariance = pcoaVariance;
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
