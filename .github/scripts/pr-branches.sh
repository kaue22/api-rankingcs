#!/usr/bin/env bash
set -euo pipefail

echo "Branch de destino (base): ${GITHUB_BASE_REF:-<vazio>}"
echo "Branch de origem (head):  ${GITHUB_HEAD_REF:-<vazio>}"

# Também escreve no Job Summary (aba Summary do run)
{
  echo "### Branches do Pull Request"
  echo "- Base (destino): \`${GITHUB_BASE_REF:-}\`"
  echo "- Head (origem): \`${GITHUB_HEAD_REF:-}\`"
} >> "$GITHUB_STEP_SUMMARY"

