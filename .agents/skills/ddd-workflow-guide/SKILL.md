---
name: ddd-workflow-guide
description: |
  Guide Domain-Driven Design implementation with bounded contexts, aggregates, repositories, and ubiquitous language.
  Use when: modeling complex business domains, defining bounded contexts, designing aggregates, implementing repository pattern, or establishing ubiquitous language in a project.
activation: Manual
estimated_tokens: 1470
---

# DDD Workflow Guide

This skill provides operational patterns for applying Domain-Driven Design. Not theory — actionable steps for LLM agents implementing DDD in real projects.

## How to Discover the Domain

Before writing any code, understand the business domain:

1. **Identify the actors** — who uses the system and what do they need?
2. **Map the processes** — what are the key business workflows?
3. **Find the nouns** — entities, value objects, aggregates
4. **Find the verbs** — domain events, commands, queries
5. **Define the language** — every term must mean ONE thing (ubiquitous language)

Load `templates/domain-discovery-template.md` for a structured domain discovery worksheet.

## How to Define Bounded Contexts

A bounded context is a boundary where a domain model is valid. Different contexts can use the same word with different meanings.

```
Context: Enrollment             Context: Finance
┌──────────────────────┐       ┌──────────────────────┐
│ Student               │       │ Student               │
│  - name               │       │  - name               │
│  - ra (registration)  │       │  - cpf (tax id)       │
│  - course             │       │  - balance             │
│  - enrollments[]      │       │  - payments[]          │
└──────────────────────┘       └──────────────────────┘
Same word "Student" → different models → different contexts
```

**Rules:**
- Each bounded context has its own model (no sharing entities across contexts)
- Communication between contexts: via events, DTOs, or anti-corruption layers
- One team owns one context (Conway's Law alignment)

## How to Design Aggregates

An aggregate is a cluster of related objects treated as a single unit for data changes.

**Aggregate rules:**
1. Each aggregate has ONE root entity (the aggregate root)
2. External references point ONLY to the aggregate root (never to internal entities)
3. Changes to the aggregate go through the root
4. Persist/delete the entire aggregate as a unit

```java
// Order is the aggregate root
public class Order {                    // ← Aggregate Root
    private OrderId id;
    private CustomerId customerId;      // ← Reference by ID, not by entity
    private List<OrderItem> items;      // ← Internal entity, not directly accessible
    private OrderStatus status;

    public void addItem(Product product, int quantity) {
        // Business rule enforced by aggregate root
        if (status != OrderStatus.DRAFT) throw new IllegalStateException("Cannot modify confirmed order");
        items.add(new OrderItem(product.getId(), product.getPrice(), quantity));
    }
}
```

Load `references/aggregate-design-rules.md` for the full set of aggregate design guidelines.

## How to Implement Repository Pattern

Repositories provide a collection-like interface for aggregates. One repository per aggregate root.

```java
public interface OrderRepository {
    Order findById(OrderId id);
    List<Order> findByCustomer(CustomerId customerId);
    void save(Order order);
    void delete(OrderId id);
    // NO: findByOrderItemProductId() — violates aggregate boundary
}
```

**Rules:**
- One repository per aggregate root (no repository for OrderItem)
- Repository interface lives in the DOMAIN layer
- Repository implementation lives in the INFRASTRUCTURE layer
- Never expose database details (no SQL, no HQL in the domain)

## How to Structure the Layers

```
src/main/java/com/example/
├── domain/                    ← Pure business logic, no framework dependencies
│   ├── model/                 ← Entities, Value Objects, Aggregates
│   ├── event/                 ← Domain Events
│   ├── repository/            ← Repository interfaces (ports)
│   └── service/               ← Domain Services (logic that doesn't belong to one entity)
├── application/               ← Use cases, orchestration
│   ├── command/               ← Commands (write operations)
│   ├── query/                 ← Queries (read operations)
│   └── service/               ← Application Services (orchestrate domain objects)
├── infrastructure/            ← Framework + external adapters
│   ├── persistence/           ← Repository implementations (JPA, JDBC)
│   ├── messaging/             ← Event publishers, message consumers
│   └── web/                   ← Controllers, REST endpoints
└── config/                    ← DI, configuration
```

**Dependency rule:** domain → nothing. application → domain. infrastructure → application, domain.

## How to Apply DDD with LLM Agents

LLMs tend to create anemic domain models (entities with only getters/setters, all logic in services). Prevent this:

```
RULE: Business logic lives IN the entity or aggregate root.
Services are for orchestration ONLY — they call domain methods, not implement domain logic.

❌ Bad: orderService.calculateTotal(order)  — logic in service
✅ Good: order.calculateTotal()             — logic in aggregate
```

Load `references/common-traps.md` for the 5 most common DDD failures with LLM agents.
