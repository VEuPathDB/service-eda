package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import java.util.Map;

@JsonDeserialize(
    as = InternalJobImpl.class
)
public interface InternalJob {
  @JsonProperty("jobId")
  String getJobId();

  @JsonProperty("jobId")
  void setJobId(String jobId);

  @JsonProperty("status")
  JobStatus getStatus();

  @JsonProperty("status")
  void setStatus(JobStatus status);

  @JsonProperty("owned")
  Boolean getOwned();

  @JsonProperty("owned")
  void setOwned(Boolean owned);

  @JsonProperty("created")
  Date getCreated();

  @JsonProperty("created")
  void setCreated(Date created);

  @JsonProperty("grabbed")
  Date getGrabbed();

  @JsonProperty("grabbed")
  void setGrabbed(Date grabbed);

  @JsonProperty("finished")
  Date getFinished();

  @JsonProperty("finished")
  void setFinished(Date finished);

  @JsonAnyGetter
  Map<String, Object> getAdditionalProperties();

  @JsonAnySetter
  void setAdditionalProperties(String key, Object value);
}
