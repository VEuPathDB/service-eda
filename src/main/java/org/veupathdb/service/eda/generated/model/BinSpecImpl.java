package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "value",
    "units",
    "range"
})
public class BinSpecImpl implements BinSpec {
  @JsonProperty("type")
  private BinSpec.TypeType type;

  @JsonProperty("value")
  private Number value;

  @JsonProperty("units")
  private BinUnits units;

  @JsonProperty("range")
  private BinSpec.RangeType range;

  @JsonProperty("type")
  public BinSpec.TypeType getType() {
    return this.type;
  }

  @JsonProperty("type")
  public void setType(BinSpec.TypeType type) {
    this.type = type;
  }

  @JsonProperty("value")
  public Number getValue() {
    return this.value;
  }

  @JsonProperty("value")
  public void setValue(Number value) {
    this.value = value;
  }

  @JsonProperty("units")
  public BinUnits getUnits() {
    return this.units;
  }

  @JsonProperty("units")
  public void setUnits(BinUnits units) {
    this.units = units;
  }

  @JsonProperty("range")
  public BinSpec.RangeType getRange() {
    return this.range;
  }

  @JsonProperty("range")
  public void setRange(BinSpec.RangeType range) {
    this.range = range;
  }

  @JsonDeserialize(
      using = RangeType.RangeDeserializer.class
  )
  @JsonSerialize(
      using = RangeType.Serializer.class
  )
  public static class RangeTypeImpl implements BinSpec.RangeType {
    private Object anyType;

    private RangeTypeImpl() {
      this.anyType = null;
    }

    public RangeTypeImpl(NumberRange numberRange) {
      this.anyType = numberRange;
    }

    public RangeTypeImpl(DateRange dateRange) {
      this.anyType = dateRange;
    }

    public NumberRange getNumberRange() {
      if ( !(anyType instanceof  NumberRange)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.eda.generated.model.NumberRange");
      return (NumberRange) anyType;
    }

    public Boolean isNumberRange() {
      return anyType instanceof NumberRange;
    }

    public DateRange getDateRange() {
      if ( !(anyType instanceof  DateRange)) throw new IllegalStateException("fetching wrong type out of the union: org.veupathdb.service.eda.generated.model.DateRange");
      return (DateRange) anyType;
    }

    public Boolean isDateRange() {
      return anyType instanceof DateRange;
    }
  }
}
