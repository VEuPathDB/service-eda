package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = BinRangeImpl.class
)
public interface BinRange {
  @JsonProperty("binStart")
  String getBinStart();

  @JsonProperty("binStart")
  void setBinStart(String binStart);

  @JsonProperty("binEnd")
  String getBinEnd();

  @JsonProperty("binEnd")
  void setBinEnd(String binEnd);

  @JsonProperty("binLabel")
  String getBinLabel();

  @JsonProperty("binLabel")
  void setBinLabel(String binLabel);
}
