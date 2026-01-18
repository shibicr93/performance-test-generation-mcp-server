package io.mcp.java.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperContext {
  public static ObjectMapper getObjectMapper() {
    return new ObjectMapper()
        .configure(
            com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
  }
}
