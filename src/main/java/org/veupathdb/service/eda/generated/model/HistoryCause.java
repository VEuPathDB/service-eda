package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.OffsetDateTime;

@JsonDeserialize(
    as = HistoryCauseImpl.class
)
public interface HistoryCause {
  @JsonProperty("user")
  HistoryUser getUser();

  @JsonProperty("user")
  void setUser(HistoryUser user);

  @JsonProperty("action")
  ActionType getAction();

  @JsonProperty("action")
  void setAction(ActionType action);

  @JsonProperty("timestamp")
  OffsetDateTime getTimestamp();

  @JsonProperty("timestamp")
  void setTimestamp(OffsetDateTime timestamp);

  enum ActionType {
    @JsonProperty("CREATE")
    CREATE("CREATE"),

    @JsonProperty("UPDATE")
    UPDATE("UPDATE"),

    @JsonProperty("DELETE")
    DELETE("DELETE");

    public final String value;

    public String getValue() {
      return this.value;
    }

    ActionType(String name) {
      this.value = name;
    }
  }
}
