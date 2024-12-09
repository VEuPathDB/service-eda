package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "xVariableDetails",
    "overlayVariableDetails",
    "facetVariableDetails",
    "size"
})
public class SampleSizeTableImpl implements SampleSizeTable {
  @JsonProperty("xVariableDetails")
  private List<StrataVariableDetails> xVariableDetails;

  @JsonProperty("overlayVariableDetails")
  private StrataVariableDetails overlayVariableDetails;

  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("size")
  private List<Number> size;

  @JsonProperty("xVariableDetails")
  public List<StrataVariableDetails> getXVariableDetails() {
    return this.xVariableDetails;
  }

  @JsonProperty("xVariableDetails")
  public void setXVariableDetails(List<StrataVariableDetails> xVariableDetails) {
    this.xVariableDetails = xVariableDetails;
  }

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

  @JsonProperty("size")
  public List<Number> getSize() {
    return this.size;
  }

  @JsonProperty("size")
  public void setSize(List<Number> size) {
    this.size = size;
  }
}
