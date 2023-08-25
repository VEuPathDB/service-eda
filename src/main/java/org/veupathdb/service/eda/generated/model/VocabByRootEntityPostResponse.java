package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = VocabByRootEntityPostResponseImpl.class
)
public interface VocabByRootEntityPostResponse extends EntityTabularPostResponse {
  @JsonProperty("tabular")
  List<List<String>> getTabular();

  @JsonProperty("tabular")
  void setTabular(List<List<String>> tabular);
}
