# Microservices - Visão Geral e Fluxos

## Documentação Swagger/OpenAPI

Todos os microserviços possuem documentação automática via Swagger UI:

| Serviço              | Swagger UI                      |
|----------------------|---------------------------------|
| auth-service         | http://localhost:8081/swagger-ui.html |
| client-service       | http://localhost:8082/swagger-ui.html |
| catalog-service      | http://localhost:8083/swagger-ui.html |
| order-service        | http://localhost:8084/swagger-ui.html |
| payment-service      | http://localhost:8085/swagger-ui.html |
| restaurant-service   | http://localhost:8086/swagger-ui.html |
| orchestrator-service | http://localhost:8087/swagger-ui.html |

Consulte cada serviço para visualizar endpoints, payloads, status, exemplos e contratos OpenAPI.


Este documento resume os microservices, endpoints, eventos, resiliência e fluxos principais do projeto.

## Referência aos Diagramas
Consulte os diagramas em [docs/diagrams](diagrams/) para visualizar:
- Fluxo de pedido e pagamento: [arquitetura-sequencia-pedido-pagamento.puml](diagrams/arquitetura-sequencia-pedido-pagamento.puml)
- Fluxo completo: [flow-order.puml](diagrams/flow-order.puml)
- Sequência de autenticação: [arquitetura-sequencia-atual.puml](diagrams/arquitetura-sequencia-atual.puml)

## Principais Eventos e Resiliência
- Eventos obrigatórios: `pedido.criado`, `pagamento.aprovado`, `pagamento.pendente` (RabbitMQ/Kafka).
- Resiliência: Retry, Timeout, Circuit Breaker (Resilience4j), fallback para status pendente, reprocessamento automático.
- Todos os fluxos de pagamento são tolerantes a falhas: se o serviço externo estiver indisponível, o pedido é marcado como `PENDENTE_PAGAMENTO` e reprocessado automaticamente.
- Os eventos de pendência e reprocessamento são rastreáveis via RabbitMQ e logs dos serviços.
- Reprocessamento: Worker consome pendências e atualiza status.

## Microservices

### api-gateway
- Entrada única das APIs, roteamento, validação JWT, verificação de blacklist Redis.
- Valida assinatura JWT via Keycloak JWKS.
- Consulta Redis para blacklist de tokens.
- Regras de autorização dinâmicas carregadas do Redis.
- Tratamento de erros centralizado (RFC 7807).

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/auth-service/test/public` | Sim | Pública | Encaminha para endpoint público do `auth-service`. |
| GET | `/order-service/test/public` | Sim | Pública | Encaminha para endpoint público do `order-service`. |
| GET | `/payment-service/test/public` | Sim | Pública | Encaminha para endpoint público do `payment-service`. |
| GET | `/restaurant-service/test/public` | Sim | Pública | Encaminha para endpoint público do `restaurant-service`. |
| GET | `/client-service/test/public` | Sim | Pública | Encaminha para endpoint público do `client-service`. |
| GET | `/catalog-service/test/public` | Sim | Pública | Encaminha para endpoint público do `catalog-service`. |
| GET | `/gateway/security/routes` | Sim | `admin` | Lista regras dinâmicas de autorização do gateway. |
| POST | `/gateway/security/routes` | Sim | `admin` | Cria uma regra de autorização. |
| PUT | `/gateway/security/routes/{id}` | Sim | `admin` | Atualiza uma regra específica. |
| DELETE | `/gateway/security/routes/{id}` | Sim | `admin` | Remove uma regra específica. |
| PUT | `/gateway/security/routes` | Sim | `admin` | Substitui toda a lista de regras. |
| Todos | `/**` | Sim | JWT obrigatório | Demais rotas exigem autenticação. |

### service-registry
- Descoberta de serviços com Eureka.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| N/A | N/A | Não | N/A | Não há endpoints de negócio para o `service-registry`. |

### auth-service
- Autenticação/autorização, integração com Keycloak.
- Banco dedicado: `auth-service-db`.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| POST | `/auth/register` | Sim (`/auth-service/auth/register`) | Pública | Registra novo usuário no Keycloak e no banco local, atribuindo roles padrão. |
| POST | `/auth/login` | Sim (`/auth-service/auth/login`) | Pública | Autentica o usuário no Keycloak e retorna um token JWT. |
| POST | `/auth/logout` | Sim (`/auth-service/auth/logout`) | JWT obrigatório | Revoga a sessão no Keycloak e grava o `jti` do token no Redis (TTL = vida restante do token). O gateway rejeitará o token com `401` imediatamente após o logout. |
| GET | `/test/public` | Sim (`/auth-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/auth-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## client-service
- **Responsabilidade:** domínio de clientes.
- **Banco dedicado:** `client-service-db`.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/test/public` | Sim (`/client-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/client-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## catalog-service
- **Responsabilidade:** catálogo de produtos e preços.
- **Banco dedicado:** `catalog-service-db`.
- **Persistência:** JPA + PostgreSQL (`products`). Dados iniciais carregados via `data.sql`.
- **Tratamento de erros:** RFC 7807 via `GlobalExceptionHandler` (`@RestControllerAdvice`). `ProductNotFoundException` → `404 Not Found` com `type: /errors/product-not-found.html`.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/products` | Sim (`/catalog-service/products`) | Pública | Lista todos os produtos. |
| GET | `/products/{id}` | Sim (`/catalog-service/products/{id}`) | Pública | Retorna produto pelo ID. `404` com ProblemDetail se não encontrado. |
| POST | `/products` | Sim (`/catalog-service/products`) | `owner` ou `admin` | Cria novo produto. Corpo: `{ "name": string, "price": number }`. Retorna `201 Created`. |
| PUT | `/products/{id}` | Sim (`/catalog-service/products/{id}`) | `owner` ou `admin` | Atualiza produto existente. `404` se não encontrado. |
| DELETE | `/products/{id}` | Sim (`/catalog-service/products/{id}`) | `owner` ou `admin` | Remove produto. Retorna `204 No Content`. |
| GET | `/test/public` | Sim (`/catalog-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/catalog-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## order-service
- **Responsabilidade:** pedidos e status.
- **Banco dedicado:** `order-service-db`.
- **Eventos outbound:** publica `ORDER_CREATED` em `Orders-Orchestrator.queue` após confirmação.
- **Eventos inbound:** consome `PAYMENT_APPROVED` e `PAYMENT_PENDING` de `Orchestrator-Orders.queue` para atualização automática de status.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/test/public` | Sim (`/order-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/order-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## payment-service
- **Responsabilidade:** processamento de pagamentos e pendências.
- **Banco dedicado:** `payment-service-db`.
- **Eventos inbound:**
  - `ORDER_CREATED` via `Orchestrator-Payments.queue`.
  - `PAYMENT_PENDING` via `Orchestrator-Payments-Worker.queue` (reprocessamento).
- **Eventos outbound:** publica `PAYMENT_APPROVED` e `PAYMENT_PENDING` em `Payments-Orchestrator.queue`.
- **Resiliência aplicada:** `Retry` + `Circuit Breaker` na integração com `procpag`, com fallback para pendência.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/test/public` | Sim (`/payment-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/payment-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## restaurant-service
- **Responsabilidade:** cadastro e gestão de restaurantes com controle de propriedade por owner.
- **Banco dedicado:** `restaurant-service-db`.
- **Tratamento de erros:** RFC 7807 via `GlobalExceptionHandler` (`@RestControllerAdvice`). `RestaurantNotFoundException` → `404`. `RestaurantAccessDeniedException` → `403 Forbidden`.
- **Política de acesso:**
  - `GET` — público (não requer autenticação).
  - `POST` — qualquer usuário autenticado. O criador é automaticamente adicionado à lista de `owners` do restaurante e recebe a role `owner` no Keycloak (via Admin API). Após criar, faça login novamente para obter um token JWT com a role `owner` atualizada.
  - `PUT` / `DELETE` — exige role `admin` **ou** pertencer à lista de `owners` do restaurante. Retorna `403` se o chamador não tiver permissão.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/restaurants` | Sim (`/restaurant-service/restaurants`) | Pública | Lista todos os restaurantes. Retorna array com campo `owners` (lista de UUIDs). |
| GET | `/restaurants/{id}` | Sim (`/restaurant-service/restaurants/{id}`) | Pública | Retorna restaurante pelo ID. `404` com ProblemDetail se não encontrado. |
| POST | `/restaurants` | Sim (`/restaurant-service/restaurants`) | Autenticado (qualquer role) | Cria novo restaurante. Criador vira owner automaticamente + role `owner` atribuída no Keycloak. Faça login novamente após criar para obter token com a nova role. Retorna `201 Created`. |
| PUT | `/restaurants/{id}` | Sim (`/restaurant-service/restaurants/{id}`) | `admin` ou owner do restaurante | Atualiza dados do restaurante. `403` se não for admin nem owner. `404` se não encontrado. |
| DELETE | `/restaurants/{id}` | Sim (`/restaurant-service/restaurants/{id}`) | `admin` ou owner do restaurante | Remove restaurante. `403` se não for admin nem owner. `404` se não encontrado. Retorna `204 No Content`. |
| GET | `/test/public` | Sim (`/restaurant-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/restaurant-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## orchestrator-service
- **Responsibility:** orchestrates event flow between `order-service`, `payment-service`, and `restaurant-service` via RabbitMQ.
- Consumes domain service events and coordinates order processing, validation, and payment sequencing.
- **Implemented payment routes:**
  - `Orders-Orchestrator.queue` -> `Orchestrator-Payments.queue` to start charging.
  - `Payments-Orchestrator.queue` -> `Orchestrator-Orders.queue` to send the payment outcome back to orders.
  - `Payments-Orchestrator.queue` -> `Orchestrator-Payments-Worker.queue` when type is `PAYMENT_PENDING`.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| N/A | N/A | Não | N/A | Serviço interno de orquestração de eventos; sem endpoints REST públicos. |

---

## Serviços de apoio (infra)

### redis
- Armazena a blacklist de tokens JWT após logout.
- Chave: `blacklist:<jti>` com TTL automático igual ao tempo restante de vida do token.
- Consultado pelo `TokenBlacklistFilter` do `api-gateway` antes de encaminhar qualquer requisição autenticada.

### rabbitmq
- Broker de mensageria assíncrona entre os serviços de domínio.
- Eventos: `pedido.criado`, `pagamento.aprovado`, `pagamento.pendente`.
- Filas principais do fluxo de pagamento:
  - `Orders-Orchestrator.queue`
  - `Orchestrator-Payments.queue`
  - `Payments-Orchestrator.queue`
  - `Orchestrator-Orders.queue`
  - `Orchestrator-Payments-Worker.queue`
- Acessível em `localhost:5672` (AMQP) e `localhost:15672` (Management UI).

### keycloak
- Provedor de identidade para autenticação/autorização e emissão de JWT.

### procpag
- Serviço externo de processamento de pagamento.

### Bancos dedicados
- `auth-service-db`
- `client-service-db`
- `catalog-service-db`
- `order-service-db`
- `payment-service-db`
- `restaurant-service-db`
- `keycloak-db`

> **Nota:** Redis não utiliza banco persistente de negócio — apenas armazena blacklist em memória com TTL.
