package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import java.util.Map;

@JsonDeserialize(
    as = AnalysisDescriptorImpl.class
)
public interface AnalysisDescriptor {
  @JsonProperty("subset")
  SubsetType getSubset();

  @JsonProperty("subset")
  void setSubset(SubsetType subset);

  @JsonProperty("computations")
  List<Computation> getComputations();

  @JsonProperty("computations")
  void setComputations(List<Computation> computations);

  @JsonProperty("starredVariables")
  List<VariableSpec> getStarredVariables();

  @JsonProperty("starredVariables")
  void setStarredVariables(List<VariableSpec> starredVariables);

  @JsonProperty("dataTableConfig")
  DataTableConfigSet getDataTableConfig();

  @JsonProperty("dataTableConfig")
  void setDataTableConfig(DataTableConfigSet dataTableConfig);

  @JsonProperty("derivedVariables")
  List<String> getDerivedVariables();

  @JsonProperty("derivedVariables")
  void setDerivedVariables(List<String> derivedVariables);

  @JsonDeserialize(
      as = AnalysisDescriptorImpl.SubsetTypeImpl.class
  )
  interface SubsetType {
    @JsonProperty("descriptor")
    List<Object> getDescriptor();

    @JsonProperty("descriptor")
    void setDescriptor(List<Object> descriptor);

    @JsonProperty("uiSettings")
    Object getUiSettings();

    @JsonProperty("uiSettings")
    void setUiSettings(Object uiSettings);

    @JsonAnyGetter
    Map<String, java.lang.Object> getAdditionalProperties();

    @JsonAnySetter
    void setAdditionalProperties(String key, java.lang.Object value);
  }
}
