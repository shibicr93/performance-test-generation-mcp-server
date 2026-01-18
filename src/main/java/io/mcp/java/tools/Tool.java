package io.mcp.java.tools;

import io.mcp.java.tools.handlers.GatlingTestGeneratorHandler;
import io.mcp.java.tools.handlers.SuggestTestScenariosHandler;
import io.mcp.java.tools.handlers.ToolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Tool definitions for the MCP server. */
public sealed interface Tool permits Tool.GatlingTestGenerator, Tool.TestScenariosSuggester {
  Logger LOG = LoggerFactory.getLogger(Tool.class);

  String name();

  String title();

  String description();

  ToolHandler handler();

  record GatlingTestGenerator(String name, String title, String description, ToolHandler handler)
      implements Tool {
    public GatlingTestGenerator() {
      this(
          "generate_gatling_test",
          "Gatling Test Generator",
          "Generates gatling performance tests based on specifications",
          new GatlingTestGeneratorHandler());
      LOG.info("Registering Gatling Test Generator Tool");
    }
  }

  record TestScenariosSuggester(String name, String title, String description, ToolHandler handler)
      implements Tool {
    public TestScenariosSuggester() {
      this(
          "suggest_test_scenarios",
          "Test Scenarios Suggester",
          "Suggests test scenarios based on application specifications",
          new SuggestTestScenariosHandler());
      LOG.info("Registering Test Scenarios Suggester Tool");
    }
  }
}
