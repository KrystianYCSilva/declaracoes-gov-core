# ZK Stack Common Traps

## Trap 1: @NotifyChange("*") Performance Bomb
- **What happens**: Every bound property re-evaluates on every command
- **Fix**: Notify only changed properties explicitly: `@NotifyChange({"name","email"})`
- **Detection**: UI sluggishness on forms with many fields

## Trap 2: Background Thread UI Updates
- **What happens**: `NullPointerException` or silent failures when touching components from worker threads
- **Fix**: Use ZK's desktop activation/scheduling API, then update components
- **Rule**: If it touches a component, it MUST run on the event thread

## Trap 3: Session Memory Leaks
- **What happens**: Heap grows per user, eventually OOM
- **Cause**: Storing entity graphs, large lists, or file content in HttpSession
- **Fix**: Store only identity (userId, roles). Persist state in DB. Limit desktops to 3-5.

## Trap 4: Mixing MVC and MVVM in Same Screen
- **What happens**: Unpredictable binding behavior, events not firing
- **Rule**: One screen = one pattern. If you need both, use includes/fragments.

## Trap 5: Testing ZK Composers Directly
- **What happens**: Tests need ZK runtime (Desktop, Execution) which is hard to mock
- **Fix**: Keep composers thin. Test services and DAOs. Use characterization tests for UI flows via Selenium/integration tests.
- **JaCoCo tip**: Exclude `*.composer.*` and `*.viewmodel.*` packages from coverage targets — test business logic in service layer instead.
