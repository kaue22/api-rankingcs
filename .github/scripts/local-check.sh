#!/usr/bin/env bash
set -e

# --- Cores e Formatação ---
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# --- Configurações ---
# Detecta a branch atual automaticamente
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
# Define a base (pode passar como argumento, ex: ./script.sh develop)
BASE_BRANCH="${1:-main}" 

echo -e "${BLUE}${BOLD}=== 🕵️  Validando Branch: ${CURRENT_BRANCH} (Base: ${BASE_BRANCH}) ===${NC}"
echo "Atualizando referências remotas..."
git fetch origin "${BASE_BRANCH}" "${CURRENT_BRANCH}" --quiet

# --- Coleta de Dados ---
LOCAL_SHA=$(git rev-parse HEAD)
# Tenta pegar o hash remoto, se não existir (branch nova local), avisa
REMOTE_SHA=$(git rev-parse "origin/${CURRENT_BRANCH}" 2>/dev/null || echo "MISSING")

BASE_LAST_UPDATE=$(git log -1 --format="%cd (%cr)" --date=format:'%d/%m/%Y %H:%M' "origin/${BASE_BRANCH}")

# --- 1. Validação de Integridade (Local vs Remoto) ---
echo ""
echo -e "${BOLD}1. Status de Sincronização:${NC}"

if [ "$REMOTE_SHA" == "MISSING" ]; then
    echo -e "   ${YELLOW}⚠️  Branch ainda não existe no remoto (apenas local).${NC}"
    echo -e "   Hash Local: ${LOCAL_SHA:0:7}"
elif [ "$LOCAL_SHA" == "$REMOTE_SHA" ]; then
    echo -e "   ${GREEN}✅ Sincronizado com o remoto.${NC}"
    echo -e "   Hash: ${LOCAL_SHA:0:7}"
else
    echo -e "   ${RED}❌ Desincronizado!${NC}"
    echo -e "   Local:  ${LOCAL_SHA:0:7}"
    echo -e "   Remoto: ${REMOTE_SHA:0:7}"
    
    # Verifica quem está na frente
    BEHIND=$(git rev-list --count HEAD..origin/${CURRENT_BRANCH})
    AHEAD=$(git rev-list --count origin/${CURRENT_BRANCH}..HEAD)
    
    if [ "$AHEAD" -gt 0 ]; then
        echo -e "   ${YELLOW}➔ Você tem $AHEAD commit(s) para subir (git push).${NC}"
    fi
    if [ "$BEHIND" -gt 0 ]; then
        echo -e "   ${RED}➔ Você está $BEHIND commit(s) atrás do remoto (git pull).${NC}"
    fi
fi

# --- 2. Informações da Base ---
echo ""
echo -e "${BOLD}2. Status da Base (${BASE_BRANCH}):${NC}"
echo -e "   Última atualização: ${BASE_LAST_UPDATE}"

# --- 3. Histórico de Merges ---
echo ""
echo -e "${BOLD}3. Histórico de Merges (Recentes):${NC}"

# Mesma lógica do CI, mas formatada para terminal
RAW_MERGES=$(git log --merges -n 20 --format="%s" HEAD)

if [ -z "$RAW_MERGES" ]; then
    echo -e "   ${YELLOW}_Nenhum merge recente detectado._${NC}"
else
    while IFS= read -r line; do
        if [[ "$line" == "Merge branch"* ]]; then
            BRANCH_NAME=$(echo "$line" | sed -E "s/Merge branch '([^']+)'.*/\1/")
            if [[ "$BRANCH_NAME" == "$BASE_BRANCH" ]]; then
                echo -e "   • ${BLUE}${BRANCH_NAME}${NC} (Sync com a base)"
            else
                echo -e "   • ${GREEN}${BRANCH_NAME}${NC}"
            fi
        elif [[ "$line" == "Merge pull request"* ]]; then
            BRANCH_FROM=$(echo "$line" | sed -E "s/.* from (.*)/\1/")
            echo -e "   • ${GREEN}${BRANCH_FROM}${NC} (via PR)"
        fi
    done <<< "$RAW_MERGES" | sort | uniq
fi

echo ""
echo -e "${BLUE}=== Fim da validação ===${NC}"
