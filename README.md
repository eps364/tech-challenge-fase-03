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

## Endereços dos serviços

| Serviço | URL |
|---|---|
| Keycloak Admin | http://localhost:8080/admin |
| Service Registry (Eureka) | http://localhost:8762 |
| API Gateway | http://localhost:8761 |
| RabbitMQ Management | http://localhost:15672 |
| ProcPag OpenAPI | http://localhost:8089/openapi.yml |
| Redis | localhost:6379 |

## Segurança — Invalidação de Token (Logout)

Após `POST /auth-service/auth/logout`, o token JWT emitido é imediatamente invalidado:

1. O `auth-service` revoga a sessão no Keycloak e grava o `jti` do token no **Redis** com TTL igual ao tempo restante de vida do token.
2. O `api-gateway` verifica toda requisição entrante contra a blacklist Redis antes de encaminhar — tokens blacklistados recebem `401 Unauthorized` imediatamente, independente de ainda estarem dentro da validade Keycloak.

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