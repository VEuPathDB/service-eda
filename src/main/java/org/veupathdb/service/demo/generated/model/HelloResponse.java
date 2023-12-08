package org.veupathdb.service.demo.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(
    as = HelloResponseImpl.class
)
public interface HelloResponse {
  @JsonProperty("greeting")
  GreetingType getGreeting();

  @JsonProperty("greeting")
  void setGreeting(GreetingType greeting);

  @JsonProperty("anotherType")
  AnotherType getAnotherType();

  @JsonProperty("anotherType")
  void setAnotherType(AnotherType anotherType);

  enum GreetingType {
    @JsonProperty("Hello World")
    HELLOWORLD("Hello World");

    public final String value;

    public String getValue() {
      return this.value;
    }

    GreetingType(String name) {
      this.value = name;
    }
  }
}
