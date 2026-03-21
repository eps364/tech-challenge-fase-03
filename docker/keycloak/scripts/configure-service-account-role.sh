#!/bin/bash
set -euo pipefail

KEYCLOAK_URL="${KEYCLOAK_URL:-http://keycloak:8080}"
KEYCLOAK_ADMIN="${KEYCLOAK_ADMIN:-admin}"
KEYCLOAK_ADMIN_PASSWORD="${KEYCLOAK_ADMIN_PASSWORD:-admin}"
TARGET_REALM="${TARGET_REALM:-tech-challenge}"
TARGET_CLIENT_ID="${TARGET_CLIENT_ID:-orchestrator-service}"
TARGET_ROLE="${TARGET_ROLE:-system}"

echo "Autenticando no Keycloak..."
/opt/keycloak/bin/kcadm.sh config credentials \
  --server "$KEYCLOAK_URL" \
  --realm master \
  --user "$KEYCLOAK_ADMIN" \
  --password "$KEYCLOAK_ADMIN_PASSWORD"

echo "Buscando client id de ${TARGET_CLIENT_ID}..."
CLIENT_UUID=$(
  /opt/keycloak/bin/kcadm.sh get clients \
    -r "$TARGET_REALM" \
    -q clientId="$TARGET_CLIENT_ID" \
    --fields id \
    --format csv \
    --noquotes | head -n 1
)

if [ -z "$CLIENT_UUID" ]; then
  echo "Client ${TARGET_CLIENT_ID} não encontrado no realm ${TARGET_REALM}"
  exit 1
fi

echo "Buscando usuário da service account..."
SERVICE_ACCOUNT_USER_ID=$(
  /opt/keycloak/bin/kcadm.sh get "clients/${CLIENT_UUID}/service-account-user" \
    -r "$TARGET_REALM" \
    --fields id \
    --format csv \
    --noquotes | head -n 1
)

if [ -z "$SERVICE_ACCOUNT_USER_ID" ]; then
  echo "Service account do client ${TARGET_CLIENT_ID} não encontrada"
  exit 1
fi

echo "Atribuindo role ${TARGET_ROLE}..."
/opt/keycloak/bin/kcadm.sh add-roles \
  -r "$TARGET_REALM" \
  --uid "$SERVICE_ACCOUNT_USER_ID" \
  --rolename "$TARGET_ROLE"

echo "Role ${TARGET_ROLE} atribuída com sucesso à service account de ${TARGET_CLIENT_ID}"

