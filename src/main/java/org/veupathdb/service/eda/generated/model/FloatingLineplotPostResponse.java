package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = FloatingLineplotPostResponseImpl.class
)
public interface FloatingLineplotPostResponse {
  @JsonProperty("lineplot")
  FloatingLineplot getLineplot();

  @JsonProperty("lineplot")
  void setLineplot(FloatingLineplot lineplot);
}
