package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = CollectionFloatingBarplotSpecImpl.class
)
public interface CollectionFloatingBarplotSpec {
  @JsonProperty("outputEntityId")
  String getOutputEntityId();

  @JsonProperty("outputEntityId")
  void setOutputEntityId(String outputEntityId);

  @JsonProperty("barMode")
  BarModeType getBarMode();

  @JsonProperty("barMode")
  void setBarMode(BarModeType barMode);

  @JsonProperty("valueSpec")
  ValueSpec getValueSpec();

  @JsonProperty("valueSpec")
  void setValueSpec(ValueSpec valueSpec);

  @JsonProperty("overlayConfig")
  CollectionOverlayConfigWithValues getOverlayConfig();

  @JsonProperty("overlayConfig")
  void setOverlayConfig(CollectionOverlayConfigWithValues overlayConfig);

  enum BarModeType {
    @JsonProperty("group")
    GROUP("group"),

    @JsonProperty("stack")
    STACK("stack");

    public final String value;

    public String getValue() {
      return this.value;
    }

    BarModeType(String name) {
      this.value = name;
    }
  }
}
