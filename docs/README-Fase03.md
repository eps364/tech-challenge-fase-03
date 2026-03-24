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
**Coleção Postman:** [`docs/TechChallenge-Fase03.postman_collection.json`](https://github.com/eps364/tech-challenge-fase-03/blob/main/docs/TechChallenge-Fase03.postman_collection.json)

## 1.2. Objetivo do projeto

Implementar um sistema de pedidos online baseado em microsserviços, com autenticação JWT, comunicação assíncrona via eventos, resiliência a falhas e arquitetura limpa.

### Funcionalidades principais
- Cadastro e autenticação de clientes
- Criação e consulta de pedidos
- Catálogo de produtos
- Integração com serviço externo de pagamento
- Resiliência: fallback para status pendente e reprocessamento automático
- API Gateway, Service Registry, mensageria (RabbitMQ/Kafka), Redis (blacklist JWT)



# 2. Arquitetura do Sistema

## 2.1. Visão Geral

#### Siglas e Significados

| Sigla | Significado |
|-------|-------------|
| JWT   | JSON Web Token |
| RBAC  | Role-Based Access Control |
| CORS  | Cross-Origin Resource Sharing |
| API   | Application Programming Interface |
| DB    | Database |
| CRUD  | Create, Read, Update, Delete |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |
| CRUD  | Create, Read, Update, Delete |
| API   | Application Programming Interface |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |
| CRUD  | Create, Read, Update, Delete |
| API   | Application Programming Interface |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |
| CRUD  | Create, Read, Update, Delete |
| API   | Application Programming Interface |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |
| CRUD  | Create, Read, Update, Delete |
| API   | Application Programming Interface |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |
| CRUD  | Create, Read, Update, Delete |
| API   | Application Programming Interface |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |
| CRUD  | Create, Read, Update, Delete |
| API   | Application Programming Interface |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |
| CRUD  | Create, Read, Update, Delete |
| API   | Application Programming Interface |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |
| CRUD  | Create, Read, Update, Delete |
| API   | Application Programming Interface |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |

O sistema adota arquitetura de microsserviços, cada um com banco dedicado, comunicação via REST e eventos, autenticação centralizada (Keycloak), resiliência com Resilience4j e mensageria para integração assíncrona.

### Componentes principais

#### Detalhamento dos Serviços

##### Mensageria: RabbitMQ e Kafka

O sistema utiliza dois mecanismos de mensageria para garantir flexibilidade, resiliência e desacoplamento entre os microserviços:

- **RabbitMQ**: Usado principalmente para garantir entrega confiável e ordenada de eventos críticos, como `pedido.criado` e `pagamento.pendente`. É empregado nos fluxos em que a confirmação de processamento é importante (ack/nack), permitindo reprocessamento automático em caso de falha. RabbitMQ é utilizado para filas de trabalho e integração entre serviços que exigem confirmação explícita de consumo.

- **Kafka**: Utilizado para transmissão de eventos de domínio e integração assíncrona entre múltiplos consumidores, como logs de eventos, auditoria e cenários onde a escalabilidade e o processamento paralelo são necessários. Kafka permite que múltiplos serviços consumam o mesmo evento de forma independente, garantindo alta disponibilidade e tolerância a falhas.

Ambos os sistemas podem ser utilizados em conjunto, dependendo do fluxo e da criticidade do evento, reforçando a resiliência e a escalabilidade da solução.

- **API Gateway**: Porta de entrada para todas as requisições externas. Realiza autenticação e autorização via JWT, roteia chamadas para os microserviços corretos, aplica blacklist de tokens revogados (Redis) e centraliza políticas de CORS e segurança.

- **Service Registry (Eureka)**: Responsável pelo registro e descoberta dinâmica dos microserviços, permitindo escalabilidade e balanceamento de carga sem dependência de endpoints fixos.

- **Auth Service**: Gerencia autenticação e autorização de usuários, geração e validação de tokens JWT, refresh tokens e integração com Keycloak. Implementa endpoints de login, registro, logout e refresh.

- **Client Service**: Mantém os dados cadastrais dos clientes, permitindo CRUD seguro via JWT. Expõe endpoints para consulta, atualização e remoção de perfis de clientes.

- **Catalog Service**: Gerencia o catálogo de produtos disponíveis para pedido, incluindo nome, preço e descrição. Permite consulta pública e serve de base para composição dos pedidos.

- **Order Service**: Responsável pela criação, consulta e atualização do status dos pedidos. Recebe eventos de pagamento, atualiza status e garante rastreabilidade do fluxo do pedido.

- **Payment Service**: Realiza a integração com o serviço externo de pagamento (procpag), aplicando resiliência (Retry, Timeout, Circuit Breaker). Publica eventos de pagamento aprovado ou pendente e gerencia reprocessamento automático.

- **Restaurant Service**: Gerencia dados dos restaurantes, como nome, endereço e cardápio associado. Permite cadastro, consulta e atualização por perfis autorizados.

- **Orchestrator Service**: Orquestra o fluxo de criação de pedidos, coordenando order-service e payment-service. Garante consistência de status, publica eventos e implementa lógica de fallback para resiliência.

- **RabbitMQ/Kafka**: Infraestrutura de mensageria para troca de eventos assíncronos entre os serviços, desacoplando fluxos críticos e permitindo reprocessamento.

- **Redis**: Utilizado para blacklist de tokens JWT revogados, garantindo logout imediato e seguro em todo o ecossistema.

- **Keycloak**: Provedor de identidade centralizado, gerencia usuários, papéis e autenticação federada, integrando-se ao Auth Service.

- **procpag**: Serviço externo simulado de processamento de pagamentos, utilizado para validar a resiliência do sistema diante de falhas e indisponibilidades.

### Diagramas dos principais Fluxos

Os principais fluxos do sistema estão ilustrados abaixo

#### Fluxo de Autenticação e Logout
<img src="diagrams/auth-logout-flow.svg" alt="Fluxo de Autenticação e Logout" style="max-width:100%;">

#### Fluxo de Registro de Usuário
<img src="diagrams/register-user-flow.svg" alt="Fluxo de Registro de Usuário" style="max-width:100%;">

#### Fluxo de Criação de Pedido
<img src="diagrams/order-create-flow.svg" alt="Fluxo de Criação de Pedido" style="max-width:100%;">

#### Fluxo de Pagamento com Resiliência
<img src="diagrams/payment-flow.svg" alt="Fluxo de Pagamento com Resiliência" style="max-width:100%;">

#### Fluxo de Consulta de Pedido
<img src="diagrams/get-order-flow.svg" alt="Fluxo de Consulta de Pedido" style="max-width:100%;">

Estes diagramas ilustram os principais cenários de autenticação, registro, criação de pedidos, processamento de pagamento (com fallback e reprocessamento) e consulta de status, alinhados à arquitetura distribuída e resiliente do projeto.

# 3. Endpoints Principais



## Endpoints Principais por Fluxo

Os endpoints abaixo refletem os fluxos dos diagramas apresentados:

### Autenticação e Logout
| Método | Endpoint | Permissão | Roles | Descrição |
|---|---|---|---|---|
| POST | /auth-service/auth/register | Público | - | Registro de novo usuário |
| POST | /auth-service/auth/login | Público | - | Autenticação/login |
| POST | /auth-service/auth/logout | JWT | user, owner, admin | Logout do usuário, revoga o token JWT, adiciona à blacklist e invalida a sessão |
| POST | /auth-service/auth/refresh | JWT (refresh) | user, owner, admin | Gera novo token JWT a partir do refresh token |

### Cadastro de Dados do Cliente
| Método | Endpoint | Permissão | Roles | Descrição |
|---|---|---|---|---|
| POST | /client-service/clients/{userId} | JWT | user | Cadastro de dados pessoais do cliente |
| GET | /client-service/clients/{userId} | JWT | user | Consulta dados do cliente |
| PUT | /client-service/clients/{userId} | JWT | user | Atualiza dados do cliente |
| DELETE | /client-service/clients/{userId} | JWT | user | Remove cadastro do cliente |

### Catálogo de Produtos
| Método | Endpoint | Permissão | Roles | Descrição |
|---|---|---|---|---|
| GET | /catalog-service/products | Público | - | Lista produtos disponíveis |
| GET | /catalog-service/products/{id} | Público | - | Consulta produto por ID |

### Criação e Consulta de Pedido
| Método | Endpoint | Permissão | Roles | Descrição |
|---|---|---|---|---|
| POST | /orchestrator-service/orchestrator/requests | JWT | user | Criação de pedido (via orquestrador) |
| GET | /order-service/orders/{id} | JWT | user | Consulta pedido por ID |
| GET | /order-service/orders | JWT | user | Lista pedidos do cliente autenticado |

### Pagamento
| Método | Endpoint | Permissão | Roles | Descrição |
|---|---|---|---|---|
| POST | /payment-service/payments | JWT | user | Processa pagamento de pedido |
| GET | /payment-service/payments/{id} | JWT | user | Consulta status do pagamento |

### Restaurantes
| Método | Endpoint | Permissão | Roles | Descrição |
|---|---|---|---|---|
| GET | /restaurant-service/restaurants | Público | - | Lista restaurantes |
| GET | /restaurant-service/restaurants/{id} | Público | - | Consulta restaurante por ID |
| POST | /restaurant-service/restaurants | JWT | owner, admin | Criação de restaurante |
| PUT | /restaurant-service/restaurants/{id} | JWT | owner, admin | Atualiza restaurante |
| DELETE | /restaurant-service/restaurants/{id} | JWT | owner, admin | Remove restaurante |

> Observação: O endpoint principal para criação de pedidos no fluxo resiliente é o do orchestrator-service, que orquestra a comunicação entre order-service e payment-service, garantindo resiliência e consistência de status.


# 4. Eventos e Resiliência

- **Eventos**: `pedido.criado`, `pagamento.aprovado`, `pagamento.pendente` (RabbitMQ/Kafka)
- **Resiliência**: Retry, Timeout, Circuit Breaker (Resilience4j), fallback para status pendente, reprocessamento automático
- **Fluxo**: Pedido criado → evento → pagamento processado (ou pendente) → reprocessamento automático se necessário

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


# 7. Qualidade, Segurança e Práticas

- **Arquitetura Limpa**: Separação core/infra, domínio rico, DTOs em core, adapters em infra
- **Segurança**: JWT, RBAC, CORS, blacklist Redis, endpoints protegidos
	- **Logout seguro**: O endpoint `/auth-service/auth/logout` revoga o token JWT do usuário, adicionando o `jti` (jti, identificador único do token) à blacklist no Redis (com TTL igual ao tempo restante do token). O API Gateway bloqueia tokens revogados em todas as requisições, garantindo logout imediato e seguro.
- **Resiliência**: Circuit Breaker, Retry, Timeout, fallback, eventos de reprocessamento
- **Exception Handling**: RFC 7807 (ProblemDetail), handlers globais
- **Documentação**: Markdown em `/docs`, diagramas em `/docs/diagrams`, OpenAPI/Swagger

# 8. Repositório do Código

O Repositório: [https://github.com/eps364/tech-challenge-fase-03](https://github.com/eps364/tech-challenge-fase-03) é um fork do repositório base fornecido pelo professor, contendo a implementação completa do Tech Challenge Fase 03, seguindo os requisitos e boas práticas de desenvolvimento de software.
> Fork do repositório base: [https://github.com/proferickmuller/adjt-fase3-inicial](https://github.com/proferickmuller/adjt-fase3-inicial)


# 9. Vídeo Explicativo da API

Vídeo explicativo detalhando a API, suas funcionalidades e como utilizá-la está disponível em:

**URL:** https://youtu.be/{link_do_video}


# 10. Conclusão

O Tech Challenge Fase 03 entrega uma solução distribuída, resiliente e segura, com arquitetura limpa, eventos assíncronos, fallback e reprocessamento, pronta para ambientes produtivos e escaláveis. A implementação segue as melhores práticas de desenvolvimento, garantindo qualidade, segurança e facilidade de manutenção.


---

## Siglas e Significados

| Sigla | Significado |
|-------|-------------|
| JWT   | JSON Web Token |
| RBAC  | Role-Based Access Control |
| CORS  | Cross-Origin Resource Sharing |
| API   | Application Programming Interface |
| DB    | Database |
| CRUD  | Create, Read, Update, Delete |
| RFC   | Request for Comments |
| TTL   | Time To Live |
| ACK   | Acknowledgment |
| NACK  | Negative Acknowledgment |
| MS    | Microservice |
| REST  | Representational State Transfer |
| CI/CD | Continuous Integration / Continuous Deployment |
| BI    | Business Intelligence |
| DevTools | Developer Tools |
| Dev   | Development |
| DBMS  | Database Management System |
| DBaaS | Database as a Service |
