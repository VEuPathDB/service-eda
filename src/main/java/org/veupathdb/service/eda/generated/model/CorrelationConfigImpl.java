package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(
    using = CorrelationConfig.CorrelationConfigDeserializer.class
)
@JsonSerialize(
    using = CorrelationConfig.Serializer.class
)
public class CorrelationConfigImpl implements CorrelationConfig {
  private Object anyType;

  private CorrelationConfigImpl() {
    this.anyType = null;
  }

  public CorrelationConfigImpl(Correlation1Collection correlation1Collection) {
    this.anyType = correlation1Collection;
  }

  public CorrelationConfigImpl(Correlation2Collections correlation2Collections) {
    this.anyType = correlation2Collections;
  }

  public Correlation1Collection getCorrelation1Collection() {
    if ( !(anyType instanceof  Correlation1Collection)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.eda.generated.model.Correlation1Collection");
    return (Correlation1Collection) anyType;
  }

  public Boolean isCorrelation1Collection() {
    return anyType instanceof Correlation1Collection;
  }

  public Correlation2Collections getCorrelation2Collections() {
    if ( !(anyType instanceof  Correlation2Collections)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.eda.generated.model.Correlation2Collections");
    return (Correlation2Collections) anyType;
  }

  public Boolean isCorrelation2Collections() {
    return anyType instanceof Correlation2Collections;
  }
}
