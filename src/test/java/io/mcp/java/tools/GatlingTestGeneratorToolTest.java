package io.mcp.java.tools;

import static org.junit.jupiter.api.Assertions.*;

import io.mcp.java.tools.handlers.GatlingTestGeneratorHandler;
import org.junit.jupiter.api.Test;

class GatlingTestGeneratorToolTest {

  @Test
  void getToolDefinition() {
    GatlingTestGeneratorTool gatlingTestGeneratorTool = new GatlingTestGeneratorTool();

    var toolDef = gatlingTestGeneratorTool.getToolDefinition();

    assertEquals("generate_gatling_test", toolDef.name());
    assertEquals("Gatling Test Generator", toolDef.title());
    assertNotNull(toolDef.inputSchema());
    assertNotNull(toolDef.inputSchema().properties());
  }

  @Test
  void getToolHandler() {
    GatlingTestGeneratorTool gatlingTestGeneratorTool = new GatlingTestGeneratorTool();

    var handler = gatlingTestGeneratorTool.getToolHandler();

    assertNotNull(handler);
    assertInstanceOf(GatlingTestGeneratorHandler.class, handler);
  }
}
