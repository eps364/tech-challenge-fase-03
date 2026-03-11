# Tech Challenge - Fase 03

Arquitetura de microsserviços com Spring Boot, Service Registry (Eureka), API Gateway, autenticação com Keycloak e serviço de pagamento externo (`procpag`).

## Visão geral

Este repositório contém os serviços:

- `service-registry` (Eureka)
- `api-gateway`
- `auth-service`
- `client-service`
- `catalog-service`
- `order-service`
- `payment-service`
- `restaurant-service`
- `orchestrator-service`
- `keycloak` + `keycloak-db`
- `redis` (blacklist de tokens JWT)
- `rabbitmq` (mensageria assíncrona)
- `procpag` (serviço externo de pagamento)
- bancos dedicados por serviço (`*-service-db`)

## Pré-requisitos

- Docker + Docker Compose
- Java 21 (para execução local com Maven Wrapper)

## Subindo com Docker Compose

Na raiz do projeto:

```bash
docker compose up --build
```

Para parar:

```bash
docker compose down
```

## Modo desenvolvimento

Para desenvolvimento com hot reload em containers, use o guia:

- [docs/development.md](docs/development.md)

## Testes de API (Bruno)

As chamadas de API para validação manual estão em `docs/API`.

Convenção visual dos nomes das requests no Bruno:

| Tipo | Icone | Significado |
|---|---|---|
| Dominio | `🔐` | Auth |
| Dominio | `🛡️` | Keycloak |
| Dominio | `👤` | Client |
| Dominio | `📦` | Catalog |
| Dominio | `🧾` | Order |
| Dominio | `💳` | Payment |
| Dominio | `🍽️` | Restaurant |
| Permissao | `🌐` | Publico (sem login) |
| Permissao | `🙋` | Usuario autenticado (`user`) |
| Permissao | `👑` | Owner ou Admin |
| Permissao | `🛠️` | Somente Admin |

Exemplos de nomenclatura:

- `🧾 🙋 Order - Criar Pedido`
- `📦 👑 Catalog - Create Product`
- `🍽️ 🛠️ Restaurant - Adicionar Owner`

## Endereços dos serviços

| Serviço | URL |
|---|---|
| Keycloak Admin | http://localhost:8080/admin |
| Service Registry (Eureka) | http://localhost:8762 |
| API Gateway | http://localhost:8761 |
| RabbitMQ Management | http://localhost:15672 |
| ProcPag OpenAPI | http://localhost:8089/openapi.yml |
| Redis | localhost:6379 |
| AkHQ | http://localhost:8085 |
| RabbitMQ | http://localhost:15672 |

## Segurança — Invalidação de Token (Logout)

Após `POST /auth-service/auth/logout`, o token JWT emitido é imediatamente invalidado:

1. O `auth-service` revoga a sessão no Keycloak e grava o `jti` do token no **Redis** com TTL igual ao tempo restante de vida do token.
2. O `api-gateway` verifica toda requisição entrante contra a blacklist Redis antes de encaminhar — tokens blacklistados recebem `401 Unauthorized` imediatamente, independente de ainda estarem dentro da validade Keycloak.

## Segurança — Regras de Rotas Dinâmicas no Gateway

O `api-gateway` possui regras de autorizacao de rota carregadas dinamicamente do Redis (chave `gateway:security:rules`).

Com isso, a manutencao das permissoes de endpoint pode ser feita sem alteracao de codigo da aplicacao.

Endpoint administrativo (somente `admin`):

- `GET /gateway/security/routes` - lista regras atuais.
- `POST /gateway/security/routes` - cria uma regra e gera `id` inteiro automaticamente.
- `PUT /gateway/security/routes/{id}` - atualiza uma regra especifica por `id`.
- `DELETE /gateway/security/routes/{id}` - remove uma regra especifica por `id`.
- `PUT /gateway/security/routes` - substitui a lista inteira de regras (modo lote).

Campos de cada regra:

- `id`: identificador inteiro da regra (usado para update/delete).
- `method`: metodo HTTP (`GET`, `POST`, `PUT`, `DELETE`, etc) ou `null` para qualquer metodo.
- `pathPattern`: padrao de rota (`/catalog-service/products/**`).
- `access`: `PERMIT_ALL`, `AUTHENTICATED` ou `HAS_ANY_ROLE`.
- `roles`: lista de papeis exigidos quando `access=HAS_ANY_ROLE`.

Observacoes:

- Se nao houver regras no Redis, o gateway aplica um conjunto padrao equivalente ao comportamento anterior hardcoded.
- O `PUT` substitui toda a configuracao, entao mantenha regras de administracao para evitar lockout.
- Requests Bruno para administracao de regras:
	- `docs/API/auth/gateway-security-routes-list.bru`
	- `docs/API/auth/gateway-security-routes-update-permit-all.bru`
	- `docs/API/auth/gateway-security-routes-update-has-any-role.bru`
	- `docs/API/auth/gateway-security-routes-update-by-id.bru`
	- `docs/API/auth/gateway-security-routes-delete-by-id.bru`

## Usuários e senhas

A tabela completa de serviços, URLs e credenciais está em:

- [docs/services/services.md](docs/services/services.md)

Resumo de microservices, endpoints, permissões e acesso via gateway:

- [docs/microservices.md](docs/microservices.md)

## Banco de dados

Cada microsserviço de domínio utiliza um Postgres dedicado no Compose:

- `auth-service-db` (banco: `auth-service-db`)
- `client-service-db` (banco: `client-service-db`)
- `catalog-service-db` (banco: `catalog-service-db`)
- `order-service-db` (banco: `order-service-db`)
- `payment-service-db` (banco: `payment-service-db`)
- `restaurant-service-db` (banco: `restaurant-service-db`)

Todos usam o mesmo padrão de credenciais:

- Usuário: `postgres`
- Senha: `password`

> Observação: esses bancos estão expostos na rede interna do Docker (`fase3net`).

## Execução local (sem Docker para os apps Java)

Você pode subir somente dependências em Docker e rodar os apps com Maven Wrapper:

```bash
docker compose up -d keycloak keycloak-db auth-service-db client-service-db catalog-service-db order-service-db payment-service-db restaurant-service-db
./mvnw spring-boot:run
```

Para cada microserviço, execute o wrapper dentro da pasta do serviço, por exemplo:

```bash
cd auth-service
./mvnw spring-boot:run
```

## Observações

- O projeto Compose está nomeado como `tech-challenge-fase-03`.
- Os nomes de containers seguem o padrão `tech-challenge-fase-03-{service}-{instancia}`.


# Mensageria

- AkHQ: http://localhost:8085

- RABBIT MQ: http://localhost:15672