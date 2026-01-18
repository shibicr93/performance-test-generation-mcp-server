# Test Generation MCP Server

## Overview

The Test Generation MCP Server is a specialized Model Context Protocol (MCP) server designed to analyze OpenAPI
specifications and generate comprehensive performance test plans. It provides intelligent analysis of API structure,
complexity assessment, and automated generation of test scripts for both Gatling and JMeter frameworks.

## Features

### 🔍 **OpenAPI Analysis**

- Parses OpenAPI 3.x specifications
- Identifies endpoints, methods, and complexity patterns
- Analyzes load characteristics and caching opportunities
- Provides security and performance recommendations

### 📋 **Test Scenario Generation**

- Creates comprehensive test scenarios based on API analysis
- Supports multiple test types: smoke, load, stress, spike, endurance, user journey
- Intelligent workload distribution based on HTTP methods
- Configurable test levels (basic, comprehensive, advanced)

### 🚀 **Gatling Script Generation**

- Generates ready-to-run Gatling simulation files
- Implements realistic load patterns and user journeys
- Includes proper think time and request pacing
- Supports parameterization and data feeds

### ⚡ **JMeter Test Plan Generation**

- Creates JMX test plan files for Apache JMeter
- Configures thread groups based on test scenarios
- Implements HTTP request defaults and header management
- Includes assertions and response validation

### ✅ **Test Coverage Validation**

- Analyzes endpoint coverage completeness
- Validates test type diversity and load pattern coverage
- Provides coverage scoring and improvement recommendations
- Identifies gaps in error scenario testing

## Quick Start

```bash
# Compile and run the server
mvn clean compile
mvn exec:java -Dexec.mainClass="com.performance.mcp.Main"

# Test the server
./test_server.sh
```

## MCP Tools Available

| Tool Name                 | Description                    | Input Parameters                             |
|---------------------------|--------------------------------|----------------------------------------------|
| `analyze_openapi`         | Analyzes OpenAPI specification | `openApiSpec`, `analysisLevel`               |
| `generate_test_scenarios` | Creates test scenarios         | `apiAnalysis`, `testLevel`                   |
| `generate_gatling_tests`  | Generates Gatling simulation   | `scenarios`, `baseUrl`, `packageName`        |
| `generate_jmeter_tests`   | Creates JMeter test plan       | `scenarios`, `baseUrl`, `testPlanName`       |
| `validate_test_coverage`  | Analyzes coverage completeness | `apiAnalysis`, `scenarios`, `validationType` |

## Implementation Status

✅ **Completed**

- Core MCP server implementation with custom protocol handling
- Test scenario generation with multiple types
- Gatling script generation with realistic patterns
- Comprehensive documentation and examples
- Build and testing verification

🪏**In Progress**

- OpenAPI analysis with complexity assessment
- JMeter test plan generation with proper configuration
- Test coverage validation with scoring

---