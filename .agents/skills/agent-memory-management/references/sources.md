---
description: |
  Authoritative sources for agent memory management, including foundational papers, surveys, and practical frameworks.
  Use when: the design needs academic grounding, source-backed terminology, or URLs for referenced papers on memory tiers and retrieval.
Last verified: 2025-07-09
---

# Agent Memory Management — Sources

| Source | Description | Last verified |
|--------|-------------|---------------|
| [MemGPT: Towards LLMs as Operating Systems (Packer et al., 2023)](https://arxiv.org/pdf/2310.08560) | Foundational paper for multi-tier agent memory; introduces virtual context management and the paging model (RAM vs disk for LLMs) | 2025-07-01 |
| [CoALA: Cognitive Architectures for Language Agents (Sumers et al., 2024)](https://arxiv.org/abs/2309.02427) | Systematic vocabulary for working memory, long-term memory, and action space in language agents; primary academic anchor for memory tier terminology | 2025-07-01 |
| [Retrieval-Augmented Generation for Knowledge-Intensive NLP (Lewis et al., 2020)](https://arxiv.org/abs/2005.11401) | Baseline framing for retrieval-backed memory; demonstrates why stuffing all facts into a prompt context fails at scale | 2025-07-01 |
| [Lost in the Middle: How LLMs Use Long Contexts (Liu et al., 2023)](https://arxiv.org/abs/2307.03172) | Shows that retrieval placement and ranking matter; items in the middle of long contexts are often ignored | 2025-07-01 |
| [Generative Agents: Interactive Simulacra of Human Behavior (Park et al., 2023)](https://arxiv.org/abs/2304.03442) | Episodic memory, reflection, and behavior continuity patterns; useful for the episodic tier design | 2025-07-01 |
| [A Survey on Memory Mechanism of LLM-based Agents (Zhang et al., 2024)](https://arxiv.org/abs/2404.13501) | Maps the memory design space: in-context, external, parametric, and collective memory mechanisms with evaluation axes | 2025-07-01 |
| [Memory in the Age of AI Agents: A Survey (Liu et al., 2025)](https://memorypapers.org/) | Comprehensive review of 100+ papers; taxonomy by memory function (factual, experiential, working) and form | 2025-07-01 |
| [Letta — Open-Source Agent Memory Platform](https://github.com/letta-ai/letta) | Production implementation of multi-tier agent memory (core, recall, archival); successor to MemGPT | 2025-07-01 |
| [Survey of AI Agent Memory Frameworks (Graphlit, 2025)](https://www.graphlit.com/blog/survey-of-ai-agent-memory-frameworks) | Practical comparison of Letta, Mem0, CrewAI, Zep, and Cognee across persistence and vector-search strategies | 2025-07-01 |
| [DeepLearning.AI: LLMs as Operating Systems — Agent Memory](https://learn.deeplearning.ai/courses/llms-as-operating-systems-agent-memory/information) | Hands-on course by MemGPT creators; covers multi-tier memory architecture and Letta integration | 2025-07-01 |
| [Model Context Protocol Documentation](https://modelcontextprotocol.io/) | Standard for exposing memory through tool contracts rather than embedding it inline in prompts | 2025-07-01 |
