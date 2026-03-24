# Projeto: Sistema de Pedidos Online - Tech Challenge Fase 03

## Equipe: Lista dos nomes e RMs dos alunos

| Nome | RM |
| --- | --- |
| Emerson Pereira da Silva | RM367268 |
| Luiz Octavio Tassinari Saraiva | RM367408|

# 1. Introdução

## 1.1. Descrição do problema

Restaurantes buscam uma solução unificada para pedidos online, reduzindo custos e aumentando a eficiência operacional. O desafio é criar uma plataforma distribuída, resiliente e segura, permitindo aos clientes realizar pedidos, pagamentos e acompanhar o status, mesmo diante de falhas em serviços externos.

**Repositório:** https://github.com/eps364/tech-challenge-fase-03

## 1.2. Objetivo do projeto

Implementar um sistema de pedidos online baseado em microsserviços, com autenticação JWT, comunicação assíncrona via eventos, resiliência a falhas e arquitetura limpa.

### Funcionalidades principais
- Cadastro e autenticação de clientes
- Criação e consulta de pedidos
- Catálogo de produtos
- Integração com serviço externo de pagamento
- Resiliência: fallback para status pendente e reprocessamento automático
- API Gateway, Service Registry, mensageria (RabbitMQ/Kafka), Redis (blacklist JWT)

Coleção Postman para testes: [`docs/TechChallenge-Fase03.postman_collection.json`](https://github.com/eps364/tech-challenge-fase-03/blob/main/docs/TechChallenge-Fase03.postman_collection.json)

# 2. Arquitetura do Sistema

## 2.1. Visão Geral

O sistema adota arquitetura de microsserviços, cada um com banco dedicado, comunicação via REST e eventos, autenticação centralizada (Keycloak), resiliência com Resilience4j e mensageria para integração assíncrona.

### Componentes principais
- **API Gateway**: Entrada única, validação JWT, roteamento, blacklist Redis
- **Service Registry (Eureka)**: Descoberta de serviços
- **Auth Service**: Autenticação/autorização
- **Client Service**: Cadastro e consulta de clientes
- **Catalog Service**: Produtos e preços
- **Order Service**: Criação e consulta de pedidos
- **Payment Service**: Processamento de pagamentos, fallback/resiliência
- **Restaurant Service**: Dados de restaurantes
- **Orchestrator Service**: Orquestração de pedidos
- **RabbitMQ/Kafka**: Eventos de pedido/pagamento
- **Redis**: Blacklist de tokens JWT
- **Keycloak**: Autenticação centralizada
- **procpag**: Serviço externo de pagamento

### Diagrama de fluxo principal

> TODO LINK
Ver [docs/diagrams/arquitetura-sequencia-pedido-pagamento.puml](https://github.com/eps364/tech-challenge-fase-03/blob/main/docs/diagrams/arquitetura-sequencia-pedido-pagamento.puml)

# 3. Endpoints Principais

> TODO LINK
Consulte Swagger UI de cada serviço:
- auth-service: http://localhost:8081/swagger-ui.html
- client-service: http://localhost:8082/swagger-ui.html
- catalog-service: http://localhost:8083/swagger-ui.html
- order-service: http://localhost:8084/swagger-ui.html
- payment-service: http://localhost:8085/swagger-ui.html
- restaurant-service: http://localhost:8086/swagger-ui.html
- orchestrator-service: http://localhost:8087/swagger-ui.html

Exemplo de endpoints do API Gateway:
| Método | Endpoint | Permissão | Descrição |
|---|---|---|---|
| POST | /auth-service/auth/login | Público | Autenticação/login |
| POST | /auth-service/auth/logout | JWT | Logout do usuário, revoga o token JWT, adiciona à blacklist e invalida a sessão |
| POST | /client-service/clients | Público | Cadastro de cliente |
| POST | /order-service/orders | JWT | Criação de pedido |
| GET | /order-service/orders/{id} | JWT | Consulta pedido por ID |
| POST | /payment-service/payments | JWT | Processa pagamento |

# 4. Eventos e Resiliência

> TODO LINK

- **Eventos**: `pedido.criado`, `pagamento.aprovado`, `pagamento.pendente` (RabbitMQ/Kafka)
- **Resiliência**: Retry, Timeout, Circuit Breaker (Resilience4j), fallback para status pendente, reprocessamento automático
- **Fluxo**: Pedido criado → evento → pagamento processado (ou pendente) → reprocessamento automático se necessário
- **Diagramas**: [docs/diagrams/arquitetura-sequencia-pedido-pagamento.puml](https://github.com/eps364/tech-challenge-fase-03/blob/main/docs/diagrams/arquitetura-sequencia-pedido-pagamento.puml)
- 


# 5. Banco de Dados e Containers

- Cada serviço possui banco Postgres dedicado (`<service>-db`), credenciais padrão: `postgres/password`
- Orquestração completa via `compose.yml` (infraestrutura) e `compose.dev.yml` (hot reload dos MSs Java)
- Serviços auxiliares: Redis, RabbitMQ, Keycloak, procpag
- Todos os serviços Spring Boot possuem Dockerfile multi-stage com stage `dev` para desenvolvimento/hot reload
- **No estágio dev, não há mais `COPY . .` — apenas `VOLUME /workspace`**. Assim, o código do host é montado diretamente no container, permitindo hot reload instantâneo sem duplicação de arquivos.
- O build de produção continua usando `COPY . .` para empacotar o código na imagem final.


## 6. Execução e Testes

### 6.1. Subir ambiente completo (Hot Reload)
```bash
docker compose -f compose.yml -f compose.dev.yml up --build --force-recreate
```

Esse comando:
- Garante rebuild das imagens de desenvolvimento (stage dev dos Dockerfiles)
- Sobe todos os serviços Java em modo hot reload (Spring Boot DevTools)
- Infraestrutura (DBs, Keycloak, RabbitMQ, etc) permanece igual ao compose base

Para parar:
```bash
docker compose -f compose.yml -f compose.dev.yml down
```

#### Coleções para teste:
- Collection Bruno [docs/API](https://github.com/eps364/tech-challenge-fase-03/tree/main/docs/API)
- Collection Postman [docs/TechChallenge-Fase03.postman_collection.json](https://github.com/eps364/tech-challenge-fase-03/blob/main/docs/TechChallenge-Fase03.postman_collection.json)


## 6.3. Testes automatizados
- Rodar testes: `./mvnw test`
- Cobertura: `./mvnw clean test jacoco:report`


# 7. Qualidade, Segurança e Práticas

- **Arquitetura Limpa**: Separação core/infra, domínio rico, DTOs em core, adapters em infra
- **Segurança**: JWT, RBAC, CORS, blacklist Redis, endpoints protegidos
	- **Logout seguro**: O endpoint `/auth-service/auth/logout` revoga o token JWT do usuário, adicionando o `jti` (jti, identificador único do token) à blacklist no Redis (com TTL igual ao tempo restante do token). O API Gateway bloqueia tokens revogados em todas as requisições, garantindo logout imediato e seguro.
- **Resiliência**: Circuit Breaker, Retry, Timeout, fallback, eventos de reprocessamento
- **Exception Handling**: RFC 7807 (ProblemDetail), handlers globais
- **Testes**: Unitários, integração, collections de API
- **Documentação**: Markdown em `/docs`, diagramas em `/docs/diagrams`, OpenAPI/Swagger


# 8. Repositório do Código

O Repositório: [https://github.com/eps364/tech-challenge-fase-03](https://github.com/eps364/tech-challenge-fase-03) é um fork do repositório base fornecido pelo professor, contendo a implementação completa do Tech Challenge Fase 03, seguindo os requisitos e boas práticas de desenvolvimento de software.
> Fork do repositório base: [https://github.com/proferickmuller/adjt-fase3-inicial](https://github.com/proferickmuller/adjt-fase3-inicial)


# 9. Vídeo Explicativo da API

> TODO LINK

# 10. Conclusão

O Tech Challenge Fase 03 entrega uma solução distribuída, resiliente e segura, com arquitetura limpa, eventos assíncronos, fallback e reprocessamento, pronta para ambientes produtivos e escaláveis. A implementação segue as melhores práticas de desenvolvimento, garantindo qualidade, segurança e facilidade de manutenção.
