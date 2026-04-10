---
name: rigorous-research
description: |
  Conduct source-verified research with academic, institutional, and regulatory rigor.
  Use when: answering questions that require authoritative sources, citation-grade evidence,
  policy or compliance analysis, or a clear separation between evidence, interpretation, and opinion.
---

# Rigorous Research

Use this skill to produce fewer, stronger claims with explicit provenance.

Activate when a claim must be traceable to authoritative sources: research questions, compliance analysis, policy guidance, or any output that could be challenged. Do not activate for conversational responses or clearly bounded factual lookups where citation is unnecessary.

## How to Classify Sources

- `T1`: academic and empirical sources, including peer-reviewed papers and clearly flagged preprints
- `T2`: institutional and official operational sources, such as standards bodies, official docs, and public datasets
- `T3`: regulatory and normative sources, such as laws, regulations, rulings, and court decisions

If a source cannot be classified, treat it as context, not evidence.

## How to Choose the Right Tier

- empirical or state-of-the-art question -> start with `T1`
- implementation, standards, or operational guidance -> start with `T2`
- legal, regulatory, or compliance question -> start with `T3`
- mixed question -> combine tiers and state which one carries the most weight

Use `references/sources.md` for canonical portals and search starting points.

## How to Structure the Answer

1. restate the question precisely, including scope or jurisdiction
2. declare the source plan by tier
3. use `web_search` for questions that depend on recency — verify against primary sources before presenting findings
4. present findings with inline attribution
5. list sources in a compact source table
6. state confidence and limitations explicitly

## How to Cite Safely

- prefer primary sources over summaries
- include the tier with each source
- flag preprints, drafts, or superseded materials explicitly
- distinguish evidence from inference with clear wording
- never fabricate or pad weak claims with uncited text

## How to Reject Weak Evidence

- reject sources with no clear author, institution, or date
- treat blogs, social posts, and AI-generated pages as leads, not evidence
- downgrade or reject stale, mirrored, or unverifiable documents
- do not mix jurisdictions without saying so explicitly

## How to Stay Honest

- if no strong source exists, say so directly
- if only a lower-tier source exists, say it is the best available evidence
- if the answer depends on recency, verify against the official portals in `references/sources.md`
