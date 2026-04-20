"""Skill compiler: distributes skills from ~/.agents/skills/ to CLI-native directories.

Reads SKILL.md files from the canonical source (~/.agents/skills/) and copies them
to each CLI's official skill directory, adapting variable syntax as needed.

This replaces the need for each CLI to reference ~/.agents/ directly — instead,
skills are compiled into each CLI's native format.

Usage:
    python compile_skills.py                    # Compile all skills to all CLIs
    python compile_skills.py --clis copilot claude  # Compile to specific CLIs
    python compile_skills.py --skills brownfield-refactoring  # Compile specific skill
    python compile_skills.py --dry-run          # Preview without writing
"""

from __future__ import annotations

import argparse
import os
import re
import shutil
from pathlib import Path


# Official CLI skill directory mappings (project-level, relative to project root)
# For global skills, these go under ~/.{cli}/skills/
CLI_CONFIG: dict[str, dict[str, str]] = {
    'copilot': {
        'global_skills_dir': '.github/skills',
        'home_dir': '.copilot',
        'var_syntax': '$ARGUMENTS',
    },
    'claude': {
        'global_skills_dir': '.claude/skills',
        'home_dir': '.claude',
        'var_syntax': '$ARGUMENTS',
    },
    'gemini': {
        'global_skills_dir': '.gemini/skills',
        'home_dir': '.gemini',
        'var_syntax': '{{args}}',
    },
    'codex': {
        'global_skills_dir': '.codex/skills',
        'home_dir': '.codex',
        'var_syntax': '$ARGUMENTS',
    },
    'cursor': {
        'global_skills_dir': '.cursor/skills',
        'home_dir': '.cursor',
        'var_syntax': '$ARGUMENTS',
    },
    'qwen': {
        'global_skills_dir': '.qwen/skills',
        'home_dir': '.qwen',
        'var_syntax': '{{args}}',
    },
    'opencode': {
        'global_skills_dir': '.opencode/skills',
        'home_dir': '.opencode',
        'var_syntax': '$ARGUMENTS',
    },
}

# Variable syntax patterns to translate between CLIs
VAR_PATTERNS = {
    '$ARGUMENTS': re.compile(r'\$ARGUMENTS'),
    '{{args}}': re.compile(r'\{\{args\}\}'),
}


def translate_variables(content: str, source_syntax: str, target_syntax: str) -> str:
    """Translate variable placeholders between CLI syntaxes."""
    if source_syntax == target_syntax:
        return content
    for syntax, pattern in VAR_PATTERNS.items():
        if syntax != target_syntax:
            content = pattern.sub(target_syntax, content)
    return content


def detect_var_syntax(content: str) -> str:
    """Detect which variable syntax a SKILL.md uses."""
    if '{{args}}' in content:
        return '{{args}}'
    return '$ARGUMENTS'


def compile_skills(
    source_dir: Path,
    target_base: Path,
    clis: list[str] | None = None,
    skills: list[str] | None = None,
    dry_run: bool = False,
) -> dict[str, list[str]]:
    """Compile skills from canonical source to CLI-native directories.

    Args:
        source_dir: Path to canonical skills (e.g., ~/.agents/skills/)
        target_base: Base path for output (e.g., ~ for global, or project root)
        clis: Subset of CLIs to compile for. None = all.
        skills: Subset of skills to compile. None = all.
        dry_run: If True, only report what would be done.

    Returns:
        Dict mapping CLI name to list of compiled skill names.
    """
    targets = clis or list(CLI_CONFIG.keys())
    results: dict[str, list[str]] = {}

    skill_dirs = sorted(source_dir.iterdir()) if source_dir.is_dir() else []
    if skills:
        skill_dirs = [d for d in skill_dirs if d.name in skills]

    for cli in targets:
        if cli not in CLI_CONFIG:
            print(f'  WARNING: Unknown CLI: {cli} (skipped)')
            continue

        cfg = CLI_CONFIG[cli]
        cli_skills_dir = target_base / cfg['global_skills_dir']
        compiled: list[str] = []

        for skill_dir in skill_dirs:
            if not skill_dir.is_dir() or skill_dir.name.startswith('.'):
                continue

            skill_md = skill_dir / 'SKILL.md'
            if not skill_md.exists():
                continue

            target_skill_dir = cli_skills_dir / skill_dir.name
            if dry_run:
                print(f'  -> {cli}: {skill_dir.name}/ -> {target_skill_dir}')
                compiled.append(skill_dir.name)
                continue

            # Copy entire skill directory
            if target_skill_dir.exists():
                shutil.rmtree(target_skill_dir)
            shutil.copytree(skill_dir, target_skill_dir)

            # Translate variable syntax in SKILL.md
            target_md = target_skill_dir / 'SKILL.md'
            content = target_md.read_text(encoding='utf-8')
            source_syntax = detect_var_syntax(content)
            content = translate_variables(content, source_syntax, cfg['var_syntax'])
            target_md.write_text(content, encoding='utf-8')

            compiled.append(skill_dir.name)

        results[cli] = compiled
        action = 'Would compile' if dry_run else 'Compiled'
        print(f'{action} {len(compiled)} skills -> {cli} ({cli_skills_dir})')

    return results


def main() -> None:
    parser = argparse.ArgumentParser(description='Compile skills to CLI-native directories')
    parser.add_argument('--source', default=str(Path.home() / '.agents' / 'skills'),
                        help='Canonical skills directory (default: ~/.agents/skills/)')
    parser.add_argument('--target', default=str(Path.home()),
                        help='Target base directory (default: ~ for global)')
    parser.add_argument('--clis', nargs='+', help='CLIs to compile for (default: all)')
    parser.add_argument('--skills', nargs='+', help='Skills to compile (default: all)')
    parser.add_argument('--dry-run', action='store_true', help='Preview without writing')

    args = parser.parse_args()

    source = Path(args.source)
    target = Path(args.target)

    if not source.exists():
        print(f'ERROR: Source directory not found: {source}')
        return

    print(f'Compiling skills from {source}')
    print(f'   Target: {target}')
    print(f'   Mode: {"DRY RUN" if args.dry_run else "WRITE"}\n')

    results = compile_skills(source, target, args.clis, args.skills, args.dry_run)

    total = sum(len(v) for v in results.values())
    print(f'\nTotal: {total} skill compilations across {len(results)} CLIs')


if __name__ == '__main__':
    main()
