package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "overlayVariableDetails",
    "facetVariableDetails",
    "value",
    "binStart",
    "binEnd",
    "binLabel"
})
public class HistogramDataImpl implements HistogramData {
  @JsonProperty("overlayVariableDetails")
  private StrataVariableDetails overlayVariableDetails;

  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("value")
  private List<Number> value;

  @JsonProperty("binStart")
  private List<String> binStart;

  @JsonProperty("binEnd")
  private List<String> binEnd;

  @JsonProperty("binLabel")
  private List<String> binLabel;

  @JsonProperty("overlayVariableDetails")
  public StrataVariableDetails getOverlayVariableDetails() {
    return this.overlayVariableDetails;
  }

  @JsonProperty("overlayVariableDetails")
  public void setOverlayVariableDetails(StrataVariableDetails overlayVariableDetails) {
    this.overlayVariableDetails = overlayVariableDetails;
  }

  @JsonProperty("facetVariableDetails")
  public List<StrataVariableDetails> getFacetVariableDetails() {
    return this.facetVariableDetails;
  }

  @JsonProperty("facetVariableDetails")
  public void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails) {
    this.facetVariableDetails = facetVariableDetails;
  }

  @JsonProperty("value")
  public List<Number> getValue() {
    return this.value;
  }

  @JsonProperty("value")
  public void setValue(List<Number> value) {
    this.value = value;
  }

  @JsonProperty("binStart")
  public List<String> getBinStart() {
    return this.binStart;
  }

  @JsonProperty("binStart")
  public void setBinStart(List<String> binStart) {
    this.binStart = binStart;
  }

  @JsonProperty("binEnd")
  public List<String> getBinEnd() {
    return this.binEnd;
  }

  @JsonProperty("binEnd")
  public void setBinEnd(List<String> binEnd) {
    this.binEnd = binEnd;
  }

  @JsonProperty("binLabel")
  public List<String> getBinLabel() {
    return this.binLabel;
  }

  @JsonProperty("binLabel")
  public void setBinLabel(List<String> binLabel) {
    this.binLabel = binLabel;
  }
}
