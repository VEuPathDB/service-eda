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
    "label"
})
public class BarplotDataImpl implements BarplotData {
  @JsonProperty("overlayVariableDetails")
  private StrataVariableDetails overlayVariableDetails;

  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("value")
  private List<Number> value;

  @JsonProperty("label")
  private List<String> label;

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

  @JsonProperty("label")
  public List<String> getLabel() {
    return this.label;
  }

  @JsonProperty("label")
  public void setLabel(List<String> label) {
    this.label = label;
  }
}