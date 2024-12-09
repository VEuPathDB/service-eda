package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = FloatingLineplotImpl.class
)
public interface FloatingLineplot {
  @JsonProperty("data")
  List<LineplotData> getData();

  @JsonProperty("data")
  void setData(List<LineplotData> data);

  @JsonProperty("variables")
  List<VariableMapping> getVariables();

  @JsonProperty("variables")
  void setVariables(List<VariableMapping> variables);
}
