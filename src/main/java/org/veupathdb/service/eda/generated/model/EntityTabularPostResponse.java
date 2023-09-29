package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = EntityTabularPostResponseImpl.class
)
public interface EntityTabularPostResponse {
  @JsonProperty("tabular")
  List<List<String>> getTabular();

  @JsonProperty("tabular")
  void setTabular(List<List<String>> tabular);
}
