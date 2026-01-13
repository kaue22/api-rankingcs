#!/usr/bin/env bash
set -e

# 1. Configura Fuso Horário SP
export TZ='America/Sao_Paulo'
NOW=$(date '+%d/%m/%Y %H:%M:%S')

# 2. Pega Referências
BASE_REF="${GITHUB_BASE_REF:-main}"
HEAD_REF="${GITHUB_HEAD_REF:-feature}"

# 3. Pega data da última atualização da branch de destino (Base)
# Usa 'git fetch' antes no workflow para garantir dados atualizados
BASE_LAST_UPDATE=$(git log -1 --format="%cd" --date=format:'%d/%m/%Y %H:%M:%S' "origin/$BASE_REF")

# 4. Busca lista de merges feitos dentro desta feature branch
# Lista apenas merges que não estão na base (exclusivos da feature)
MERGES=$(git log "origin/$BASE_REF..HEAD" --merges --pretty=format:"| %h | %ad | %s | %an |" --date=format:'%d/%m/%Y %H:%M')

# 5. Gera o Markdown
echo "### 🌿 Relatório de Branches e Merges"
echo ""
echo "| Info | Detalhe |"
echo "| :--- | :--- |"
echo "| **Data do Relatório** | $NOW (SP) |"
echo "| **Base (Destino)** | \`$BASE_REF\` |"
echo "| **Última Atualização (Base)** | $BASE_LAST_UPDATE |"
echo "| **Head (Origem)** | \`$HEAD_REF\` |"
echo ""

echo "#### 🔀 Merges nesta feature:"
if [ -z "$MERGES" ]; then
    echo "_Nenhum merge detectado nesta branch (além da base)._"
else
    echo ""
    echo "| Hash | Data (SP) | Merge | Autor |"
    echo "| :--- | :--- | :--- | :--- |"
    echo "$MERGES"
fi

echo ""
echo "---"
echo "_Automação CI - $NOW_"
