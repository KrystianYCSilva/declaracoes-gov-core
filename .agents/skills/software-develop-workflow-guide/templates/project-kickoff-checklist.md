---
description: |
  Pre-development kickoff checklist. Copy and fill before starting.
  Use when: kicking off a new software development project or feature.
---

# Project Kickoff Checklist

## 1. Understand
- [ ] Problem statement written (1 paragraph)
- [ ] Stakeholders identified
- [ ] Requirements gathered (functional + non-functional)
- [ ] Acceptance criteria defined (Given/When/Then)
- [ ] Scope boundaries set (in-scope / out-of-scope)
- [ ] Methodology chosen (Waterfall / Agile / Hybrid)

## 2. Design
- [ ] Tech stack decided and documented
- [ ] Architecture sketched (components + interactions)
- [ ] Data model drafted (entities + relationships)
- [ ] API contracts defined (if applicable)
- [ ] SDD written (if significant feature) OR ADR (if small decision)
- [ ] Test strategy defined (unit/integration/E2E targets)

## 3. Setup
- [ ] Repository created with proper .gitignore
- [ ] Build tool configured (pom.xml / package.json / go.mod)
- [ ] Test framework configured with coverage plugin
- [ ] CI pipeline created (build + test + coverage gate)
- [ ] AGENTS.md created (< 150 lines)
- [ ] Branch protection enabled (PR required, CI must pass)

## 4. Vertical Slice
- [ ] 1 entity/model created with proper annotations
- [ ] 1 DAO/repository with CRUD
- [ ] 1 service with business logic
- [ ] 1 controller/endpoint (if web)
- [ ] Tests for ALL layers (entity, DAO, service)
- [ ] CI passes with coverage gate

## 5. Ready to Develop
- [ ] All items above completed
- [ ] Team aligned on conventions (code style, naming, patterns)
- [ ] First sprint/iteration planned
- [ ] Definition of Done agreed upon

**Project is ready to start: ✅ / ❌**
