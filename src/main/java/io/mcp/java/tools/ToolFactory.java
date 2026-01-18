package io.mcp.java.tools;

import static io.mcp.java.mapper.MapperContext.getMcpJsonMapper;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;

/** Factory for creating tool definitions. */
public class ToolFactory {
  private final ToolSchemaLoader schemaLoader;

  public ToolFactory(ToolSchemaLoader schemaLoader) {
    this.schemaLoader = schemaLoader;
  }

  /**
   * Create a tool specification with the given parameters.
   *
   * @param tool tool definition
   * @return tool specification
   */
  public McpServerFeatures.SyncToolSpecification createToolSpecification(Tool tool) {
    return new McpServerFeatures.SyncToolSpecification.Builder()
        .tool(createTool(tool))
        .callHandler(tool.handler())
        .build();
  }

  private McpSchema.Tool createTool(Tool tool) {
    String schema = schemaLoader.loadSchema(tool.name());
    return new McpSchema.Tool.Builder()
        .name(tool.name())
        .title(tool.title())
        .description(tool.description())
        .inputSchema(getMcpJsonMapper(), schema)
        .build();
  }
}
