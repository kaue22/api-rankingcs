#!/usr/bin/env bash

# Gera a mensagem em Markdown
echo "### 🌿 Branches do PR (Manual Control)"
echo ""
echo "| Tipo | Branch |"
echo "| :--- | :--- |"
echo "| **Base** (Destino) | \`${GITHUB_BASE_REF}\` |"
echo "| **Head** (Origem) | \`${GITHUB_HEAD_REF}\` |"
echo ""
echo "_Comentário gerado via cURL em $(date)_"
