package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(
    using = SampleSize.SampleSizeDeserializer.class
)
@JsonSerialize(
    using = SampleSize.Serializer.class
)
public class SampleSizeImpl implements SampleSize {
  private Object anyType;

  private SampleSizeImpl() {
    this.anyType = null;
  }

  public SampleSizeImpl(SimpleSampleSize simpleSampleSize) {
    this.anyType = simpleSampleSize;
  }

  public SampleSizeImpl(ProportionSampleSize proportionSampleSize) {
    this.anyType = proportionSampleSize;
  }

  public SimpleSampleSize getSimpleSampleSize() {
    if ( !(anyType instanceof  SimpleSampleSize)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.eda.generated.model.SimpleSampleSize");
    return (SimpleSampleSize) anyType;
  }

  public Boolean isSimpleSampleSize() {
    return anyType instanceof SimpleSampleSize;
  }

  public ProportionSampleSize getProportionSampleSize() {
    if ( !(anyType instanceof  ProportionSampleSize)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.eda.generated.model.ProportionSampleSize");
    return (ProportionSampleSize) anyType;
  }

  public Boolean isProportionSampleSize() {
    return anyType instanceof ProportionSampleSize;
  }
}
