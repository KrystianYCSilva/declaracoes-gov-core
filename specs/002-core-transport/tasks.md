# Tasks: Core Transport Module

**Input**: Design documents from `/specs/002-core-transport/`
**Prerequisites**: [plan.md](plan.md), [spec.md](spec.md), [data-model.md](data-model.md), [contracts/](contracts/), [research.md](research.md), [quickstart.md](quickstart.md)

**Tests**: Included — the spec mandates test-driven delivery and 90% JaCoCo coverage.

**Organization**: Tasks grouped by user story to enable independent implementation and testing.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Initialize the new Maven module and verify the Java 8 baseline.

- [X] T001 Create `declaracoes-gov-core-transport/pom.xml` with Java 8 source/target, parent reactor reference, and `core-crypto` + `core-domain` dependencies
- [X] T002 [P] Add Apache HttpClient 5.2.6 dependency (`org.apache.httpcomponents.client5:httpclient5`) and Wiremock test dependency
- [X] T003 [P] Add `declaracoes-gov-core-transport` module to root `pom.xml` reactor (order: domain → format → crypto → xml → **transport** → bom)
- [X] T004 Update `declaracoes-gov-core-bom/pom.xml` to include `declaracoes-gov-core-transport` artifact with version property
- [X] T005 Run `mvn -B verify` from repo root to confirm module compiles (no sources yet) and reactor order is correct

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Immutable value objects and exception hierarchy required by ALL user stories.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

### Tests (Write First — Must Fail)

- [X] T006 [P] Create `HttpRequestTest.java` — builder validation, immutability, header case-insensitivity
- [X] T007 [P] Create `HttpResponseTest.java` — construction, field access, empty body handling
- [X] T008 [P] Create `ProxyConfigTest.java` — builder validation, user/pass pairing rule, port range
- [X] T009 [P] Create `TransportExceptionTest.java` — hierarchy, cause chaining, `TransportTimeoutException`, `TransportSecurityException`

### Implementation

- [X] T010 [P] Create `HttpRequest.java` with builder pattern in `src/main/java/br/uem/npd/govcore/transport/`
- [X] T011 [P] Create `HttpResponse.java` in `src/main/java/br/uem/npd/govcore/transport/`
- [X] T012 [P] Create `ProxyConfig.java` with builder pattern in `src/main/java/br/uem/npd/govcore/transport/`
- [X] T013 Create `TransportException.java` (checked) + `TransportTimeoutException.java` + `TransportSecurityException.java` in `src/main/java/br/uem/npd/govcore/transport/`
- [X] T014 Run `mvn -B verify` in the transport module to confirm foundational tests pass and JaCoCo baseline is tracked

**Checkpoint**: Foundation ready — `HttpRequest`, `HttpResponse`, `ProxyConfig`, `TransportException` are immutable, tested, and compile on Java 8.

---

## Phase 3: User Story 1 — Neutral HTTP Transport SPI (Priority: P1) 🎯 MVP

**Goal**: Define the `RestTransport` SPI and prove it can be mocked in under 50 lines without referencing Apache HttpClient.

**Independent Test**: A consumer can compile and run using only `transport` package classes; mock implementation compiles and satisfies `RestTransport` contract.

### Tests for User Story 1 (Write First — Must Fail)

- [X] T015 [P] [US1] Create `MockRestTransportTest.java` — verify a mock `RestTransport` under 50 LOC compiles and returns an `HttpResponse`
- [X] T016 [P] [US1] Create `RestTransportContractTest.java` — enforce that `RestTransport` extends `Closeable` and `execute()` throws `TransportException`

### Implementation for User Story 1

- [X] T017 [US1] Create `RestTransport.java` SPI interface in `src/main/java/br/uem/npd/govcore/transport/`
- [X] T018 [US1] Ensure `RestTransport` package does NOT import any `org.apache.hc.client5` or `org.apache.http` classes

**Checkpoint**: User Story 1 is independently testable. Mock transport works without Apache dependency. SC-003 satisfied.

---

## Phase 4: User Story 2 — Apache HttpClient 5 Default Implementation (Priority: P2)

**Goal**: Provide `ApacheHttpClientRestTransport` with mTLS, proxy, timeout, and User-Agent support.

**Independent Test**: Integration tests with embedded Wiremock verify proxy routing, timeout exceptions, custom User-Agent headers, and mTLS certificate presentation.

### Tests for User Story 2 (Write First — Must Fail)

- [X] T019 [P] [US2] Create `ApacheHttpClientRestTransportIT.java` — Wiremock tests for GET/POST, 200 OK, 503 response, custom User-Agent header presence
- [X] T020 [P] [US2] Create `TimeoutIT.java` — verify `TransportTimeoutException` on delayed Wiremock response exceeding configured timeout
- [X] T021 [P] [US2] Create `ProxyRoutingIT.java` — verify traffic routes through Wiremock-configured proxy
- [X] T022 [P] [US2] Create `MutualTlsIT.java` — use `core-crypto` test-jar fixtures to configure test keystore and verify TLS handshake (or mock at socket layer if embedded server mTLS is infeasible)

### Implementation for User Story 2

- [X] T023 [US2] Create `ApacheHttpClientRestTransport.java` in `src/main/java/br/uem/npd/govcore/transport/apache/` with `Builder` inner class
- [X] T024 [US2] Implement request conversion (`HttpRequest` → Apache `ClassicHttpRequest`)
- [X] T025 [US2] Implement response conversion (Apache `ClassicHttpResponse` → `HttpResponse`)
- [X] T026 [US2] Wire `CertificateProvider` into `SSLContext` via `core-crypto` `SslContextBuilder`
- [X] T027 [US2] Wire `ProxyConfig` into Apache `HttpHost` and `CredentialsProvider`
- [X] T028 [US2] Configure connection timeout and read timeout via Apache `RequestConfig`
- [X] T029 [US2] Inject custom `User-Agent` header into default request headers
- [X] T030 [US2] Implement `close()` to shut down `CloseableHttpClient` and connection manager
- [X] T031 [US2] Add JaCoCo exclusion in `declaracoes-gov-core-transport/pom.xml` for `ApacheHttpClientRestTransport` with XML comment justification

**Checkpoint**: User Story 2 is independently testable via Wiremock. Proxy, timeout, User-Agent, and mTLS are verified. SC-001, SC-004 satisfied.

---

## Phase 5: User Story 3 — Composable Retry Policy (Priority: P3)

**Goal**: Provide a `RetryPolicy` SPI and `ExponentialBackoff` implementation that wraps `RestTransport`.

**Independent Test**: Mock transport fails N times before succeeding; retry policy executes correct number of attempts with expected delays. No-retry policy fails immediately.

### Tests for User Story 3 (Write First — Must Fail)

- [X] T032 [P] [US3] Create `ExponentialBackoffTest.java` — delay calculation formula, cap at `maxDelay`, respect `retryableStatusCodes`
- [X] T033 [P] [US3] Create `RetryPolicyIntegrationTest.java` — mock `RestTransport` that throws on first 2 calls then succeeds; verify 3 total attempts
- [X] T034 [P] [US3] Create `NoRetryPolicyTest.java` — verify immediate failure when `maxAttempts == 1`

### Implementation for User Story 3

- [X] T035 [US3] Create `RetryPolicy.java` SPI interface in `src/main/java/br/uem/npd/govcore/transport/`
- [X] T036 [US3] Create `ExponentialBackoff.java` in `src/main/java/br/uem/npd/govcore/transport/`
- [X] T037 [US3] Implement retry wrapper logic inside `ApacheHttpClientRestTransport` (or decorator) that consults `RetryPolicy.shouldRetry()` and `delayMillis()`
- [X] T038 [US3] Ensure retry wrapper does NOT retry on non-retryable status codes (e.g., 400, 404) per `ExponentialBackoff` defaults

**Checkpoint**: User Story 3 is independently testable. Retry delays and attempt counts are deterministic. SC-002 (coverage for policy classes) on track.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Coverage gates, documentation, BOM alignment, and memory sync.

- [X] T039 [P] Run `mvn -B verify` in the transport module and confirm JaCoCo line ≥ 90% and branch ≥ 90% for non-excluded classes
- [X] T040 [P] Add Javadoc in Portuguese for all public API classes (`HttpRequest`, `HttpResponse`, `ProxyConfig`, `RestTransport`, `RetryPolicy`, `ExponentialBackoff`, `TransportException` hierarchy)
- [X] T041 [P] Add AI-facing English documentation in `.context/knowledge/` or update `.context/README.md` referencing the new transport module
- [X] T042 Update `declaracoes-gov-core-bom/pom.xml` publication order: domain → format → crypto → xml → **transport** → bom (verify SC-005)
- [X] T043 [P] Update root `MEMORY.md` to mark Feature 002 as completed
- [X] T044 [P] Sync `.kimi/memory/agent-local-memory.md` with final implementation notes
- [X] T045 Validate `quickstart.md` examples compile against the final API (copy to a scratch test if needed)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — can start immediately.
- **Foundational (Phase 2)**: Depends on Setup completion — BLOCKS all user stories.
- **User Stories (Phase 3–5)**: All depend on Foundational phase completion.
  - US1, US2, US3 can proceed sequentially or in parallel after Foundation.
  - Recommended: US1 → US2 → US3 (SPI first, then adapter, then policy).
- **Polish (Phase 6)**: Depends on all desired user stories being complete.

### User Story Dependencies

- **User Story 1 (P1)**: No dependencies on other stories. Must complete first to establish SPI.
- **User Story 2 (P2)**: Depends on US1 (`RestTransport` interface). Can start in parallel with US1 tests if interface is stable, but safer after US1 checkpoint.
- **User Story 3 (P3)**: Depends on US1 (`RestTransport`, `TransportException`). Optionally depends on US2 if retry wrapper is implemented as a decorator around `ApacheHttpClientRestTransport`; can also wrap a mock for independent testing.

### Within Each User Story

- Tests MUST be written and FAIL before implementation.
- SPI interfaces before implementations.
- Value objects before services/adapters.
- Core implementation before integration tests.
- Story complete before moving to next priority.

### Parallel Opportunities

- All Setup tasks (T001–T005) marked [P] can run in parallel.
- All Foundational tests (T006–T009) marked [P] can run in parallel.
- All Foundational implementations (T010–T012) marked [P] can run in parallel.
- US1 tests (T015–T016) marked [P] can run in parallel.
- US2 integration tests (T019–T022) marked [P] can run in parallel (each uses separate Wiremock ports).
- US3 tests (T032–T034) marked [P] can run in parallel.
- Polish documentation tasks (T040–T041) marked [P] can run in parallel.

---

## Parallel Example: User Story 2

```bash
# Launch all integration tests for User Story 2 together:
Task: "ApacheHttpClientRestTransportIT.java — Wiremock core behavior"
Task: "TimeoutIT.java — delayed response timeout"
Task: "ProxyRoutingIT.java — proxy routing"
Task: "MutualTlsIT.java — mTLS handshake"

# Launch implementation pieces in parallel after tests exist:
Task: "Request conversion logic in ApacheHttpClientRestTransport"
Task: "Response conversion logic in ApacheHttpClientRestTransport"
Task: "ProxyConfig wiring into Apache HttpClient builder"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (immutable value objects + exceptions)
3. Complete Phase 3: User Story 1 (`RestTransport` SPI + mock test < 50 LOC)
4. **STOP and VALIDATE**: Confirm SC-003 (mockability) and SC-001 (Java 8 compile)
5. Merge to `feature/002-core-transport` if ready for early review

### Incremental Delivery

1. Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Checkpoint (SPI stable)
3. Add User Story 2 → Test independently → Checkpoint (adapter verified)
4. Add User Story 3 → Test independently → Checkpoint (retry verified)
5. Polish → 90% JaCoCo, docs, BOM sync → Final merge

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together.
2. Once Foundational is done:
   - Developer A: US1 (SPI interface + mock test)
   - Developer B: US2 (Apache adapter + Wiremock ITs)
   - Developer C: US3 (RetryPolicy + ExponentialBackoff)
3. US2 and US3 developers agree on `RestTransport` interface contract from US1.
4. Stories integrate independently; no cross-story file conflicts.

---

## Notes

- [P] tasks = different files, no dependencies.
- [Story] label maps task to specific user story for traceability.
- Each user story is independently completable and testable.
- Verify tests fail before implementing (red-green-refactor).
- Commit after each task or logical group.
- Stop at any checkpoint to validate story independently.
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence.
