package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "modifiedDate",
    "size"
})
public class FileImpl implements File {
  @JsonProperty("name")
  private String name;

  @JsonProperty("modifiedDate")
  private String modifiedDate;

  @JsonProperty("size")
  private String size;

  @JsonProperty("name")
  public String getName() {
    return this.name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty("modifiedDate")
  public String getModifiedDate() {
    return this.modifiedDate;
  }

  @JsonProperty("modifiedDate")
  public void setModifiedDate(String modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  @JsonProperty("size")
  public String getSize() {
    return this.size;
  }

  @JsonProperty("size")
  public void setSize(String size) {
    this.size = size;
  }
}
