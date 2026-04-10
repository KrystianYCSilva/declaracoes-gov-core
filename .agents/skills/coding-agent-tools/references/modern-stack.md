---
name: modern-stack
description: |
  Reference for the modern AI coding stack around protocols, orchestration layers, and retrieval systems.
  Use when: comparing ecosystem pieces beyond a single coding assistant,
  or deciding what supporting infrastructure a serious AI coding workflow needs.
---

# Modern AI Coding Stack

The landscape has moved beyond inline completion.
Serious AI coding workflows are layered systems.

## Layer 1: Coding Interface

This is where the human interacts with the model:

- IDE chat or inline editing
- terminal-first coding CLIs
- repository-aware editors and agent shells

This layer determines ergonomics, but not the whole architecture.

## Layer 2: Tool and Protocol Access

This layer determines what the agent can actually inspect or operate:

- shell execution
- repository tooling
- browser or API access
- Model Context Protocol servers

If this layer is vague, the workflow will feel magical until it fails.

## Layer 3: Orchestration

This layer matters when tasks stop being single-turn:

- governed plan-act-review loops
- orchestrator-worker patterns
- review and closure stages

Itzamna, flow, and Spec Kit all live here in different ways.

## Layer 4: Retrieval and Memory

This layer matters when the assistant must retain or fetch more than the immediate prompt:

- repository documentation
- durable workflow state
- RAG or vector-backed recall
- explicit memory artifacts

This layer should stay subordinate to authoritative governance artifacts.

## Primary Reading Anchors

- official documentation for the concrete tool being evaluated
- Model Context Protocol documentation for tool-boundary design
- NIST Secure Software Development Framework for delivery controls
- Google SRE workbook material for rollout and operational safety

## Representative Ecosystems

Keep vendor and framework examples as examples, not as the main taxonomy:

- protocol layer examples: Model Context Protocol and other tool-integration surfaces
- skill and prompt surface examples: vendor-specific skill systems or repository-local skill catalogs
- orchestration examples: LangGraph, AutoGen, CrewAI, and governed in-repo orchestration such as Itzamna
- retrieval examples: Pinecone, Qdrant, Chroma, or repository-local retrieval layers

If a decision depends on a specific vendor, load the vendor-specific reference next instead of bloating this file into a directory of product docs.

## How to Read This Stack

Treat the stack in layers:

1. coding interface
2. tool or protocol layer
3. orchestration layer
4. retrieval and memory layer

A team can be mature at layer 1 without needing all higher layers.
