---
description: |
  Common DDD traps when working with LLM agents.
  Use when: reviewing DDD implementations or debugging domain model issues.
---

# Common DDD Traps

## Trap 1: Anemic Domain Model

**Symptom:** Entities have only getters/setters. All business logic is in "Service" classes. The domain model is just a data structure.

**Cause:** LLMs default to the pattern most common in tutorials: Entity = data bag, Service = logic holder. This is the opposite of DDD.

**Fix:**
- Business rules go IN the entity: `order.addItem(product, qty)` not `orderService.addItem(order, product, qty)`
- Entities validate their own invariants in setters/constructors
- If an entity has only getters/setters, it's a DTO, not a domain entity
- Services ORCHESTRATE domain objects, they don't CONTAIN domain logic

---

## Trap 2: Shared Entity Across Bounded Contexts

**Symptom:** `Student` entity is used in both Enrollment and Finance contexts. Changes to one context break the other.

**Cause:** LLM sees "same word = same class" and reuses the entity. DDD requires different models for different contexts.

**Fix:**
- Each bounded context has its OWN `Student` class (different attributes, different behavior)
- Communication between contexts uses DTOs or events, never shared entities
- Use anti-corruption layers at context boundaries

---

## Trap 3: Repository for Every Entity

**Symptom:** `OrderRepository`, `OrderItemRepository`, `OrderItemDiscountRepository` — one repository per table.

**Cause:** LLM maps "entity = table = repository" from its training data (typically Spring Data tutorials).

**Fix:**
- One repository per AGGREGATE ROOT only
- `OrderItem` is part of the `Order` aggregate → accessed through `OrderRepository`
- If you need to query `OrderItem` directly, reconsider your aggregate boundaries

---

## Trap 4: Big Ball of Mud (No Bounded Contexts)

**Symptom:** 50 entities in one package, all referencing each other. No clear boundaries. Every change ripples across the entire model.

**Cause:** LLM creates all entities in one flat structure. No incentive to separate contexts.

**Fix:**
- Start with domain discovery: identify 2-3 bounded contexts BEFORE coding
- Each context gets its own package: `enrollment.model`, `finance.model`, `academic.model`
- Cross-context references use IDs (not entity references): `private CustomerId customerId` not `private Customer customer`
- If two contexts need the same data, they each have their own copy (eventual consistency)

---

## Trap 5: Over-Engineering with CQRS/Event Sourcing

**Symptom:** Agent implements CQRS + Event Sourcing + Saga pattern for a simple CRUD application with 5 entities.

**Cause:** LLM training data includes many DDD "advanced pattern" tutorials. It applies them regardless of complexity.

**Fix:**
- Start with simple DDD: Entities, Value Objects, Repositories, Domain Services
- Add CQRS only when read and write models genuinely diverge
- Add Event Sourcing only when you need audit trail or temporal queries
- Add Sagas only when you have distributed transactions across services
- Rule: if the domain has < 10 aggregates, you almost certainly don't need CQRS/ES
