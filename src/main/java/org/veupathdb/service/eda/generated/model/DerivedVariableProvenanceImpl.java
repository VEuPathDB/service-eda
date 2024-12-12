package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "copyDate",
    "copiedFrom"
})
public class DerivedVariableProvenanceImpl implements DerivedVariableProvenance {

  @JsonProperty("copyDate")
  private OffsetDateTime copyDate;

  @JsonProperty("copiedFrom")
  private String copiedFrom;

  @JsonProperty("copyDate")
  public OffsetDateTime getCopyDate() {
    return this.copyDate;
  }

  @JsonProperty("copyDate")
  public void setCopyDate(OffsetDateTime copyDate) {
    this.copyDate = copyDate;
  }

  @JsonProperty("copiedFrom")
  public String getCopiedFrom() {
    return this.copiedFrom;
  }

  @JsonProperty("copiedFrom")
  public void setCopiedFrom(String copiedFrom) {
    this.copiedFrom = copiedFrom;
  }
}
