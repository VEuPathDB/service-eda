package org.veupathdb.service.eda.access.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageTemplate(@JsonGetter(FIELD_SUBJECT) String subject, @JsonGetter(FIELD_BODY) String body) {
  public static final String
                             FIELD_SUBJECT = "subject";
  public static final String FIELD_BODY    = "body";

  @JsonCreator
  public MessageTemplate(
    @JsonProperty(FIELD_SUBJECT) final String subject,
    @JsonProperty(FIELD_BODY) final String body
  ) {
    this.subject = subject;
    this.body = body;
  }
}
