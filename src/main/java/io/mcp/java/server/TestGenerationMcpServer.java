package io.mcp.java.server;

import static io.mcp.java.mapper.MapperContext.getObjectMapper;

import io.mcp.java.tools.Tool;
import io.mcp.java.tools.ToolFactory;
import io.mcp.java.tools.ToolSchemaLoader;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestGenerationMcpServer {
  private static final Logger LOG = LoggerFactory.getLogger(TestGenerationMcpServer.class);

  public static void start() {
    LOG.info("Starting test generation MCP server");

    StdioServerTransportProvider transportProvider =
        new StdioServerTransportProvider(new JacksonMcpJsonMapper(getObjectMapper()));

    McpSyncServer mcpSyncServer =
        McpServer.sync(transportProvider)
            .serverInfo("performance-test-generation-mcp-server", "1.0.0")
            .capabilities(
                McpSchema.ServerCapabilities.builder().tools(true).prompts(true).logging().build())
            .build();

    ToolFactory toolFactory = new ToolFactory(new ToolSchemaLoader());

    mcpSyncServer.addTool(toolFactory.createToolSpecification(new Tool.GatlingTestGenerator()));
    mcpSyncServer.addTool(toolFactory.createToolSpecification(new Tool.TestScenariosSuggester()));
  }
}
