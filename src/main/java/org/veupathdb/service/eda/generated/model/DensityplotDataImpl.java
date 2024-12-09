package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "overlayVariableDetails",
    "facetVariableDetails",
    "densityY",
    "densityX"
})
public class DensityplotDataImpl implements DensityplotData {
  @JsonProperty("overlayVariableDetails")
  private StrataVariableDetails overlayVariableDetails;

  @JsonProperty("facetVariableDetails")
  private List<StrataVariableDetails> facetVariableDetails;

  @JsonProperty("densityY")
  private List<Number> densityY;

  @JsonProperty("densityX")
  private List<Number> densityX;

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

  @JsonProperty("densityY")
  public List<Number> getDensityY() {
    return this.densityY;
  }

  @JsonProperty("densityY")
  public void setDensityY(List<Number> densityY) {
    this.densityY = densityY;
  }

  @JsonProperty("densityX")
  public List<Number> getDensityX() {
    return this.densityX;
  }

  @JsonProperty("densityX")
  public void setDensityX(List<Number> densityX) {
    this.densityX = densityX;
  }
}
