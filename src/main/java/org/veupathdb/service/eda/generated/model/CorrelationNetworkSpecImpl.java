package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "significanceThreshold",
    "correlationCoefThreshold",
    "layout",
    "degree"
})
public class CorrelationNetworkSpecImpl implements CorrelationNetworkSpec {
  @JsonProperty("significanceThreshold")
  private Number significanceThreshold;

  @JsonProperty("correlationCoefThreshold")
  private Number correlationCoefThreshold;

  @JsonProperty("layout")
  private CorrelationNetworkSpec.LayoutType layout;

  @JsonProperty("degree")
  private Boolean degree;

  @JsonProperty("significanceThreshold")
  public Number getSignificanceThreshold() {
    return this.significanceThreshold;
  }

  @JsonProperty("significanceThreshold")
  public void setSignificanceThreshold(Number significanceThreshold) {
    this.significanceThreshold = significanceThreshold;
  }

  @JsonProperty("correlationCoefThreshold")
  public Number getCorrelationCoefThreshold() {
    return this.correlationCoefThreshold;
  }

  @JsonProperty("correlationCoefThreshold")
  public void setCorrelationCoefThreshold(Number correlationCoefThreshold) {
    this.correlationCoefThreshold = correlationCoefThreshold;
  }

  @JsonProperty("layout")
  public CorrelationNetworkSpec.LayoutType getLayout() {
    return this.layout;
  }

  @JsonProperty("layout")
  public void setLayout(CorrelationNetworkSpec.LayoutType layout) {
    this.layout = layout;
  }

  @JsonProperty("degree")
  public Boolean getDegree() {
    return this.degree;
  }

  @JsonProperty("degree")
  public void setDegree(Boolean degree) {
    this.degree = degree;
  }
}
