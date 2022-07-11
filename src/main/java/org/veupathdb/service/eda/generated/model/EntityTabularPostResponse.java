package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = EntityTabularPostResponseImpl.class
)
public interface EntityTabularPostResponse {
  @JsonProperty("counts")
  List<List<String>> getCounts();

  @JsonProperty("counts")
  void setCounts(List<List<String>> counts);
}
