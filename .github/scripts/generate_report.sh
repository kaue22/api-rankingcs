#!/usr/bin/env bash
set -e
export TZ='America/Sao_Paulo'
NOW=$(date '+%d/%m/%Y %H:%M:%S')

BASE_REF="${GITHUB_BASE_REF:-main}"
HEAD_REF="${GITHUB_HEAD_REF:-feature}"

# Atualiza referências para garantir integridade
git fetch origin $BASE_REF $HEAD_REF --update-head-ok

# Pega Hash Remoto Real da feature (para conferência)
REMOTE_HEAD_SHA=$(git rev-parse "origin/$HEAD_REF" 2>/dev/null || echo "Desconhecido")
LOCAL_HEAD_SHA=$(git rev-parse HEAD)

# Data base
BASE_LAST_UPDATE=$(git log -1 --format="%cd" --date=format:'%d/%m/%Y %H:%M:%S' "origin/$BASE_REF")

# --- NOVA LÓGICA DE MERGES ---
# Lista TODOS os merges contidos neste histórico da feature (últimos 20 para não poluir)
# Formatamos para pegar o nome da branch mergeada
RAW_MERGES=$(git log --merges -n 20 --format="%s" HEAD)

# Filtra e limpa os nomes
# 1. Pega 'Merge branch X' ou 'Merge pull request X from Y'
MERGED_LIST=""

# Processa linha por linha (gambiarra bash safe)
while IFS= read -r line; do
    # Caso 1: Merge branch 'nome' (into ...)
    if [[ "$line" == "Merge branch"* ]]; then
        BRANCH_NAME=$(echo "$line" | sed -E "s/Merge branch '([^']+)'.*/\1/")
        # Se for merge da própria main, marca diferente
        if [[ "$BRANCH_NAME" == "$BASE_REF" ]]; then
            MERGED_LIST+="- \`$BRANCH_NAME\` (Sync com a base)\n"
        else
            MERGED_LIST+="- \`$BRANCH_NAME\`\n"
        fi
    
    # Caso 2: Merge pull request #X from branch/tal
    elif [[ "$line" == "Merge pull request"* ]]; then
        # Tenta pegar o nome da branch depois do 'from'
        BRANCH_FROM=$(echo "$line" | sed -E "s/.* from (.*)/\1/")
        MERGED_LIST+="- \`$BRANCH_FROM\` (via PR)\n"
    fi
done <<< "$RAW_MERGES"

# Remove duplicatas e linhas vazias
FINAL_MERGE_DISPLAY=$(echo -e "$MERGED_LIST" | sort | uniq)

if [ -z "$FINAL_MERGE_DISPLAY" ]; then
    FINAL_MERGE_DISPLAY="_Nenhum merge recente detectado._"
fi

# --- Gera Relatório ---
echo "### 🌿 Status Avançado do PR"
echo ""
echo "| Info | Detalhe |"
echo "| :--- | :--- |"
echo "| **Data Report** | $NOW (SP) |"
echo "| **Base** | \`$BASE_REF\` (Atualizado em: $BASE_LAST_UPDATE) |"
echo "| **Head (Local CI)** | \`${LOCAL_HEAD_SHA:0:7}\` |"
echo "| **Head (Remoto)** | \`${REMOTE_HEAD_SHA:0:7}\` |"
echo ""
echo "#### 🧬 Histórico de Merges (Recentes)"
echo "$FINAL_MERGE_DISPLAY"
echo ""
echo "---"

# Verificação de Integridade
if [ "$LOCAL_HEAD_SHA" != "$REMOTE_HEAD_SHA" ]; then
    echo "⚠️ **Atenção:** O commit testado no CI (\`${LOCAL_HEAD_SHA:0:7}\`) difere da ponta da branch remota (\`${REMOTE_HEAD_SHA:0:7}\`). Você pode ter dado push enquanto o CI rodava."
else
    echo "✅ CI rodando na versão mais recente do remoto."
fi
