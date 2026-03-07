# Microservices - Visão Simplificada

Este documento resume os microservices do projeto e lista os endpoints disponíveis com permissão e descrição.

## api-gateway
- **Responsabilidade:** entrada única das APIs, roteamento, validação JWT e verificação de blacklist Redis.
- **Segurança:** valida assinatura JWT via Keycloak JWKS e, antes de encaminhar qualquer requisição autenticada, consulta o Redis para verificar se o `jti` do token foi blacklistado — retornando `401` imediatamente em caso positivo.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/auth-service/test/public` | Sim (este serviço) | Pública | Encaminha para endpoint público do `auth-service`. |
| GET | `/order-service/test/public` | Sim (este serviço) | Pública | Encaminha para endpoint público do `order-service`. |
| GET | `/payment-service/test/public` | Sim (este serviço) | Pública | Encaminha para endpoint público do `payment-service`. |
| GET | `/restaurant-service/test/public` | Sim (este serviço) | Pública | Encaminha para endpoint público do `restaurant-service`. |
| GET | `/client-service/test/public` | Sim (este serviço) | Pública | Encaminha para endpoint público do `client-service`. |
| GET | `/catalog-service/test/public` | Sim (este serviço) | Pública | Encaminha para endpoint público do `catalog-service`. |
| Todos | `/**` (demais rotas) | Sim (este serviço) | JWT obrigatório | Requisições não listadas como públicas exigem autenticação. |

## service-registry
- **Responsabilidade:** descoberta de serviços com Eureka.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| N/A | N/A | Não | N/A | Não há endpoints de negócio documentados neste projeto para o `service-registry`. |

## auth-service
- **Responsabilidade:** autenticação/autorização e integração com Keycloak.
- **Banco dedicado:** `auth-service-db`.

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

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/test/public` | Sim (`/catalog-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/catalog-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## order-service
- **Responsabilidade:** pedidos e status.
- **Banco dedicado:** `order-service-db`.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/test/public` | Sim (`/order-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/order-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## payment-service
- **Responsabilidade:** processamento de pagamentos e pendências.
- **Banco dedicado:** `payment-service-db`.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/test/public` | Sim (`/payment-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/payment-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## restaurant-service
- **Responsabilidade:** dados/validações de restaurante.
- **Banco dedicado:** `restaurant-service-db`.

| Método | Endpoint | Via Gateway | Permissão | Descrição |
|---|---|---|---|---|
| GET | `/test/public` | Sim (`/restaurant-service/test/public`) | Pública | Health/check simples de endpoint público do serviço. |
| GET | `/test/private` | Sim (`/restaurant-service/test/private`) | JWT obrigatório | Retorna dados do usuário autenticado e roles do token. |

## orchestrator-service
- **Responsabilidade:** orquestra o fluxo de eventos entre `order-service`, `payment-service` e `restaurant-service` via RabbitMQ.
- Consome eventos publicados pelos serviços de domínio e coordena a sequência de processamento de pedido, validação e pagamento.

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
