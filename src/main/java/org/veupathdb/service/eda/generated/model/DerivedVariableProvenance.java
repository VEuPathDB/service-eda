package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.OffsetDateTime;

@JsonDeserialize(
    as = DerivedVariableProvenanceImpl.class
)
public interface DerivedVariableProvenance {
  @JsonProperty("copyDate")
  OffsetDateTime getCopyDate();

  @JsonProperty("copyDate")
  void setCopyDate(OffsetDateTime copyDate);

  @JsonProperty("copiedFrom")
  String getCopiedFrom();

  @JsonProperty("copiedFrom")
  void setCopiedFrom(String copiedFrom);
}
