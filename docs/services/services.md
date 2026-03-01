# Serviços e acessos

| Serviço | Endereço | Usuário | Senha | Observações |
|---|---|---|---|---|
| Keycloak (Admin Console) | http://localhost:8000/admin | admin | admin | Conta administrativa do Keycloak (`KEYCLOAK_ADMIN`). |
| Keycloak (Realm `tech-challenge`) | http://localhost:8000/realms/tech-challenge/account/ | admin, user, cliente, dono, gerente | admin, user, cliente, dono, gerente | Usuários importados de `containers-config/keycloak/realm/realm-export.json` (cada usuário usa a mesma senha do próprio nome). |
| Service Registry (Eureka) | http://localhost:8762 | N/A | N/A | Endpoint/UI do Eureka Server. |
| API Gateway | http://localhost:8761 | Token JWT (Keycloak) | Token JWT (Keycloak) | Rotas protegidas via OAuth2 Resource Server. |
| ProcPag | http://localhost:8089/openapi.yml | N/A | N/A | Endpoint OpenAPI documentado no `README.md`. |
| Keycloak DB | localhost:5433 (PostgreSQL) | root | root | Banco do Keycloak (`keycloak-db`). |
| Auth Service DB | host interno Docker: `auth-service-db:5432` | postgres | password | Serviço de banco dedicado no Docker Compose. |
| Client Service DB | host interno Docker: `client-service-db:5432` | postgres | password | Serviço de banco dedicado no Docker Compose. |
| Catalog Service DB | host interno Docker: `catalog-service-db:5432` | postgres | password | Serviço de banco dedicado no Docker Compose. |
| Order Service DB | host interno Docker: `order-service-db:5432` | postgres | password | Serviço de banco dedicado no Docker Compose. |
| Payment Service DB | host interno Docker: `payment-service-db:5432` | postgres | password | Serviço de banco dedicado no Docker Compose. |
| Restaurant Service DB | host interno Docker: `restaurant-service-db:5432` | postgres | password | Serviço de banco dedicado no Docker Compose. |

## Observação rápida

- Os bancos `auth-service-db`, `client-service-db`, `catalog-service-db`, `order-service-db`, `payment-service-db` e `restaurant-service-db` estão acessíveis internamente na rede Docker (`fase3net`).
- Para acessar esses bancos via host local, adicione mapeamento de portas no `compose.yml`.
