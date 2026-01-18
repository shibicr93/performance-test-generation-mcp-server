package io.mcp.java.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mcp.java.tools.handlers.GatlingTestGeneratorHandler;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpError;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatlingTestGeneratorTool extends Tool {
  private static final Logger LOG = LoggerFactory.getLogger(GatlingTestGeneratorTool.class);

  public GatlingTestGeneratorTool() {
      LOG.info("Registering Gatling Test Generator Tool");
  }

  public McpSchema.Tool getToolDefinition() {
    var description = "Generates gatling performance tests based on specifications";
    var name = "generate_gatling_test";
    var title = "Gatling Test Generator";
    try {
      var schema =
          Files.readString(
              Paths.get(ClassLoader.getSystemResource("tools/generate_gatling_test.json").toURI()));
      return new McpSchema.Tool.Builder()
          .name(name)
          .title(title)
          .description(description)
          .inputSchema(new JacksonMcpJsonMapper(new ObjectMapper()), schema)
          .build();
    } catch (IOException | URISyntaxException e) {
      LOG.error("Failed to load Gatling Test Generator tool definition", e);
      throw McpError.builder(500)
          .message("Failed to load Gatling Test Generator tool definition")
          .data(e)
          .build();
    }
  }

  protected BiFunction<McpSyncServerExchange, McpSchema.CallToolRequest, McpSchema.CallToolResult>
      getToolHandler() {
    return new GatlingTestGeneratorHandler();
  }
}
