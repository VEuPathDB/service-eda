package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "user",
    "action",
    "timestamp"
})
public class HistoryCauseImpl implements HistoryCause {
  @JsonProperty("user")
  private HistoryUser user;

  @JsonProperty("action")
  private HistoryCause.ActionType action;


  @JsonProperty("timestamp")
  private OffsetDateTime timestamp;

  @JsonProperty("user")
  public HistoryUser getUser() {
    return this.user;
  }

  @JsonProperty("user")
  public void setUser(HistoryUser user) {
    this.user = user;
  }

  @JsonProperty("action")
  public HistoryCause.ActionType getAction() {
    return this.action;
  }

  @JsonProperty("action")
  public void setAction(HistoryCause.ActionType action) {
    this.action = action;
  }

  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return this.timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
