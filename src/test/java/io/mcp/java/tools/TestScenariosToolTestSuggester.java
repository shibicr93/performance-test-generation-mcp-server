package io.mcp.java.tools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestScenariosToolTestSuggester {
  @Test
  void getToolDefinition() {
    TestScenariosSuggesterTool tool = new TestScenariosSuggesterTool();
    var toolDef = tool.getToolDefinition();

    assertEquals("suggest_test_scenarios", toolDef.name());
    assertEquals("Test Scenarios Suggester", toolDef.title());
    assertNotNull(toolDef.inputSchema());
    assertNotNull(toolDef.inputSchema().properties());
  }

  @Test
  void getToolHandler() {
    TestScenariosSuggesterTool tool = new TestScenariosSuggesterTool();
    var handler = tool.getToolHandler();

    assertNotNull(handler);
  }
}
