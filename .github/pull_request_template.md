---
name: Pull Request
about: Propose changes or new performance test tools.
title: "[PR] "
---

## Description

Clear description of the changes. If this adds a new performance framework (e.g., k6, Locust), explain the
implementation.

Fixes # (issue number)

## Type of Change

- [ ] 🐛 Bug fix
- [ ] ✨ New feature (MCP Tool/Resource)
- [ ] 📝 Documentation update
- [ ] 🚀 Performance optimization

## How Has This Been Tested?

Please describe the tests you ran to verify your changes.

- [ ] **Unit Tests:** `mvn test` output.
- [ ] **MCP Inspector:** Verified tool definition and response.
- [ ] **Integration:** Tested via [Claude Desktop](https://modelcontextprotocol.io) or another client.

## Checklist

- [ ] My code follows the [Google Java Style Guide](https://google.github.io).
- [ ] I have commented my code, particularly in `@Tool` descriptions.
- [ ] I have added tests that prove my fix is effective or that my feature works.
- [ ] New and existing unit tests pass locally with my changes.
