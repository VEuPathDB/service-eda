package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("mapElements")
public class StandaloneMapMarkersPostResponseImpl implements StandaloneMapMarkersPostResponse {
  @JsonProperty("mapElements")
  private List<StandaloneMapElementInfo> mapElements;

  @JsonProperty("mapElements")
  public List<StandaloneMapElementInfo> getMapElements() {
    return this.mapElements;
  }

  @JsonProperty("mapElements")
  public void setMapElements(List<StandaloneMapElementInfo> mapElements) {
    this.mapElements = mapElements;
  }
}
