---
description: |
  Guidelines for designing aggregates in DDD.
  Use when: defining aggregate boundaries, choosing aggregate roots, or reviewing aggregate design.
---

# Aggregate Design Rules

## The 4 Rules of Aggregates

### Rule 1: Protect Business Invariants Inside Aggregate Boundaries

An invariant is a business rule that must ALWAYS be true. The aggregate is responsible for enforcing it.

```java
public class Order {
    public void addItem(ProductId productId, BigDecimal price, int quantity) {
        if (this.status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Cannot modify a confirmed order");
            // Invariant: only draft orders can be modified
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.items.add(new OrderItem(productId, price, quantity));
    }
}
```

### Rule 2: Design Small Aggregates

Prefer small aggregates with few entities. Large aggregates cause:
- Performance problems (loading entire object graph)
- Concurrency conflicts (locking large portions of data)
- Complexity (hard to understand, hard to test)

**Guideline:** An aggregate root + 0-3 internal entities is typical. If you have 5+, reconsider boundaries.

### Rule 3: Reference Other Aggregates by Identity

Never hold a direct object reference to another aggregate. Use the ID.

```java
// ❌ Wrong: direct reference creates tight coupling
public class Order {
    private Customer customer;  // full entity reference
}

// ✅ Right: reference by identity
public class Order {
    private CustomerId customerId;  // just the ID
}
```

### Rule 4: Use Eventual Consistency Across Aggregates

Updates within one aggregate are immediately consistent (one transaction). Updates across aggregates are eventually consistent (domain events).

```java
// When Order is confirmed, Payment aggregate needs to know
// Don't: update Payment inside Order's transaction
// Do: publish OrderConfirmedEvent → Payment subscribes and reacts
public class Order {
    public OrderConfirmedEvent confirm() {
        this.status = OrderStatus.CONFIRMED;
        return new OrderConfirmedEvent(this.id, this.calculateTotal());
    }
}
```

## Aggregate Boundary Checklist

| Question | If YES | If NO |
|----------|--------|-------|
| Does this entity have its own lifecycle? | Separate aggregate | Part of parent aggregate |
| Can this entity exist without the parent? | Separate aggregate | Part of parent aggregate |
| Is this entity referenced directly from outside? | Aggregate root | Internal entity |
| Does changing this entity require validating a parent invariant? | Part of parent aggregate | Consider separate |
| Do I need to load this entity independently for queries? | Consider separate aggregate | Part of parent |

## Value Objects vs Entities

| Aspect | Entity | Value Object |
|--------|--------|-------------|
| Identity | Has unique ID | Identified by attributes |
| Mutability | Mutable (state changes) | Immutable (replace, don't modify) |
| Equality | By ID | By all attributes |
| Example | `Student(id=1, name="John")` | `Money(amount=100, currency="BRL")` |
| Persistence | Own table | Embedded in entity table |

**Rule of thumb:** If you compare two objects by their ID → entity. If by their values → value object.
