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
    using = DynamicDataSpec.DynamicDataSpecDeserializer.class
)
@JsonSerialize(
    using = DynamicDataSpec.Serializer.class
)
public interface DynamicDataSpec {
  CollectionSpec getCollectionSpec();

  Boolean isCollectionSpec();

  VariableSpec getVariableSpec();

  Boolean isVariableSpec();

  class Serializer extends StdSerializer<DynamicDataSpec> {
    public Serializer() {
      super(DynamicDataSpec.class);}

    public void serialize(DynamicDataSpec object, JsonGenerator jsonGenerator,
        SerializerProvider jsonSerializerProvider) throws IOException, JsonProcessingException {
      if ( object.isCollectionSpec()) {
        jsonGenerator.writeObject(object.getCollectionSpec());
        return;
      }
      if ( object.isVariableSpec()) {
        jsonGenerator.writeObject(object.getVariableSpec());
        return;
      }
      throw new IOException("Can't figure out type of object" + object);
    }
  }

  class DynamicDataSpecDeserializer extends StdDeserializer<DynamicDataSpec> {
    public DynamicDataSpecDeserializer() {
      super(DynamicDataSpec.class);}

    private Boolean looksLikeCollectionSpec(Map<String, Object> map) {
      return map.keySet().containsAll(Arrays.asList("entityId","collectionId"));
    }

    private Boolean looksLikeVariableSpec(Map<String, Object> map) {
      return map.keySet().containsAll(Arrays.asList("entityId","variableId"));
    }

    public DynamicDataSpec deserialize(JsonParser jsonParser, DeserializationContext jsonContext)
        throws IOException, JsonProcessingException {
      ObjectMapper mapper  = new ObjectMapper();
      Map<String, Object> map = mapper.readValue(jsonParser, Map.class);
      if ( looksLikeCollectionSpec(map) ) return new DynamicDataSpecImpl(mapper.convertValue(map, CollectionSpecImpl.class));
      if ( looksLikeVariableSpec(map) ) return new DynamicDataSpecImpl(mapper.convertValue(map, VariableSpecImpl.class));
      throw new IOException("Can't figure out type of object" + map);
    }
  }
}
