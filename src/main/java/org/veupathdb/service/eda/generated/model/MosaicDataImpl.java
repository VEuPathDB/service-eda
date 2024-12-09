package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "facetVariableDetails",
    "xLabel",
    "yLabel",
    "value"
})
public class MosaicDataImpl implements MosaicData {
  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("xLabel")
  private List<String> xLabel;

  @JsonProperty("yLabel")
  private List<List<String>> yLabel;

  @JsonProperty("value")
  private List<List<Number>> value;

  @JsonProperty("facetVariableDetails")
  public List<StrataVariableDetails> getFacetVariableDetails() {
    return this.facetVariableDetails;
  }

  @JsonProperty("facetVariableDetails")
  public void setFacetVariableDetails(List<StrataVariableDetails> facetVariableDetails) {
    this.facetVariableDetails = facetVariableDetails;
  }

  @JsonProperty("xLabel")
  public List<String> getXLabel() {
    return this.xLabel;
  }

  @JsonProperty("xLabel")
  public void setXLabel(List<String> xLabel) {
    this.xLabel = xLabel;
  }

  @JsonProperty("yLabel")
  public List<List<String>> getYLabel() {
    return this.yLabel;
  }

  @JsonProperty("yLabel")
  public void setYLabel(List<List<String>> yLabel) {
    this.yLabel = yLabel;
  }

  @JsonProperty("value")
  public List<List<Number>> getValue() {
    return this.value;
  }

  @JsonProperty("value")
  public void setValue(List<List<Number>> value) {
    this.value = value;
  }
}
