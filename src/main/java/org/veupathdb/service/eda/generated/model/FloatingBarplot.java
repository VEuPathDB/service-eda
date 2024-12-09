package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = FloatingBarplotImpl.class
)
public interface FloatingBarplot {
  @JsonProperty("data")
  List<BarplotData> getData();

  @JsonProperty("data")
  void setData(List<BarplotData> data);

  @JsonProperty("variables")
  List<VariableMapping> getVariables();

  @JsonProperty("variables")
  void setVariables(List<VariableMapping> variables);
}
