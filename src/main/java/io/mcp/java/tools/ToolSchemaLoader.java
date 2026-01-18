package io.mcp.java.tools;

import io.mcp.java.file.FileIO;
import io.modelcontextprotocol.spec.McpError;
import io.modelcontextprotocol.spec.McpSchema;
import java.io.IOException;

/** Responsible for loading tool schema definitions from resources. */
public class ToolSchemaLoader {
  public String loadSchema(String toolName) {
    try {
      return FileIO.getFileContentFromResource("tools/%s.json".formatted(toolName));
    } catch (IOException e) {
      throw McpError.builder(McpSchema.ErrorCodes.INTERNAL_ERROR)
          .message("Failed to load tool definition for tool : %s".formatted(toolName))
          .data(e)
          .build();
    }
  }
}
