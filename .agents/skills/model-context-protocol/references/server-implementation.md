---
name: server-implementation
description: |
  MCP server implementation reference covering transports, capabilities, and protocol-safe runtime behavior.
  Use when: designing an MCP server, debugging server/client negotiation, or checking whether a capability
  should be modeled as a resource, tool, or prompt.
---

# Server Implementation Patterns

## Transport Selection

### stdio (local)
- Messages exchanged via stdin/stdout; newline-delimited JSON-RPC 2.0.
- No HTTP or authentication overhead.
- Use for desktop IDE integrations, local tools, and tightly controlled pipelines.
- Credentials via environment variables when needed.

### HTTP + SSE (remote)
- Tool calls via HTTP POST; streaming results and events via Server-Sent Events (SSE) GET.
- Use when remote access, distributed topology, or multi-client access is required.
- **OAuth 2.1 is mandatory** for any publicly accessible HTTP/SSE endpoint.
- Access tokens arrive as `Authorization: Bearer <token>`; servers must validate audience, expiry, and scope.

## Authentication — OAuth 2.1 (HTTP/SSE only)

1. Client requests endpoint without token → server returns `401` with `WWW-Authenticate` details.
2. Client performs OAuth 2.1 Authorization Code flow with PKCE.
3. Authorization server issues short-lived access token.
4. Client retries request with `Authorization: Bearer <token>`.
5. Server validates token (audience, expiry, scope) and grants or denies access.

Dynamic client registration (RFC 7591) and server discovery via `.well-known/openid-configuration`
are supported for ecosystem-compatible deployments.

## Capability Negotiation

During the initialization handshake, both sides declare what they support:

- `resources`: server can expose readable data objects
- `tools`: server exposes callable functions
- `prompts`: server provides reusable prompt templates
- `subscriptions`: server supports resource change notifications
- `experimental`: opt-in capabilities not yet stable in the spec

Clients must not assume unsupported capabilities are available.
Servers must reject invocations for capabilities they did not declare.

## Version Negotiation

- Include `mcp_version` in the initialization message (e.g., `"2025-11"`).
- If the versions are incompatible, attempt to downgrade to a mutually supported version.
- If no shared version exists, reject the connection with a clear error.

## Server Discovery

- **File-based (local)**: server publishes tool schemas as JSON files in a known directory.
- **Registry-based (remote)**: server registers endpoint metadata including supported tools,
  transport type, and authentication requirements for dynamic client discovery.
- For OAuth-protected servers, advertise the authorization server via `/.well-known/openid-configuration`
  or `/.well-known/oauth-authorization-server` (RFC 8414).

## Safety Defaults

1. Keep tool inputs typed and validated before execution.
2. Keep outputs small, structured, and paginated when the result set is large.
3. Gate any destructive or irreversible operation with explicit approval and confirmation.
4. Never write server logs or debug output to the protocol stream (stdout for stdio, SSE channel for HTTP).
5. For HTTP/SSE: enforce TLS; reject unauthenticated or incorrectly scoped requests immediately.
