package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "jobId",
    "status",
    "owned",
    "created",
    "grabbed",
    "finished"
})
public class InternalJobImpl implements InternalJob {
  @JsonProperty("jobId")
  private String jobId;

  @JsonProperty("status")
  private JobStatus status;

  @JsonProperty("owned")
  private Boolean owned;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
  )
  @JsonDeserialize(
      using = TimestampDeserializer.class
  )
  @JsonProperty("created")
  private Date created;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
  )
  @JsonDeserialize(
      using = TimestampDeserializer.class
  )
  @JsonProperty("grabbed")
  private Date grabbed;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
  )
  @JsonDeserialize(
      using = TimestampDeserializer.class
  )
  @JsonProperty("finished")
  private Date finished;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new ExcludingMap();

  @JsonProperty("jobId")
  public String getJobId() {
    return this.jobId;
  }

  @JsonProperty("jobId")
  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  @JsonProperty("status")
  public JobStatus getStatus() {
    return this.status;
  }

  @JsonProperty("status")
  public void setStatus(JobStatus status) {
    this.status = status;
  }

  @JsonProperty("owned")
  public Boolean getOwned() {
    return this.owned;
  }

  @JsonProperty("owned")
  public void setOwned(Boolean owned) {
    this.owned = owned;
  }

  @JsonProperty("created")
  public Date getCreated() {
    return this.created;
  }

  @JsonProperty("created")
  public void setCreated(Date created) {
    this.created = created;
  }

  @JsonProperty("grabbed")
  public Date getGrabbed() {
    return this.grabbed;
  }

  @JsonProperty("grabbed")
  public void setGrabbed(Date grabbed) {
    this.grabbed = grabbed;
  }

  @JsonProperty("finished")
  public Date getFinished() {
    return this.finished;
  }

  @JsonProperty("finished")
  public void setFinished(Date finished) {
    this.finished = finished;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperties(String key, Object value) {
    this.additionalProperties.put(key, value);
  }
}
