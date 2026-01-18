package io.mcp.java.tools.handlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("SuggestTestScenariosHandler Tests")
class SuggestTestScenariosHandlerTest {

  private SuggestTestScenariosHandler handler;
  private McpSyncServerExchange mockExchange;
  private McpSchema.CallToolRequest mockRequest;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    handler = new SuggestTestScenariosHandler();
    mockExchange = mock(McpSyncServerExchange.class);
    mockRequest = mock(McpSchema.CallToolRequest.class);
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("Should generate smoke test scenario by default")
  void testGenerateSmokeTestByDefault() {
    // Arrange
    Map<String, Object> arguments = createBasicArguments();
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    assertNotNull(result);
    List<McpSchema.Content> contents = result.content();
    assertFalse(contents.isEmpty());

    String jsonResult = ((McpSchema.TextContent) contents.get(0)).text();
    Map<String, Object> response = parseResponse(jsonResult);

    assertNotNull(response.get("scenarios"));
    List<Map<String, Object>> scenarios = (List<Map<String, Object>>) response.get("scenarios");
    assertTrue(
        scenarios.stream().anyMatch(s -> "smoke".equals(s.get("type"))),
        "Should include smoke test scenario");
  }

  @Test
  @DisplayName("Should generate basic level test scenarios")
  void testGenerateBasicLevelScenarios() {
    // Arrange
    Map<String, Object> arguments = createArgumentsWithTestLevel("basic");
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    assertNotNull(result);
    String jsonResult = ((McpSchema.TextContent) result.content().get(0)).text();
    Map<String, Object> response = parseResponse(jsonResult);

    List<Map<String, Object>> scenarios = (List<Map<String, Object>>) response.get("scenarios");
    assertNotNull(scenarios);

    Set<String> scenarioTypes = new HashSet<>();
    for (Map<String, Object> scenario : scenarios) {
      scenarioTypes.add((String) scenario.get("type"));
    }

    assertTrue(scenarioTypes.contains("smoke"), "Should include smoke test");
    assertTrue(scenarioTypes.contains("load"), "Should include load test");
    assertEquals(2, scenarios.size(), "Basic level should have 2 scenarios");
  }

  @Test
  @DisplayName("Should generate comprehensive level test scenarios")
  void testGenerateComprehensiveLevelScenarios() {
    // Arrange
    Map<String, Object> arguments = createArgumentsWithTestLevel("comprehensive");
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    assertNotNull(result);
    String jsonResult = ((McpSchema.TextContent) result.content().get(0)).text();
    Map<String, Object> response = parseResponse(jsonResult);

    List<Map<String, Object>> scenarios = (List<Map<String, Object>>) response.get("scenarios");
    assertNotNull(scenarios);

    Set<String> scenarioTypes = new HashSet<>();
    for (Map<String, Object> scenario : scenarios) {
      scenarioTypes.add((String) scenario.get("type"));
    }

    assertTrue(scenarioTypes.contains("smoke"), "Should include smoke test");
    assertTrue(scenarioTypes.contains("load"), "Should include load test");
    assertTrue(scenarioTypes.contains("stress"), "Should include stress test");
    assertTrue(scenarioTypes.contains("spike"), "Should include spike test");
    assertEquals(5, scenarios.size(), "Comprehensive level should have 5 scenarios");
  }

  @Test
  @DisplayName("Should generate advanced level test scenarios")
  void testGenerateAdvancedLevelScenarios() {
    // Arrange
    Map<String, Object> arguments = createArgumentsWithTestLevel("advanced");
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    assertNotNull(result);
    String jsonResult = ((McpSchema.TextContent) result.content().get(0)).text();
    Map<String, Object> response = parseResponse(jsonResult);

    List<Map<String, Object>> scenarios = (List<Map<String, Object>>) response.get("scenarios");
    assertNotNull(scenarios);

    Set<String> scenarioTypes = new HashSet<>();
    for (Map<String, Object> scenario : scenarios) {
      scenarioTypes.add((String) scenario.get("type"));
    }

    assertTrue(scenarioTypes.contains("smoke"), "Should include smoke test");
    assertTrue(scenarioTypes.contains("load"), "Should include load test");
    assertTrue(scenarioTypes.contains("stress"), "Should include stress test");
    assertTrue(scenarioTypes.contains("spike"), "Should include spike test");
    assertTrue(scenarioTypes.contains("endurance"), "Should include endurance test");
    assertTrue(scenarioTypes.contains("volume"), "Should include volume test");
    assertEquals(7, scenarios.size(), "Advanced level should have 7 scenarios");
  }

  @ParameterizedTest
  @ValueSource(strings = {"basic", "comprehensive", "advanced"})
  @DisplayName("Should include success criteria in all scenarios")
  void testSuccessCriteriaPresent(String testLevel) {
    // Arrange
    Map<String, Object> arguments = createArgumentsWithTestLevel(testLevel);
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    String jsonResult = ((McpSchema.TextContent) result.content().get(0)).text();
    Map<String, Object> response = parseResponse(jsonResult);
    List<Map<String, Object>> scenarios = (List<Map<String, Object>>) response.get("scenarios");

    for (Map<String, Object> scenario : scenarios) {
      assertNotNull(
          scenario.get("successCriteria"),
          "Scenario '" + scenario.get("name") + "' should have success criteria");
      assertNotNull(
          scenario.get("steps"), "Scenario '" + scenario.get("name") + "' should have steps");
    }
  }

  @Test
  @DisplayName("Should include summary in response")
  void testSummaryCriteria() {
    // Arrange
    Map<String, Object> arguments = createBasicArguments();
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    String jsonResult = ((McpSchema.TextContent) result.content().get(0)).text();
    Map<String, Object> response = parseResponse(jsonResult);

    assertNotNull(response.get("summary"), "Response should include summary");
    assertNotNull(response.get("testLevel"), "Response should include testLevel");
    assertNotNull(response.get("recommendations"), "Response should include recommendations");
  }

  @Test
  @DisplayName("Should handle multiple endpoints")
  void testHandleMultipleEndpoints() {
    // Arrange
    Map<String, Object> arguments = createArgumentsWithMultipleEndpoints();
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    assertNotNull(result);
    String jsonResult = ((McpSchema.TextContent) result.content().get(0)).text();
    Map<String, Object> response = parseResponse(jsonResult);

    List<Map<String, Object>> scenarios = (List<Map<String, Object>>) response.get("scenarios");
    assertFalse(scenarios.isEmpty(), "Should generate scenarios for multiple endpoints");

    for (Map<String, Object> scenario : scenarios) {
      List<Map<String, Object>> steps = (List<Map<String, Object>>) scenario.get("steps");
      assertNotNull(steps, "Each scenario should have steps");
    }
  }

  @Test
  @DisplayName("Should return non-empty result content")
  void testResultNotEmpty() {
    // Arrange
    Map<String, Object> arguments = createBasicArguments();
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    assertNotNull(result, "Result should not be null");
    assertFalse(result.content().isEmpty(), "Result content should not be empty");

    McpSchema.Content content = result.content().get(0);
    assertInstanceOf(McpSchema.TextContent.class, content);

    String jsonResult = ((McpSchema.TextContent) content).text();
    assertFalse(jsonResult.isEmpty(), "JSON result should not be empty");
  }

  @Test
  @DisplayName("Should handle case-insensitive test level")
  void testCaseInsensitiveTestLevel() {
    // Arrange
    Map<String, Object> arguments = createArgumentsWithTestLevel("COMPREHENSIVE");
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    assertNotNull(result);
    String jsonResult = ((McpSchema.TextContent) result.content().get(0)).text();
    Map<String, Object> response = parseResponse(jsonResult);

    List<Map<String, Object>> scenarios = (List<Map<String, Object>>) response.get("scenarios");
    assertEquals(5, scenarios.size(), "Should handle uppercase test level");
  }

  @Test
  @DisplayName("Should validate scenario structure")
  void testScenarioStructure() {
    // Arrange
    Map<String, Object> arguments = createBasicArguments();
    when(mockRequest.arguments()).thenReturn(arguments);

    // Act
    McpSchema.CallToolResult result = handler.apply(mockExchange, mockRequest);

    // Assert
    String jsonResult = ((McpSchema.TextContent) result.content().get(0)).text();
    Map<String, Object> response = parseResponse(jsonResult);
    List<Map<String, Object>> scenarios = (List<Map<String, Object>>) response.get("scenarios");

    for (Map<String, Object> scenario : scenarios) {
      assertNotNull(scenario.get("name"), "Scenario should have name");
      assertNotNull(scenario.get("type"), "Scenario should have type");
      assertNotNull(scenario.get("description"), "Scenario should have description");
      assertNotNull(scenario.get("users"), "Scenario should have users count");
      assertNotNull(scenario.get("duration"), "Scenario should have duration");
    }
  }

  // Helper methods

  private Map<String, Object> createBasicArguments() {
    Map<String, Object> arguments = new HashMap<>();
    arguments.put("apiAnalysis", createMockApiAnalysis());
    return arguments;
  }

  private Map<String, Object> createArgumentsWithTestLevel(String testLevel) {
    Map<String, Object> arguments = createBasicArguments();
    arguments.put("testLevel", testLevel);
    return arguments;
  }

  private Map<String, Object> createArgumentsWithMultipleEndpoints() {
    Map<String, Object> arguments = new HashMap<>();
    arguments.put("apiAnalysis", createMockApiAnalysisWithMultipleEndpoints());
    return arguments;
  }

  private Map<String, Object> createMockApiAnalysis() {
    Map<String, Object> apiAnalysis = new HashMap<>();
    List<Map<String, Object>> endpoints = new ArrayList<>();

    endpoints.add(createEndpoint("GET", "/users", "Retrieve users"));
    endpoints.add(createEndpoint("POST", "/users", "Create user"));

    apiAnalysis.put("endpoints", endpoints);
    apiAnalysis.put("title", "Test API");
    apiAnalysis.put("version", "1.0.0");

    return apiAnalysis;
  }

  private Map<String, Object> createMockApiAnalysisWithMultipleEndpoints() {
    Map<String, Object> apiAnalysis = new HashMap<>();
    List<Map<String, Object>> endpoints = new ArrayList<>();

    endpoints.add(createEndpoint("GET", "/users", "Retrieve users"));
    endpoints.add(createEndpoint("POST", "/users", "Create user"));
    endpoints.add(createEndpoint("GET", "/users/{id}", "Get user by ID"));
    endpoints.add(createEndpoint("PUT", "/users/{id}", "Update user"));
    endpoints.add(createEndpoint("DELETE", "/users/{id}", "Delete user"));
    endpoints.add(createEndpoint("GET", "/products", "List products"));
    endpoints.add(createEndpoint("POST", "/orders", "Create order"));

    apiAnalysis.put("endpoints", endpoints);
    apiAnalysis.put("title", "E-commerce API");
    apiAnalysis.put("version", "2.0.0");

    return apiAnalysis;
  }

  private Map<String, Object> createEndpoint(String method, String path, String description) {
    Map<String, Object> endpoint = new HashMap<>();
    endpoint.put("method", method);
    endpoint.put("path", path);
    endpoint.put("description", description);
    endpoint.put("parameters", new ArrayList<>());
    return endpoint;
  }

  private Map<String, Object> parseResponse(String jsonResult) {
    try {
      return objectMapper.readValue(jsonResult, Map.class);
    } catch (Exception e) {
      fail("Failed to parse JSON response: " + e.getMessage());
      return null;
    }
  }
}
