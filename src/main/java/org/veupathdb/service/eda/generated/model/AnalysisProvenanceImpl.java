package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "onImport",
    "current"
})
public class AnalysisProvenanceImpl implements AnalysisProvenance {
  @JsonProperty("onImport")
  private OnImportProvenanceProps onImport;

  @JsonProperty("current")
  private CurrentProvenanceProps current;

  @JsonProperty("onImport")
  public OnImportProvenanceProps getOnImport() {
    return this.onImport;
  }

  @JsonProperty("onImport")
  public void setOnImport(OnImportProvenanceProps onImport) {
    this.onImport = onImport;
  }

  @JsonProperty("current")
  public CurrentProvenanceProps getCurrent() {
    return this.current;
  }

  @JsonProperty("current")
  public void setCurrent(CurrentProvenanceProps current) {
    this.current = current;
  }
}
