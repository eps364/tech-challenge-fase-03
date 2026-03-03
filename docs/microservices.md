# Microservices - Visão Simplificada

Este documento resume os microservices do projeto e lista os endpoints disponíveis com permissão e descrição.

## api-gateway
- **Responsabilidade:** entrada única das APIs, roteamento e validação JWT.

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

---

## Serviços de apoio (infra)

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
