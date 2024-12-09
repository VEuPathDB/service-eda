package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "subset",
    "computations",
    "starredVariables",
    "dataTableConfig",
    "derivedVariables"
})
public class AnalysisDescriptorImpl implements AnalysisDescriptor {
  @JsonProperty("subset")
  private AnalysisDescriptor.SubsetType subset;

  @JsonProperty("computations")
  private List<Computation> computations;

  @JsonProperty("starredVariables")
  private List<VariableSpec> starredVariables;

  @JsonProperty("dataTableConfig")
  private DataTableConfigSet dataTableConfig;

  @JsonProperty("derivedVariables")
  private List<String> derivedVariables;

  @JsonProperty("subset")
  public AnalysisDescriptor.SubsetType getSubset() {
    return this.subset;
  }

  @JsonProperty("subset")
  public void setSubset(AnalysisDescriptor.SubsetType subset) {
    this.subset = subset;
  }

  @JsonProperty("computations")
  public List<Computation> getComputations() {
    return this.computations;
  }

  @JsonProperty("computations")
  public void setComputations(List<Computation> computations) {
    this.computations = computations;
  }

  @JsonProperty("starredVariables")
  public List<VariableSpec> getStarredVariables() {
    return this.starredVariables;
  }

  @JsonProperty("starredVariables")
  public void setStarredVariables(List<VariableSpec> starredVariables) {
    this.starredVariables = starredVariables;
  }

  @JsonProperty("dataTableConfig")
  public DataTableConfigSet getDataTableConfig() {
    return this.dataTableConfig;
  }

  @JsonProperty("dataTableConfig")
  public void setDataTableConfig(DataTableConfigSet dataTableConfig) {
    this.dataTableConfig = dataTableConfig;
  }

  @JsonProperty("derivedVariables")
  public List<String> getDerivedVariables() {
    return this.derivedVariables;
  }

  @JsonProperty("derivedVariables")
  public void setDerivedVariables(List<String> derivedVariables) {
    this.derivedVariables = derivedVariables;
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @JsonPropertyOrder({
      "descriptor",
      "uiSettings"
  })
  public static class SubsetTypeImpl implements AnalysisDescriptor.SubsetType {
    @JsonProperty("descriptor")
    private List<Object> descriptor;

    @JsonProperty("uiSettings")
    private Object uiSettings;

    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new ExcludingMap();

    @JsonProperty("descriptor")
    public List<Object> getDescriptor() {
      return this.descriptor;
    }

    @JsonProperty("descriptor")
    public void setDescriptor(List<Object> descriptor) {
      this.descriptor = descriptor;
    }

    @JsonProperty("uiSettings")
    public Object getUiSettings() {
      return this.uiSettings;
    }

    @JsonProperty("uiSettings")
    public void setUiSettings(Object uiSettings) {
      this.uiSettings = uiSettings;
    }

    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
      return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String key, java.lang.Object value) {
      this.additionalProperties.put(key, value);
    }
  }
}
