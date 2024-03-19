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
    using = CorrelationConfig.CorrelationConfigDeserializer.class
)
@JsonSerialize(
    using = CorrelationConfig.Serializer.class
)
public interface CorrelationConfig {
  Correlation1Collection getCorrelation1Collection();

  Boolean isCorrelation1Collection();

  Correlation2Collections getCorrelation2Collections();

  Boolean isCorrelation2Collections();

  class Serializer extends StdSerializer<CorrelationConfig> {
    public Serializer() {
      super(CorrelationConfig.class);}

    public void serialize(CorrelationConfig object, JsonGenerator jsonGenerator,
        SerializerProvider jsonSerializerProvider) throws IOException, JsonProcessingException {
      if ( object.isCorrelation1Collection()) {
        jsonGenerator.writeObject(object.getCorrelation1Collection());
        return;
      }
      if ( object.isCorrelation2Collections()) {
        jsonGenerator.writeObject(object.getCorrelation2Collections());
        return;
      }
      throw new IOException("Can't figure out type of object" + object);
    }
  }

  class CorrelationConfigDeserializer extends StdDeserializer<CorrelationConfig> {
    public CorrelationConfigDeserializer() {
      super(CorrelationConfig.class);}

    private Boolean looksLikeCorrelation1Collection(Map<String, Object> map) {
      return map.keySet().containsAll(Arrays.asList("correlationMethod","prefilterThresholds","collectionVariable"));
    }

    private Boolean looksLikeCorrelation2Collections(Map<String, Object> map) {
      return map.keySet().containsAll(Arrays.asList("correlationMethod","prefilterThresholds","collectionVariable1","collectionVariable2"));
    }

    public CorrelationConfig deserialize(JsonParser jsonParser, DeserializationContext jsonContext)
        throws IOException, JsonProcessingException {
      ObjectMapper mapper  = new ObjectMapper();
      Map<String, Object> map = mapper.readValue(jsonParser, Map.class);
      if ( looksLikeCorrelation1Collection(map) ) return new CorrelationConfigImpl(mapper.convertValue(map, Correlation1CollectionImpl.class));
      if ( looksLikeCorrelation2Collections(map) ) return new CorrelationConfigImpl(mapper.convertValue(map, Correlation2CollectionsImpl.class));
      throw new IOException("Can't figure out type of object" + map);
    }
  }
}
