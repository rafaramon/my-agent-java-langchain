---
name: GitHub Git Operations
description: Essential Git and GitHub core commands (commits, branching, pull, push, and PR creation via gh CLI).
---

# GitHub Git Operations

This skill outlines the standard commands and workflows to perform Git version control operations and interact with GitHub via the CLI. It assumes that both `git` and `gh` (GitHub CLI) are installed and authenticated.

## 1. Status and Changes

Always check the current state before adding commits or switching branches.

```bash
git status
git diff
```

## 2. Creating Branches

Use descriptive branch names (e.g., `feature/login`, `fix/login-bug`, `chore/deps-update`).

```bash
# Update local before branching
git checkout main
git pull origin main

# Create and switch to new branch
git checkout -b <branch-name>
```

## 3. Staging and Committing

Small, atomic commits with descriptive, English-only messages are preferred.

```bash
# Stage specific files (preferred over 'git add .')
git add <file-path>

# Commit with a clear message
git commit -m "feat: <description>"
# OR for fixes:
git commit -m "fix: <description>"
```

## 4. Syncing (Pull & Push)

Always ensure your branch is up to date with its remote counterpart.

```bash
# Pull latest changes from remote branch
git pull origin <branch-name>

# Push local branch to remote for the first time
git push -u origin <branch-name>

# Push local branch to remote (subsequent times)
git push
```

## 5. Creating Pull Requests (GitHub CLI)

Use the `gh` tool instead of the web UI for speed. Ensure you are on the branch you want to merge.

```bash
# Create a PR interactively
gh pr create

# Or create a PR non-interactively with title and body
gh pr create --title "feat: Add new dashboard" --body "This PR adds the main dashboard view..." --base main
```

## 6. Reviewing and Merging PRs

```bash
# List open PRs
gh pr list

# Checkout a specific PR to test locally
gh pr checkout <pr-number>

# Approve a PR
gh pr review <pr-number> --approve

# Merge a PR (interactive)
gh pr merge <pr-number>
```

## Best Practices
- **No force pushing** (`--force` or `-f`) unless strictly necessary and communicating with the team.
- **Commit messages in English** as strictly requested in `AGENTS.md`.
- Ensure all CI/CD (like `./gradlew test`) passes **before** pushing and creating a PR.
