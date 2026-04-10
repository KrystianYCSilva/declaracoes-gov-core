---
name: sources
description: |
  Official Model Context Protocol documentation and ecosystem references. Use when: implementing MCP servers or connecting agents to external data sources.
Last verified: 2025-07-09
---
# Sources

Official sources for the Model Context Protocol (2025 spec and ecosystem).

## Core Specification

- MCP home: `https://modelcontextprotocol.io/`
- MCP introduction: `https://modelcontextprotocol.io/introduction`
- MCP specification (latest): `https://modelcontextprotocol.io/specification`
- MCP specification (2025-03-26 versioned): `https://modelcontextprotocol.io/specification/2025-03-26`
- MCP GitHub organization: `https://github.com/modelcontextprotocol`

## Transports

- MCP transports overview (stdio vs HTTP SSE):
  `https://modelcontextprotocol.io/docs/concepts/transports`
- stdio transport: local/lab use; messages via stdin/stdout, newline-delimited JSON-RPC.
  No HTTP overhead. Used in desktop IDE integrations.
- HTTP + SSE transport: remote/async use. Tool calls via HTTP POST; streaming events via SSE GET.
  Requires OAuth 2.1 authentication for protected endpoints.

## Authentication — OAuth 2.1

- MCP authorization framework (OAuth 2.1):
  `https://modelcontextprotocol.io/specification/2025-03-26/basic/authorization`
- OAuth 2.1 deepwiki for MCP:
  `https://deepwiki.com/modelcontextprotocol/modelcontextprotocol/3.1-oauth-2.1-authorization-framework`
- OAuth 2.1 brings: PKCE, dynamic client registration (RFC 7591), short-lived tokens,
  `.well-known/openid-configuration` discovery, token introspection (RFC 7662).
- For stdio/local connections: environment-based credentials; no OAuth required.
- For HTTP/SSE connections: Bearer token in `Authorization` header; server validates audience, expiry, scope.

## Server Discovery

- MCP server registry concept: `https://modelcontextprotocol.io/docs/concepts/architecture`
- Servers may advertise capabilities via `.well-known/` endpoints (RFC 8414 / RFC 9728).
- Local file-based discovery: server exposes tool schemas as JSON files in a known directory.

## SDKs

- TypeScript SDK: `https://github.com/modelcontextprotocol/typescript-sdk`
- Python SDK: `https://github.com/modelcontextprotocol/python-sdk`
- Java SDK: `https://github.com/modelcontextprotocol/java-sdk`

## Version Negotiation

- Clients and servers exchange `mcp_version` during initialization (e.g. `"2025-11"`).
- On major-version mismatch, either downgrade to a mutually supported version or reject the connection.

## Usage Notes

- Use the versioned spec URL (`/specification/2025-03-26`) when asserting advanced protocol behavior.
- Prefer stdio for local tools; use HTTP/SSE only when remote access or distributed topology is required.
- OAuth 2.1 is mandatory for HTTP/SSE transports; do not expose unauthenticated MCP endpoints to the network.
