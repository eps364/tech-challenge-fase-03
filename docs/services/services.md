# Serviços e acessos

| Serviço | Endereço | Usuário | Senha | Observações |
|---|---|---|---|---|
| Keycloak (Admin Console) | http://localhost:8080/admin | admin | admin | Conta administrativa do Keycloak (`KEYCLOAK_ADMIN`). |
| Keycloak (Realm `tech-challenge`) | http://localhost:8080/realms/tech-challenge/account/ | admin, user, cliente, dono, gerente | admin, user, cliente, dono, gerente | Usuários importados de `docker/keycloak/realm/realm-export.json` (cada usuário usa a mesma senha do próprio nome). |
| Service Registry (Eureka) | http://localhost:8762 | N/A | N/A | Endpoint/UI do Eureka Server. |
| API Gateway | http://localhost:8761 | Token JWT (Keycloak) | Token JWT (Keycloak) | Rotas protegidas via OAuth2 Resource Server. Endpoints `/auth-service/auth/**` são públicos. |
| Auth Service | via API Gateway (`http://localhost:8761/auth-service`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de autenticação integrado ao Keycloak; expõe `POST /auth/register`, `POST /auth/login`, `POST /auth/logout`. O `register` retorna o campo `roles` (`["user", "manage-account", "view-profile"]`). Registrado no Eureka. |
| Client Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de clientes registrado no Eureka; sem porta exposta no host no `compose.yml`. |
| Catalog Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de catálogo registrado no Eureka; sem porta exposta no host no `compose.yml`. CRUD de produtos em `/catalog-service/products`. GET público; POST/PUT/DELETE requerem role `owner` ou `admin`. |
| Order Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de pedidos registrado no Eureka; sem porta exposta no host no `compose.yml`. |
| Payment Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de pagamento registrado no Eureka; sem porta exposta no host no `compose.yml`. |
| Restaurant Service | via API Gateway (`http://localhost:8761`) | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de restaurantes registrado no Eureka; sem porta exposta no host no `compose.yml`. |
| RabbitMQ (AMQP) | localhost:5672 | guest | guest | Broker para mensageria assíncrona entre serviços. |
| RabbitMQ (Management UI) | http://localhost:15672 | guest | guest | Interface administrativa do RabbitMQ com definições em `docker/rabbitmq/definitions.json`. |
| ProcPag | http://localhost:8089/openapi.yml | N/A | N/A | Endpoint OpenAPI documentado no `README.md`. |
| Redis | localhost:6379 | N/A | N/A | Blacklist de tokens JWT após logout. Chave: `blacklist:<jti>` com TTL automático. |
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
| `POST` | `/auth-service/auth/logout` | JWT | Revoga a sessão no Keycloak e grava o `jti` do token no Redis com TTL para invalidação imediata. O API Gateway rejeita o token com `401` até sua expiração natural. |

## Catalog Service — Endpoints

| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| `GET` | `/catalog-service/products` | Pública | Lista todos os produtos cadastrados. |
| `GET` | `/catalog-service/products/{id}` | Pública | Retorna produto pelo ID. Retorna `404 ProblemDetail` se não encontrado. |
| `POST` | `/catalog-service/products` | JWT (`owner` ou `admin`) | Cria novo produto. Corpo: `{ "name": "string", "price": 0.00 }`. Retorna `201 Created` com o produto criado. |
| `PUT` | `/catalog-service/products/{id}` | JWT (`owner` ou `admin`) | Atualiza nome e preço do produto. Retorna `404 ProblemDetail` se não encontrado. |
| `DELETE` | `/catalog-service/products/{id}` | JWT (`owner` ou `admin`) | Remove o produto. Retorna `204 No Content`. |

> **Tratamento de erros (RFC 7807):** respostas de erro retornam `ProblemDetail` com `type`, `title`, `status`, `detail` e `instance`. Erros `401 Unauthorized` são tratados centralmente pelo API Gateway.

## Observações

- Os bancos `auth-service-db`, `client-service-db`, `catalog-service-db`, `order-service-db`, `payment-service-db` e `restaurant-service-db` rodam em serviços dedicados (`*-service-db`) na rede Docker (`fase3net`).
- Para acessar esses bancos via host local, adicione mapeamento de portas no `compose.yml`.
- O Keycloak roda na porta `8080` (não `8000`).
- O `orchestrator-service` coordena o fluxo de eventos entre `order-service`, `payment-service` e `restaurant-service` via RabbitMQ.
- O Redis é utilizado exclusivamente para blacklist de tokens JWT; não há persistência de negócio nele.
- **Tratamento de 401 centralizado:** o API Gateway retorna respostas `401 Unauthorized` com corpo RFC 7807 (`ProblemDetail`). Os microserviços internos não implementam `AuthenticationEntryPoint` próprio.
- **Roles de acesso:** roles extraídas de `realm_access.roles` no JWT emitido pelo Keycloak. Para operações de escrita no `catalog-service`, o usuário precisa ter a role `owner` ou `admin` no realm `tech-challenge`.
