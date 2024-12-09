package org.veupathdb.service.eda.generated.model;

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
    using = SampleSize.SampleSizeDeserializer.class
)
@JsonSerialize(
    using = SampleSize.Serializer.class
)
public interface SampleSize {
  SimpleSampleSize getSimpleSampleSize();

  Boolean isSimpleSampleSize();

  ProportionSampleSize getProportionSampleSize();

  Boolean isProportionSampleSize();

  class Serializer extends StdSerializer<SampleSize> {
    public Serializer() {
      super(SampleSize.class);}

    public void serialize(SampleSize object, JsonGenerator jsonGenerator,
        SerializerProvider jsonSerializerProvider) throws IOException, JsonProcessingException {
      if ( object.isSimpleSampleSize()) {
        jsonGenerator.writeObject(object.getSimpleSampleSize());
        return;
      }
      if ( object.isProportionSampleSize()) {
        jsonGenerator.writeObject(object.getProportionSampleSize());
        return;
      }
      throw new IOException("Can't figure out type of object" + object);
    }
  }

  class SampleSizeDeserializer extends StdDeserializer<SampleSize> {
    public SampleSizeDeserializer() {
      super(SampleSize.class);}

    private Boolean looksLikeSimpleSampleSize(Map<String, Object> map) {
      return map.keySet().containsAll(Arrays.asList("N"));
    }

    private Boolean looksLikeProportionSampleSize(Map<String, Object> map) {
      return map.keySet().containsAll(Arrays.asList("numeratorN","denominatorN"));
    }

    public SampleSize deserialize(JsonParser jsonParser, DeserializationContext jsonContext) throws
        IOException, JsonProcessingException {
      ObjectMapper mapper  = new ObjectMapper();
      Map<String, Object> map = mapper.readValue(jsonParser, Map.class);
      if ( looksLikeSimpleSampleSize(map) ) return new SampleSizeImpl(mapper.convertValue(map, SimpleSampleSizeImpl.class));
      if ( looksLikeProportionSampleSize(map) ) return new SampleSizeImpl(mapper.convertValue(map, ProportionSampleSizeImpl.class));
      throw new IOException("Can't figure out type of object" + map);
    }
  }
}
