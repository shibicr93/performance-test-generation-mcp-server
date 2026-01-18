package io.mcp.java.tools.handlers;

import io.mcp.java.mapper.MapperContext;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatlingTestGeneratorHandler implements ToolHandler {
  private static final Logger LOG =
      LoggerFactory.getLogger(GatlingTestGeneratorHandler.class.getName());

  @Override
  public McpSchema.CallToolResult apply(
      McpSyncServerExchange exchange, McpSchema.CallToolRequest request) {
    LOG.info("GatlingTestGeneratorHandler.apply()");
    // Check if client supports sampling
    if (exchange.getClientCapabilities().sampling() == null) {
      return new McpSchema.CallToolResult(
          List.of(new McpSchema.TextContent("Client does not support sampling capability")), true);
    }

    // Extract arguments from the request
    Map<String, Object> arguments = request.arguments();

    // Build the prompt for Gatling test generation
    String scenarios =
        MapperContext.getObjectMapper().convertValue(arguments.get("scenarios"), String.class);
    String baseUrl = (String) arguments.get("baseUrl");
    String testName = (String) arguments.getOrDefault("testName", "GeneratedGatlingTest");

    String prompt = buildGenerationPrompt(scenarios, baseUrl, testName);

    LOG.info("Sending Sampling request with prompt: {}", prompt);
    // Create a sampling request
    McpSchema.CreateMessageRequest samplingRequest =
        McpSchema.CreateMessageRequest.builder()
            .messages(
                List.of(
                    new McpSchema.SamplingMessage(
                        McpSchema.Role.USER, new McpSchema.TextContent(prompt))))
            .modelPreferences(
                McpSchema.ModelPreferences.builder()
                    .hints(
                        List.of(
                            McpSchema.ModelHint.of("claude-3-5-sonnet"),
                            McpSchema.ModelHint.of("claude")))
                    .intelligencePriority(0.9) // Prioritize intelligence for code generation
                    .speedPriority(0.3) // Lower speed importance
                    .costPriority(0.7)
                    .build())
            .systemPrompt(
                "You are an expert Gatling performance test generator. Generate Scala-based Gatling test scripts that are production-ready and follow best practices. Return only the Scala code without any explanations.")
            .maxTokens(4000)
            .build();

    // Request sampling from the client
    McpSchema.CreateMessageResult result = exchange.createMessage(samplingRequest);
    LOG.info("Received Sampling result");
    // Process the result
    return new McpSchema.CallToolResult.Builder().addContent(result.content()).build();
  }

  private String buildGenerationPrompt(String scenarios, String baseUrl, String testName) {
    return String.format(
        "Generate a Gatling performance test script in Scala with the following specifications:\n"
            + "- Test Name: %s\n"
            + "- Base URL: %s\n"
            + "- Test Scenarios:\n%s\n\n"
            + "The generated test should:\n"
            + "1. Use Gatling best practices\n"
            + "2. Include proper setup, execution, and assertions\n"
            + "3. Handle HTTP requests appropriately\n"
            + "4. Include error handling and response validation\n"
            + "5. Be ready for production use\n\n"
            + "Return only the Scala code.",
        testName, baseUrl, scenarios);
  }
}
