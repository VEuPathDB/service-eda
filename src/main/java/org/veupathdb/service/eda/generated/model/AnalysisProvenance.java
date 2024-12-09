package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = AnalysisProvenanceImpl.class
)
public interface AnalysisProvenance {
  @JsonProperty("onImport")
  OnImportProvenanceProps getOnImport();

  @JsonProperty("onImport")
  void setOnImport(OnImportProvenanceProps onImport);

  @JsonProperty("current")
  CurrentProvenanceProps getCurrent();

  @JsonProperty("current")
  void setCurrent(CurrentProvenanceProps current);
}
