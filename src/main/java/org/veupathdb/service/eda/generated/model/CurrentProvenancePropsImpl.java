package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "isDeleted",
    "modificationTime",
    "isPublic"
})
public class CurrentProvenancePropsImpl implements CurrentProvenanceProps {
  @JsonProperty("isDeleted")
  private Boolean isDeleted;

  @JsonProperty("modificationTime")
  private String modificationTime;

  @JsonProperty("isPublic")
  private Boolean isPublic;

  @JsonProperty("isDeleted")
  public Boolean getIsDeleted() {
    return this.isDeleted;
  }

  @JsonProperty("isDeleted")
  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  @JsonProperty("modificationTime")
  public String getModificationTime() {
    return this.modificationTime;
  }

  @JsonProperty("modificationTime")
  public void setModificationTime(String modificationTime) {
    this.modificationTime = modificationTime;
  }

  @JsonProperty("isPublic")
  public Boolean getIsPublic() {
    return this.isPublic;
  }

  @JsonProperty("isPublic")
  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }
}
