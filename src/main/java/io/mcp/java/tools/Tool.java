package io.mcp.java.tools;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.function.BiFunction;

abstract class Tool {
  public McpServerFeatures.SyncToolSpecification getToolSpecification() {
    return new McpServerFeatures.SyncToolSpecification.Builder()
        .tool(getToolDefinition())
        .callHandler(getToolHandler())
        .build();
  }

  protected abstract McpSchema.Tool getToolDefinition();

  protected abstract BiFunction<
          McpSyncServerExchange, McpSchema.CallToolRequest, McpSchema.CallToolResult>
      getToolHandler();
}
