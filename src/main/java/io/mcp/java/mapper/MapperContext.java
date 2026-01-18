package io.mcp.java.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;

public class MapperContext {
  public static ObjectMapper getObjectMapper() {
    return new ObjectMapper()
        .configure(
            com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
  }

  public static JacksonMcpJsonMapper getMcpJsonMapper() {
    return new JacksonMcpJsonMapper(getObjectMapper());
  }
}
