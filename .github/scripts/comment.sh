#!/usr/bin/env bash
set -e

# Configurações
API_URL="https://api.github.com/repos/${GITHUB_REPOSITORY}"
PR_NUMBER="${1}"      # Número do PR passado como argumento
MESSAGE_FILE="${2}"   # Arquivo contendo a mensagem
TOKEN="${GITHUB_TOKEN}" # Token injetado pelo workflow

# Cabeçalho único para identificar SEU comentário (tipo uma "chave primária" no texto)
HEADER_KEY="<!-- bot-comment-branch-checker -->"

# Lê a mensagem e adiciona a chave oculta no início
BODY=$(cat "${MESSAGE_FILE}")
FULL_BODY="${HEADER_KEY}\n\n${BODY}"

# Função auxiliar para chamar a API
call_github() {
    curl -s -H "Authorization: token ${TOKEN}" \
            -H "Accept: application/vnd.github.v3+json" \
            "$@"
}

echo "🔍 Buscando comentários existentes no PR #${PR_NUMBER}..."

# Busca lista de comentários do PR e filtra pelo nosso HEADER_KEY
# jq é usado para parsear o JSON e pegar o ID se existir
COMMENT_ID=$(call_github "${API_URL}/issues/${PR_NUMBER}/comments" | \
    jq -r ".[] | select(.body | contains(\"${HEADER_KEY}\")) | .id" | head -n 1)

# Prepara o payload JSON (cuidado com aspas no bash)
# jq --arg cria um JSON seguro escapando caracteres especiais
JSON_PAYLOAD=$(jq -n --arg body "${FULL_BODY}" '{body: $body}')

if [ -n "$COMMENT_ID" ]; then
    echo "✏️ Atualizando comentário existente (ID: $COMMENT_ID)..."
    call_github -X PATCH -d "$JSON_PAYLOAD" "${API_URL}/issues/comments/${COMMENT_ID}" > /dev/null
else
    echo "➕ Criando novo comentário..."
    call_github -X POST -d "$JSON_PAYLOAD" "${API_URL}/issues/${PR_NUMBER}/comments" > /dev/null
fi

echo "✅ Feito!"
