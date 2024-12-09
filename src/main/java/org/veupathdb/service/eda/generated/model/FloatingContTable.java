package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = FloatingContTableImpl.class
)
public interface FloatingContTable {
  @JsonProperty("data")
  List<MosaicData> getData();

  @JsonProperty("data")
  void setData(List<MosaicData> data);

  @JsonProperty("variables")
  List<VariableMapping> getVariables();

  @JsonProperty("variables")
  void setVariables(List<VariableMapping> variables);
}
