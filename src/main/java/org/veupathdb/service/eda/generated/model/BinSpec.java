package org.veupathdb.service.eda.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@JsonDeserialize(
    as = BinSpecImpl.class
)
public interface BinSpec {
  @JsonProperty("type")
  TypeType getType();

  @JsonProperty("type")
  void setType(TypeType type);

  @JsonProperty("value")
  Number getValue();

  @JsonProperty("value")
  void setValue(Number value);

  @JsonProperty("units")
  BinUnits getUnits();

  @JsonProperty("units")
  void setUnits(BinUnits units);

  @JsonProperty("range")
  RangeType getRange();

  @JsonProperty("range")
  void setRange(RangeType range);

  @JsonDeserialize(
      using = RangeType.RangeDeserializer.class
  )
  @JsonSerialize(
      using = RangeType.Serializer.class
  )
  interface RangeType {
    NumberRange getNumberRange();

    Boolean isNumberRange();

    DateRange getDateRange();

    Boolean isDateRange();

    class Serializer extends StdSerializer<RangeType> {
      public Serializer() {
        super(RangeType.class);}

      public void serialize(RangeType object, JsonGenerator jsonGenerator,
          SerializerProvider jsonSerializerProvider) throws IOException, JsonProcessingException {
        if ( object.isNumberRange()) {
          jsonGenerator.writeObject(object.getNumberRange());
          return;
        }
        if ( object.isDateRange()) {
          jsonGenerator.writeObject(object.getDateRange());
          return;
        }
        throw new IOException("Can't figure out type of object" + object);
      }
    }

    class RangeDeserializer extends StdDeserializer<RangeType> {
      public RangeDeserializer() {
        super(RangeType.class);}

      private Boolean looksLikeNumberRange(Map<String, Object> map) {
        return map.keySet().containsAll(Arrays.asList("min","max"));
      }

      private Boolean looksLikeDateRange(Map<String, Object> map) {
        return map.keySet().containsAll(Arrays.asList("min","max"));
      }

      public RangeType deserialize(JsonParser jsonParser, DeserializationContext jsonContext) throws
          IOException, JsonProcessingException {
        ObjectMapper mapper  = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonParser, Map.class);
        if ( looksLikeNumberRange(map) ) return new BinSpecImpl.RangeTypeImpl(mapper.convertValue(map, NumberRangeImpl.class));
        if ( looksLikeDateRange(map) ) return new BinSpecImpl.RangeTypeImpl(mapper.convertValue(map, DateRangeImpl.class));
        throw new IOException("Can't figure out type of object" + map);
      }
    }
  }

  enum TypeType {
    @JsonProperty("binWidth")
    BINWIDTH("binWidth"),

    @JsonProperty("numBins")
    NUMBINS("numBins");

    public final String value;

    public String getValue() {
      return this.value;
    }

    TypeType(String name) {
      this.value = name;
    }
  }
}
