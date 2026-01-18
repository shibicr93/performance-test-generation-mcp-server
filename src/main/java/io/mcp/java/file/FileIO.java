package io.mcp.java.file;

import java.io.IOException;

public class FileIO {
  public static String getFileContentFromResource(String resource) throws IOException {
    var resourceStream = ClassLoader.getSystemResourceAsStream(resource);
    if (resourceStream == null) {
      throw new IOException("Resource not found:" + resource);
    }
    return new String(resourceStream.readAllBytes());
  }
}
