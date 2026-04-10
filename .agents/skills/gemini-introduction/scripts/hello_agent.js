#!/usr/bin/env node
/**
 * Minimal deterministic helper example for a Gemini-compatible skill.
 *
 * Use scripts like this when the task benefits from structured execution rather
 * than longer model-generated prose.
 *
 * Guidelines:
 * - Print compact, easy-to-parse output.
 * - Prefer JSON or short summaries.
 * - Page large outputs instead of dumping everything.
 */

const args = process.argv.slice(2);

if (args.includes('--help')) {
  console.log('Name: hello_agent.js');
  console.log('Description: Example helper script for a skill that returns optimized output to the LLM.');
  process.exit(0);
}

const input = args[0] || 'User/Agent';

function runDeterministicOperation(name) {
  return {
    ok: true,
    subject: name,
    message: 'Deterministic work was executed outside model generation for better safety and repeatability.',
    next_steps: 'Read the structured output and continue the workflow from the chat context.'
  };
}

console.log(JSON.stringify(runDeterministicOperation(input), null, 2));
