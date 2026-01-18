package io.mcp.java.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mcp.java.tools.handlers.SuggestTestScenariosHandler;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpError;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BiFunction;

public class TestScenariosSuggesterTool extends Tool {
    private static final Logger LOG = LoggerFactory.getLogger(TestScenariosSuggesterTool.class);

    public TestScenariosSuggesterTool() {
        LOG.info("Registering Test Scenarios Suggester Tool");
    }

  @Override
  protected McpSchema.Tool getToolDefinition() {
    var description = "Suggest load testing scenarios based on API analysis";
    var name = "suggest_test_scenarios";
    var title = "Test Scenarios Suggester";
    String schema;
    try {
      schema =
          Files.readString(
              Paths.get(
                  ClassLoader.getSystemResource("tools/suggest_test_scenarios.json").toURI()));
    } catch (IOException | URISyntaxException e) {
      throw McpError.builder(500)
          .message("Failed to load Test Scenarios Suggester tool definition")
          .data(e)
          .build();
    }
    return new McpSchema.Tool.Builder()
        .name(name)
        .title(title)
        .description(description)
        .inputSchema(new JacksonMcpJsonMapper(new ObjectMapper()), schema)
        .build();
  }

  @Override
  protected BiFunction<McpSyncServerExchange, McpSchema.CallToolRequest, McpSchema.CallToolResult>
      getToolHandler() {
    return new SuggestTestScenariosHandler();
  }
}
