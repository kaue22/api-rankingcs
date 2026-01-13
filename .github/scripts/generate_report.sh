#!/usr/bin/env bash
set -e
export TZ='America/Sao_Paulo'
NOW=$(date '+%d/%m/%Y %H:%M:%S')

BASE_REF="${GITHUB_BASE_REF:-main}"
HEAD_REF="${GITHUB_HEAD_REF:-feature}"

# Atualiza referências
git fetch origin $BASE_REF $HEAD_REF --update-head-ok

# Pega Hash Remoto Real
REMOTE_HEAD_SHA=$(git rev-parse "origin/$HEAD_REF" 2>/dev/null || echo "Desconhecido")
LOCAL_HEAD_SHA=$(git rev-parse HEAD)

# Data base
BASE_LAST_UPDATE=$(git log -1 --format="%cd" --date=format:'%d/%m/%Y %H:%M:%S' "origin/$BASE_REF")

# --- MERGES ---
RAW_MERGES=$(git log --merges -n 20 --format="%s" HEAD)
MERGED_LIST=""
while IFS= read -r line; do
    if [[ "$line" == "Merge branch"* ]]; then
        BRANCH_NAME=$(echo "$line" | sed -E "s/Merge branch '([^']+)'.*/\1/")
        if [[ "$BRANCH_NAME" == "$BASE_REF" ]]; then
            MERGED_LIST+="- \`$BRANCH_NAME\` (Sync com a base)\n"
        else
            MERGED_LIST+="- \`$BRANCH_NAME\`\n"
        fi
    elif [[ "$line" == "Merge pull request"* ]]; then
        BRANCH_FROM=$(echo "$line" | sed -E "s/.* from (.*)/\1/")
        MERGED_LIST+="- \`$BRANCH_FROM\` (via PR)\n"
    fi
done <<< "$RAW_MERGES"
FINAL_MERGE_DISPLAY=$(echo -e "$MERGED_LIST" | sort | uniq)
[ -z "$FINAL_MERGE_DISPLAY" ] && FINAL_MERGE_DISPLAY="_Nenhum merge recente detectado._"

# --- RELATÓRIO ---
echo "### 🌿 Status Avançado do PR"
echo ""
echo "| Info | Detalhe |"
echo "| :--- | :--- |"
echo "| **Data Report** | $NOW (SP) |"
echo "| **Base (Destino)** | \`$BASE_REF\` <br> _(Últ. upd: $BASE_LAST_UPDATE)_ |"
# AQUI ESTÁ A MUDANÇA: Adicionado HEAD_REF antes do hash
echo "| **Head (Local CI)** | \`$HEAD_REF\` <br> _($LOCAL_HEAD_SHA)_ |"
echo "| **Head (Remoto)** | \`origin/$HEAD_REF\` <br> _($REMOTE_HEAD_SHA)_ |"
echo ""
echo "#### 🧬 Histórico de Merges (Recentes)"
echo "$FINAL_MERGE_DISPLAY"
echo ""
echo "---"

# Verificação de Integridade
if [ "$LOCAL_HEAD_SHA" != "$REMOTE_HEAD_SHA" ]; then
    echo "⚠️ **Atenção:** A branch **$HEAD_REF** testada no CI (\`${LOCAL_HEAD_SHA:0:7}\`) está atrás da versão remota (\`${REMOTE_HEAD_SHA:0:7}\`)."
else
    echo "✅ CI rodando na versão mais recente de **$HEAD_REF**."
fi
