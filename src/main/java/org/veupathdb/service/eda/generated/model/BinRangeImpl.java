package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "binStart",
    "binEnd",
    "binLabel"
})
public class BinRangeImpl implements BinRange {
  @JsonProperty("binStart")
  private String binStart;

  @JsonProperty("binEnd")
  private String binEnd;

  @JsonProperty("binLabel")
  private String binLabel;

  @JsonProperty("binStart")
  public String getBinStart() {
    return this.binStart;
  }

  @JsonProperty("binStart")
  public void setBinStart(String binStart) {
    this.binStart = binStart;
  }

  @JsonProperty("binEnd")
  public String getBinEnd() {
    return this.binEnd;
  }

  @JsonProperty("binEnd")
  public void setBinEnd(String binEnd) {
    this.binEnd = binEnd;
  }

  @JsonProperty("binLabel")
  public String getBinLabel() {
    return this.binLabel;
  }

  @JsonProperty("binLabel")
  public void setBinLabel(String binLabel) {
    this.binLabel = binLabel;
  }
}
