#!/usr/bin/env bash
set -e

# Configura Fuso Horário SP
export TZ='America/Sao_Paulo'
NOW=$(date '+%d/%m/%Y %H:%M:%S')

BASE_REF="${GITHUB_BASE_REF:-main}"
HEAD_REF="${GITHUB_HEAD_REF:-feature}"

# Pega data da última atualização da Base
BASE_LAST_UPDATE=$(git log -1 --format="%cd" --date=format:'%d/%m/%Y %H:%M:%S' "origin/$BASE_REF")

# --- Lógica de Merges ---
# 1. Pega os merges
# 2. Extrai o nome da branch da mensagem de merge (ex: "Merge branch 'X'...")
# 3. Limpa caracteres indesejados
MERGED_BRANCHES_LIST=$(git log "origin/$BASE_REF..HEAD" --merges --pretty=format:"%s" | \
    grep "Merge branch" | \
    sed -E "s/Merge branch '([^']+)'.*/\1/" | \
    sed -E "s/ into .*//" | \
    sort | uniq)

# Formata para Markdown (lista com bullets) ou mensagem padrão se vazio
if [ -z "$MERGED_BRANCHES_LIST" ]; then
    MERGED_DISPLAY="_Nenhuma_"
else
    # Transforma em lista HTML (<br> funciona melhor dentro de tabelas MD em alguns visualizadores, 
    # mas bullets padrão com quebra de linha também servem)
    MERGED_DISPLAY=$(echo "$MERGED_BRANCHES_LIST" | sed 's/^/- `/')
fi

# --- Gera o Relatório Unificado ---
echo "### 🌿 Status do Pull Request"
echo ""
echo "| Info | Detalhe |"
echo "| :--- | :--- |"
echo "| **Data do Report** | $NOW (SP) |"
echo "| **Base (Destino)** | \`$BASE_REF\` |"
echo "| **Última Atualização (Base)** | $BASE_LAST_UPDATE |"
echo "| **Head (Origem)** | \`$HEAD_REF\` |"
echo "| **Branches Mergeadas** | $MERGED_DISPLAY |"
echo ""
echo "_Automação CI_"
