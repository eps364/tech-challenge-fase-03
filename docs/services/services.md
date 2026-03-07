# Serviços e acessos

| Serviço | Endereço | Usuário | Senha | Observações |
|---|---|---|---|---|
| Keycloak (Admin Console) | http://localhost:8080/admin | admin | admin | Conta administrativa do Keycloak (`KEYCLOAK_ADMIN`). |
| Keycloak (Realm `tech-challenge`) | http://localhost:8080/realms/tech-challenge/account/ | admin, user, cliente, dono, gerente | admin, user, cliente, dono, gerente | Usuários importados de `docker/keycloak/realm/realm-export.json` (cada usuário usa a mesma senha do próprio nome). |
| Service Registry (Eureka) | http://localhost:8762 | N/A | N/A | Endpoint/UI do Eureka Server. |
| API Gateway | http://localhost:8761 | Token JWT (Keycloak) | Token JWT (Keycloak) | Rotas protegidas via OAuth2 Resource Server. Endpoints `/auth-service/auth/**` são públicos. |
| Auth Service | via API Gateway (`http://localhost:8761/auth-service`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de autenticação integrado ao Keycloak; expõe `POST /auth/register`, `POST /auth/login`, `POST /auth/logout`. O `register` retorna o campo `roles` (`["user", "manage-account", "view-profile"]`). Registrado no Eureka. |
| Client Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de clientes registrado no Eureka; sem porta exposta no host no `compose.yml`. |
| Catalog Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de catálogo registrado no Eureka; sem porta exposta no host no `compose.yml`. |
| Order Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de pedidos registrado no Eureka; sem porta exposta no host no `compose.yml`. |
| Payment Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de pagamento registrado no Eureka; sem porta exposta no host no `compose.yml`. |
| Restaurant Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de restaurantes registrado no Eureka; sem porta exposta no host no `compose.yml`. |
| RabbitMQ (AMQP) | localhost:5672 | guest | guest | Broker para mensageria assíncrona entre serviços. |
| RabbitMQ (Management UI) | http://localhost:15672 | guest | guest | Interface administrativa do RabbitMQ com definições em `docker/rabbitmq/definitions.json`. |
| ProcPag | http://localhost:8089/openapi.yml | N/A | N/A | Endpoint OpenAPI documentado no `README.md`. |
| Keycloak DB | localhost:5433 (PostgreSQL) | root | root | Banco do Keycloak (`keycloak-db`). |
| Auth Service DB | host interno Docker: `auth-service-db:5432` | postgres | password | Banco lógico: `auth-service-db`. |
| Client Service DB | host interno Docker: `client-service-db:5432` | postgres | password | Banco lógico: `client-service-db`. |
| Catalog Service DB | host interno Docker: `catalog-service-db:5432` | postgres | password | Banco lógico: `catalog-service-db`. |
| Order Service DB | host interno Docker: `order-service-db:5432` | postgres | password | Banco lógico: `order-service-db`. |
| Payment Service DB | host interno Docker: `payment-service-db:5432` | postgres | password | Banco lógico: `payment-service-db`. |
| Restaurant Service DB | host interno Docker: `restaurant-service-db:5432` | postgres | password | Banco lógico: `restaurant-service-db`. |

## Auth Service — Endpoints

| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| `POST` | `/auth-service/auth/register` | Pública | Cria usuário no Keycloak e no banco local. Retorna `id`, `username`, `email`, `firstName`, `lastName`, `roles`. |
| `POST` | `/auth-service/auth/login` | Pública | Autentica com Keycloak e retorna o token JWT. |
| `POST` | `/auth-service/auth/logout` | JWT | Faz logout da sessão do usuário no Keycloak. |

## Observações

- Os bancos `auth-service-db`, `client-service-db`, `catalog-service-db`, `order-service-db`, `payment-service-db` e `restaurant-service-db` rodam em serviços dedicados (`*-service-db`) na rede Docker (`fase3net`).
- Para acessar esses bancos via host local, adicione mapeamento de portas no `compose.yml`.
- O Keycloak roda na porta `8080` (não `8000`).
