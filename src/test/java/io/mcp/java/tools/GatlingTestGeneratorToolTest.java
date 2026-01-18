package io.mcp.java.tools;

import static org.junit.jupiter.api.Assertions.*;

import io.mcp.java.tools.handlers.GatlingTestGeneratorHandler;
import org.junit.jupiter.api.Test;

class GatlingTestGeneratorToolTest {

  @Test
  void getToolDefinition() {
    ToolSchemaLoader schemaLoader = new ToolSchemaLoader();
    ToolFactory toolFactory = new ToolFactory(schemaLoader);

    var toolSpecification = toolFactory.createToolSpecification(new Tool.GatlingTestGenerator());
    var toolDef = toolSpecification.tool();

    assertEquals("generate_gatling_test", toolDef.name());
    assertEquals("Gatling Test Generator", toolDef.title());
    assertNotNull(toolDef.inputSchema());
    assertNotNull(toolDef.inputSchema().properties());
    assertInstanceOf(GatlingTestGeneratorHandler.class, toolSpecification.callHandler());
  }
}
