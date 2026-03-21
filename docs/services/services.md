# Serviços, Bancos e Fluxos

| Serviço | Endereço | Usuário | Senha | Observações |
|---|---|---|---|---|
| Keycloak (Admin Console) | http://localhost:8080/admin | admin | admin | Conta administrativa do Keycloak. |
| Keycloak (Realm `tech-challenge`) | http://localhost:8080/realms/tech-challenge/account/ | admin, user, cliente, dono, gerente | admin, user, cliente, dono, gerente | Usuários importados de `docker/keycloak/realm/realm-export.json`. |
| Service Registry (Eureka) | http://localhost:8762 | N/A | N/A | UI do Eureka Server. |
| API Gateway | http://localhost:8761 | Token JWT (Keycloak) | Token JWT (Keycloak) | Rotas protegidas via OAuth2 Resource Server. Endpoints `/auth-service/auth/**` são públicos. |
| Auth Service | via API Gateway | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de autenticação integrado ao Keycloak. |
| Client Service | via API Gateway | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de clientes registrado no Eureka. |
| Catalog Service | via API Gateway | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de catálogo registrado no Eureka. CRUD de produtos em `/catalog-service/products`. |
| Order Service | via API Gateway | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de pedidos registrado no Eureka. |
| Payment Service | via API Gateway | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de pagamento registrado no Eureka. |
| Restaurant Service | via API Gateway | Token JWT (Keycloak) | Token JWT (Keycloak) | Serviço de restaurantes registrado no Eureka. |
| RabbitMQ (AMQP) | localhost:5672 | guest | guest | Broker para mensageria assíncrona entre serviços. |
| RabbitMQ (Management UI) | http://localhost:15672 | guest | guest | Interface administrativa do RabbitMQ. |
| ProcPag | http://localhost:8089/openapi.yml | N/A | N/A | Endpoint OpenAPI externo. |
| Redis | localhost:6379 | N/A | N/A | Blacklist de tokens JWT após logout. |
| Keycloak DB | localhost:5433 (PostgreSQL) | root | root | Banco do Keycloak. |
| Auth Service DB | `auth-service-db:5432` | postgres | password | Banco dedicado do Auth Service. |
| Client Service DB | `client-service-db:5432` | postgres | password | Banco dedicado do Client Service. |
| Catalog Service DB | `catalog-service-db:5432` | postgres | password | Banco dedicado do Catalog Service. |
| Order Service DB | `order-service-db:5432` | postgres | password | Banco dedicado do Order Service. |
| Payment Service DB | `payment-service-db:5432` | postgres | password | Banco dedicado do Payment Service. |
| Restaurant Service DB | `restaurant-service-db:5432` | postgres | password | Banco dedicado do Restaurant Service. |

## Fluxos e Resiliência

Os fluxos principais estão ilustrados nos diagramas:
- [arquitetura-sequencia-pedido-pagamento.puml](../diagrams/arquitetura-sequencia-pedido-pagamento.puml)
- [flow-order.puml](../diagrams/flow-order.puml)

Principais eventos:
- `pedido.criado`, `pagamento.aprovado`, `pagamento.pendente` (RabbitMQ/Kafka).

Resiliência:
- Retry, Timeout, Circuit Breaker (Resilience4j).
- Fallback para status pendente e reprocessamento automático.

## Endpoints principais

### Auth Service
| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| `POST` | `/auth-service/auth/register` | Pública | Cria usuário no Keycloak e no banco local. |
| `POST` | `/auth-service/auth/login` | Pública | Autentica com Keycloak e retorna o token JWT. |
| `POST` | `/auth-service/auth/logout` | JWT | Revoga a sessão no Keycloak e grava o `jti` do token no Redis. |

### Catalog Service
| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| `GET` | `/catalog-service/products` | Pública | Lista todos os produtos cadastrados. |
| `GET` | `/catalog-service/products/{id}` | Pública | Retorna produto pelo ID. |
| `POST` | `/catalog-service/products` | JWT (`owner` ou `admin`) | Cria novo produto. Corpo: `{ "name": "string", "price": 0.00 }`. Retorna `201 Created` com o produto criado. |
| `PUT` | `/catalog-service/products/{id}` | JWT (`owner` ou `admin`) | Atualiza nome e preço do produto. Retorna `404 ProblemDetail` se não encontrado. |
| `DELETE` | `/catalog-service/products/{id}` | JWT (`owner` ou `admin`) | Remove o produto. Retorna `204 No Content`. |

> **Tratamento de erros (RFC 7807):** respostas de erro retornam `ProblemDetail` com `type`, `title`, `status`, `detail` e `instance`. Erros `401 Unauthorized` são tratados centralmente pelo API Gateway.

## Client Service
| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| `POST` | `/client-service/clients/{id}` | JWT | Cria novo cliente. |
| `GET` | `/client-service/clients/{id}` | JWT | Retorna cliente pelo ID. |
| `PUT` | `/client-service/clients/{id}` | JWT | Atualiza dados do cliente. |
| `DELETE` | `/client-service/clients/{id}` | JWT | Remove cliente. |

## Orchestrator Service
| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| `POST` | `/orchestrator-service/orchestrator/requests` | JWT | Cria pedido via orquestração. Busca dados do cliente, resolve produtos, monta objeto e cria pedido no order-service. |

## Payment Service (Endpoints de Negócio)
| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| `POST` | `/payment-service/payments` | JWT | Processa pagamento de pedido. **(Falta documentação e coleção Bruno)** |
| `GET` | `/payment-service/payments/{id}` | JWT | Consulta status de pagamento. **(Falta documentação e coleção Bruno)** |

## Observações

- Os bancos `auth-service-db`, `client-service-db`, `catalog-service-db`, `order-service-db`, `payment-service-db` e `restaurant-service-db` rodam em serviços dedicados (`*-service-db`) na rede Docker (`fase3net`).
- Para acessar esses bancos via host local, adicione mapeamento de portas no `compose.yml`.
- O Keycloak roda na porta `8080` (não `8000`).
- O `orchestrator-service` coordena o fluxo de eventos entre `order-service`, `payment-service` e `restaurant-service` via RabbitMQ.
- O Redis é utilizado exclusivamente para blacklist de tokens JWT; não há persistência de negócio nele.
- **Tratamento de 401 centralizado:** o API Gateway retorna respostas `401 Unauthorized` com corpo RFC 7807 (`ProblemDetail`). Os microserviços internos não implementam `AuthenticationEntryPoint` próprio.
- **Roles de acesso:** roles extraídas de `realm_access.roles` no JWT emitido pelo Keycloak. Para operações de escrita no `catalog-service`, o usuário precisa ter a role `owner` ou `admin` no realm `tech-challenge`.
