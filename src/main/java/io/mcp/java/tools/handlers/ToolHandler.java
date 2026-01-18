package io.mcp.java.tools.handlers;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.function.BiFunction;

public sealed interface ToolHandler
    extends BiFunction<McpSyncServerExchange, McpSchema.CallToolRequest, McpSchema.CallToolResult>
    permits GatlingTestGeneratorHandler, SuggestTestScenariosHandler {

  McpSchema.CallToolResult apply(McpSyncServerExchange exchange, McpSchema.CallToolRequest request);
}
