package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("mapElements")
public class StandaloneMapBubblesPostResponseImpl implements StandaloneMapBubblesPostResponse {
  @JsonProperty("mapElements")
  private List<ColoredMapElementInfo> mapElements;

  @JsonProperty("mapElements")
  public List<ColoredMapElementInfo> getMapElements() {
    return this.mapElements;
  }

  @JsonProperty("mapElements")
  public void setMapElements(List<ColoredMapElementInfo> mapElements) {
    this.mapElements = mapElements;
  }
}
