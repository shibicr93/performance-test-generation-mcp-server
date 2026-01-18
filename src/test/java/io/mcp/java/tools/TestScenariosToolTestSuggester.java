package io.mcp.java.tools;

import static org.junit.jupiter.api.Assertions.*;

import io.mcp.java.tools.handlers.SuggestTestScenariosHandler;
import org.junit.jupiter.api.Test;

class TestScenariosToolTestSuggester {
  @Test
  void getToolDefinition() {
    ToolSchemaLoader schemaLoader = new ToolSchemaLoader();
    ToolFactory toolFactory = new ToolFactory(schemaLoader);
    var toolSpecification = toolFactory.createToolSpecification(new Tool.TestScenariosSuggester());
    var toolDef = toolSpecification.tool();

    assertEquals("suggest_test_scenarios", toolDef.name());
    assertEquals("Test Scenarios Suggester", toolDef.title());
    assertNotNull(toolDef.inputSchema());
    assertNotNull(toolDef.inputSchema().properties());
    assertInstanceOf(SuggestTestScenariosHandler.class, toolSpecification.callHandler());
  }
}
