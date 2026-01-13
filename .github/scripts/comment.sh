#!/usr/bin/env bash
set -e

# Configurações
API_URL="https://api.github.com/repos/${GITHUB_REPOSITORY}"
PR_NUMBER="${1}"
MESSAGE_FILE="${2}"
TOKEN="${GITHUB_TOKEN}"

# Cabeçalho único (chave oculta)
HEADER_KEY="<!-- bot-comment-branch-checker -->"

# Lê o conteúdo da mensagem gerada
BODY=$(cat "${MESSAGE_FILE}")

# Função auxiliar
call_github() {
    curl -s -H "Authorization: token ${TOKEN}" \
            -H "Accept: application/vnd.github.v3+json" \
            "$@"
}

echo "🔍 Buscando comentários existentes no PR #${PR_NUMBER}..."

# Busca ID do comentário anterior
COMMENT_ID=$(call_github "${API_URL}/issues/${PR_NUMBER}/comments" | \
    jq -r ".[] | select(.body | contains(\"${HEADER_KEY}\")) | .id" | head -n 1)

# Prepara JSON seguro concatenando Header + Body direto no jq
JSON_PAYLOAD=$(jq -n \
                  --arg header "$HEADER_KEY" \
                  --arg body "$BODY" \
                  '{body: ($header + "\n\n" + $body)}')

if [ -n "$COMMENT_ID" ]; then
    echo "✏️ Atualizando comentário existente (ID: $COMMENT_ID)..."
    call_github -X PATCH -d "$JSON_PAYLOAD" "${API_URL}/issues/comments/${COMMENT_ID}" > /dev/null
else
    echo "➕ Criando novo comentário..."
    call_github -X POST -d "$JSON_PAYLOAD" "${API_URL}/issues/${PR_NUMBER}/comments" > /dev/null
fi

echo "✅ Feito!"
