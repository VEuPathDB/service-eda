package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = CorrelationNetworkSpecImpl.class
)
public interface CorrelationNetworkSpec {
  @JsonProperty("significanceThreshold")
  Number getSignificanceThreshold();

  @JsonProperty("significanceThreshold")
  void setSignificanceThreshold(Number significanceThreshold);

  @JsonProperty("correlationCoefThreshold")
  Number getCorrelationCoefThreshold();

  @JsonProperty("correlationCoefThreshold")
  void setCorrelationCoefThreshold(Number correlationCoefThreshold);

  @JsonProperty("layout")
  LayoutType getLayout();

  @JsonProperty("layout")
  void setLayout(LayoutType layout);

  @JsonProperty("degree")
  Boolean getDegree();

  @JsonProperty("degree")
  void setDegree(Boolean degree);

  enum LayoutType {
    @JsonProperty("none")
    NONE("none"),

    @JsonProperty("force")
    FORCE("force"),

    @JsonProperty("circle")
    CIRCLE("circle"),

    @JsonProperty("nicely")
    NICELY("nicely");

    public final String value;

    public String getValue() {
      return this.value;
    }

    LayoutType(String name) {
      this.value = name;
    }
  }
}
