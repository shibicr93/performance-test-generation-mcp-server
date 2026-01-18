package io.mcp.java.tools.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpError;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.*;

public final class SuggestTestScenariosHandler implements ToolHandler {

  @Override
  public McpSchema.CallToolResult apply(
      McpSyncServerExchange exchange, McpSchema.CallToolRequest request) {
    // Implement the logic to suggest test scenarios based on the request
    // For now, return a placeholder result
    try {
      String jsonResult =
          new ObjectMapper().writeValueAsString(buildSuggestions(request.arguments()));
      return McpSchema.CallToolResult.builder()
          .addContent(new McpSchema.TextContent(jsonResult))
          .build();

    } catch (JsonProcessingException e) {
      throw McpError.builder(McpSchema.ErrorCodes.INTERNAL_ERROR)
          .data(e)
          .message("Failed to process test scenario suggestions")
          .build();
    }
  }

  private Object buildSuggestions(Map<String, Object> arguments) {
    @SuppressWarnings("unchecked")
    Map<String, Object> apiAnalysis = (Map<String, Object>) arguments.get("apiAnalysis");
    String testLevel = (String) arguments.getOrDefault("testLevel", "comprehensive");

    List<Map<String, Object>> scenarios = generateTestScenarios(apiAnalysis, testLevel);

    Map<String, Object> result = new HashMap<>();
    result.put("scenarios", scenarios);
    result.put("summary", generateScenarioSummary(scenarios));
    result.put("testLevel", testLevel);
    result.put("recommendations", generateRecommendations(scenarios, apiAnalysis));

    return result;
  }

  private List<Map<String, Object>> generateTestScenarios(
      Map<String, Object> apiAnalysis, String testLevel) {
    List<Map<String, Object>> scenarios = new ArrayList<>();

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> endpoints =
        (List<Map<String, Object>>) apiAnalysis.getOrDefault("endpoints", Collections.emptyList());

    // Always include smoke test
    scenarios.add(generateSmokeTest(endpoints));

    switch (testLevel.toLowerCase()) {
      case "basic" -> scenarios.add(generateBasicLoadTest(endpoints));
      case "comprehensive" -> {
        scenarios.add(generateBasicLoadTest(endpoints));
        scenarios.add(generateStressTest(endpoints));
        scenarios.add(generateSpikeTest(endpoints));
        scenarios.add(generateUserJourneyTest(endpoints));
      }
      case "advanced" -> {
        scenarios.add(generateBasicLoadTest(endpoints));
        scenarios.add(generateStressTest(endpoints));
        scenarios.add(generateSpikeTest(endpoints));
        scenarios.add(generateEnduranceTest(endpoints));
        scenarios.add(generateUserJourneyTest(endpoints));
        scenarios.add(generateVolumeTest(endpoints));
      }
    }

    return scenarios;
  }

  private Map<String, Object> generateSmokeTest(List<Map<String, Object>> endpoints) {
    Map<String, Object> scenario = new HashMap<>();
    scenario.put("name", "API Smoke Test");
    scenario.put("type", "smoke");
    scenario.put(
        "description", "Verify all endpoints are accessible and return expected responses");
    scenario.put("users", 1);
    scenario.put("duration", "2m");
    scenario.put("rampUpDuration", "30s");

    List<Map<String, Object>> steps = new ArrayList<>();

    // Test each endpoint once
    for (Map<String, Object> endpoint : endpoints) {
      Map<String, Object> step = new HashMap<>();
      step.put("method", endpoint.get("method"));
      step.put("path", endpoint.get("path"));
      step.put("description", "Test " + endpoint.get("method") + " " + endpoint.get("path"));
      step.put("expectedStatus", 200);
      step.put("weight", 1);
      steps.add(step);
    }

    scenario.put("steps", steps);
    scenario.put(
        "successCriteria",
        Map.of(
            "errorRate", "< 1%",
            "avgResponseTime", "< 2000ms"));

    return scenario;
  }

  private Map<String, Object> generateBasicLoadTest(List<Map<String, Object>> endpoints) {
    Map<String, Object> scenario = new HashMap<>();
    scenario.put("name", "Basic Load Test");
    scenario.put("type", "load");
    scenario.put("description", "Test API performance under normal expected load");
    scenario.put("users", 10);
    scenario.put("duration", "10m");
    scenario.put("rampUpDuration", "2m");

    List<Map<String, Object>> steps = generateWeightedSteps(endpoints, "load");
    scenario.put("steps", steps);

    scenario.put(
        "successCriteria",
        Map.of(
            "errorRate", "< 1%",
            "avgResponseTime", "< 1000ms",
            "p95ResponseTime", "< 2000ms"));

    return scenario;
  }

  private Map<String, Object> generateStressTest(List<Map<String, Object>> endpoints) {
    Map<String, Object> scenario = new HashMap<>();
    scenario.put("name", "Stress Test");
    scenario.put("type", "stress");
    scenario.put("description", "Test API behavior at or beyond normal capacity");
    scenario.put("users", 50);
    scenario.put("duration", "15m");
    scenario.put("rampUpDuration", "5m");

    List<Map<String, Object>> steps = generateWeightedSteps(endpoints, "stress");
    scenario.put("steps", steps);

    scenario.put(
        "successCriteria",
        Map.of(
            "errorRate", "< 5%",
            "avgResponseTime", "< 3000ms",
            "systemStability", "no crashes"));

    return scenario;
  }

  private Map<String, Object> generateSpikeTest(List<Map<String, Object>> endpoints) {
    Map<String, Object> scenario = new HashMap<>();
    scenario.put("name", "Spike Test");
    scenario.put("type", "spike");
    scenario.put("description", "Test API response to sudden traffic spikes");
    scenario.put("users", 20);
    scenario.put("spikeUsers", 100);
    scenario.put("duration", "5m");
    scenario.put("rampUpDuration", "30s");

    List<Map<String, Object>> steps = generateWeightedSteps(endpoints, "spike");
    scenario.put("steps", steps);

    scenario.put(
        "successCriteria",
        Map.of(
            "errorRate", "< 10%",
            "recoveryTime", "< 60s",
            "systemRecovery", "returns to baseline"));

    return scenario;
  }

  private Map<String, Object> generateEnduranceTest(List<Map<String, Object>> endpoints) {
    Map<String, Object> scenario = new HashMap<>();
    scenario.put("name", "Endurance Test");
    scenario.put("type", "endurance");
    scenario.put("description", "Test API stability over extended periods");
    scenario.put("users", 25);
    scenario.put("duration", "2h");
    scenario.put("rampUpDuration", "5m");

    List<Map<String, Object>> steps = generateWeightedSteps(endpoints, "endurance");
    scenario.put("steps", steps);

    scenario.put(
        "successCriteria",
        Map.of(
            "errorRate", "< 2%",
            "memoryLeaks", "none detected",
            "performanceDegradation", "< 10%"));

    return scenario;
  }

  private Map<String, Object> generateUserJourneyTest(List<Map<String, Object>> endpoints) {
    Map<String, Object> scenario = new HashMap<>();
    scenario.put("name", "User Journey Test");
    scenario.put("type", "user_journey");
    scenario.put("description", "Simulate realistic user workflows and interactions");
    scenario.put("users", 15);
    scenario.put("duration", "20m");
    scenario.put("rampUpDuration", "3m");

    List<Map<String, Object>> steps = generateUserJourneySteps(endpoints);
    scenario.put("steps", steps);
    scenario.put("thinkTime", Map.of("min", 1000, "max", 3000));

    scenario.put(
        "successCriteria",
        Map.of(
            "errorRate", "< 1%",
            "journeyCompletionRate", "> 95%",
            "avgResponseTime", "< 1500ms"));

    return scenario;
  }

  private Map<String, Object> generateVolumeTest(List<Map<String, Object>> endpoints) {
    Map<String, Object> scenario = new HashMap<>();
    scenario.put("name", "Volume Test");
    scenario.put("type", "volume");
    scenario.put("description", "Test API with large amounts of data");
    scenario.put("users", 30);
    scenario.put("duration", "30m");
    scenario.put("rampUpDuration", "5m");

    List<Map<String, Object>> steps = generateWeightedSteps(endpoints, "volume");
    scenario.put("steps", steps);
    scenario.put("dataSize", "large");

    scenario.put(
        "successCriteria",
        Map.of(
            "errorRate", "< 2%",
            "dataIntegrity", "100%",
            "avgResponseTime", "< 5000ms"));

    return scenario;
  }

  private List<Map<String, Object>> generateWeightedSteps(
      List<Map<String, Object>> endpoints, String testType) {
    List<Map<String, Object>> steps = new ArrayList<>();

    for (Map<String, Object> endpoint : endpoints) {
      Map<String, Object> step = new HashMap<>();
      step.put("method", endpoint.get("method"));
      step.put("path", endpoint.get("path"));
      step.put("description", "Test " + endpoint.get("method") + " " + endpoint.get("path"));
      step.put("expectedStatus", 200);

      // Assign weights based on HTTP method and test type
      int weight = calculateStepWeight((String) endpoint.get("method"), testType);
      step.put("weight", weight);

      steps.add(step);
    }

    return steps;
  }

  private List<Map<String, Object>> generateUserJourneySteps(List<Map<String, Object>> endpoints) {
    List<Map<String, Object>> steps = new ArrayList<>();

    // Common user journey: Authentication -> Read operations -> Write operations -> Cleanup
    List<String> journeyOrder = List.of("GET", "POST", "PUT", "PATCH", "DELETE");

    // Group endpoints by method
    Map<String, List<Map<String, Object>>> endpointsByMethod = new HashMap<>();
    for (Map<String, Object> endpoint : endpoints) {
      String method = (String) endpoint.get("method");
      endpointsByMethod.computeIfAbsent(method, k -> new ArrayList<>()).add(endpoint);
    }

    // Create journey steps following realistic patterns
    for (String method : journeyOrder) {
      List<Map<String, Object>> methodEndpoints =
          endpointsByMethod.getOrDefault(method, Collections.emptyList());
      for (Map<String, Object> endpoint : methodEndpoints) {
        Map<String, Object> step = new HashMap<>();
        step.put("method", endpoint.get("method"));
        step.put("path", endpoint.get("path"));
        step.put(
            "description", "Journey step: " + endpoint.get("method") + " " + endpoint.get("path"));
        step.put("expectedStatus", 200);
        step.put("weight", 1);
        steps.add(step);
      }
    }

    return steps;
  }

  private int calculateStepWeight(String method, String testType) {
    // Base weights by HTTP method
    Map<String, Integer> baseWeights =
        Map.of(
            "GET", 5,
            "POST", 2,
            "PUT", 1,
            "PATCH", 1,
            "DELETE", 1);

    int weight = baseWeights.getOrDefault(method, 1);

    // Adjust weight based on test type
    switch (testType) {
      case "load", "endurance" -> {
        // Favor read operations in sustained tests
        if ("GET".equals(method)) weight += 2;
      }
      case "stress", "spike" -> {
        // Increase write operations in stress scenarios
        if (List.of("POST", "PUT", "PATCH").contains(method)) weight += 1;
      }
      case "volume" -> {
        // Focus on data operations
        if (List.of("POST", "PUT").contains(method)) weight += 3;
      }
    }

    return weight;
  }

  private Map<String, Object> generateScenarioSummary(List<Map<String, Object>> scenarios) {
    Map<String, Object> summary = new HashMap<>();
    summary.put("totalScenarios", scenarios.size());
    summary.put("testTypes", scenarios.stream().map(s -> s.get("type")).distinct().toList());

    int totalSteps =
        scenarios.stream()
            .mapToInt(
                s -> {
                  @SuppressWarnings("unchecked")
                  List<Map<String, Object>> steps = (List<Map<String, Object>>) s.get("steps");
                  return steps != null ? steps.size() : 0;
                })
            .sum();
    summary.put("totalTestSteps", totalSteps);

    String estimatedDuration = estimateTotalTestDuration(scenarios);
    summary.put("estimatedDuration", estimatedDuration);

    return summary;
  }

  private String estimateTotalTestDuration(List<Map<String, Object>> scenarios) {
    // Sum up scenario durations (simplified calculation)
    int totalMinutes =
        scenarios.stream()
            .mapToInt(
                s -> {
                  String duration = (String) s.getOrDefault("duration", "5m");
                  return parseDurationToMinutes(duration);
                })
            .sum();

    return totalMinutes + " minutes (sequential execution)";
  }

  private int parseDurationToMinutes(String duration) {
    if (duration == null) return 5;

    duration = duration.toLowerCase().trim();

    if (duration.endsWith("s")) {
      return (int) Math.ceil(Integer.parseInt(duration.replace("s", "")) / 60.0);
    } else if (duration.endsWith("m")) {
      return Integer.parseInt(duration.replace("m", ""));
    } else if (duration.endsWith("h")) {
      return Integer.parseInt(duration.replace("h", "")) * 60;
    }

    return 5; // default
  }

  private List<String> generateRecommendations(
      List<Map<String, Object>> scenarios, Map<String, Object> apiAnalysis) {
    List<String> recommendations = new ArrayList<>();

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> endpoints =
        (List<Map<String, Object>>) apiAnalysis.getOrDefault("endpoints", Collections.emptyList());

    if (endpoints.size() > 20) {
      recommendations.add(
          "Consider breaking large API into smaller test suites for better maintainability");
    }

    if (scenarios.stream().anyMatch(s -> "endurance".equals(s.get("type")))) {
      recommendations.add("Monitor memory usage during endurance tests to detect leaks");
    }

    if (scenarios.stream().anyMatch(s -> "spike".equals(s.get("type")))) {
      recommendations.add("Implement circuit breakers and rate limiting for spike resilience");
    }

    long writeOperations =
        endpoints.stream()
            .filter(e -> List.of("POST", "PUT", "PATCH", "DELETE").contains(e.get("method")))
            .count();

    if (writeOperations > endpoints.size() * 0.5) {
      recommendations.add(
          "High number of write operations detected - consider testing data consistency");
    }

    recommendations.add("Run tests in isolated environment to avoid affecting production data");
    recommendations.add("Consider implementing test data setup and teardown procedures");
    recommendations.add("Monitor database performance and connection pools during tests");

    return recommendations;
  }
}
