package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(
    as = FloatingHistogramImpl.class
)
public interface FloatingHistogram {
  @JsonProperty("data")
  List<HistogramData> getData();

  @JsonProperty("data")
  void setData(List<HistogramData> data);

  @JsonProperty("variables")
  List<VariableMapping> getVariables();

  @JsonProperty("variables")
  void setVariables(List<VariableMapping> variables);
}
