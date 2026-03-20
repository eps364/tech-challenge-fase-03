# Desenvolvimento e Execução

## 0) Documentação Swagger/OpenAPI

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

### Exemplos de ProblemDetail (RFC 7807)

Os endpoints seguem o padrão de erro RFC 7807. Exemplo de resposta:

```json
{
	"type": "https://example.com/errors/product-not-found.html",
	"title": "Product not found",
	"status": 404,
	"detail": "No product found with id=123",
	"instance": "/catalog-service/products/123"
}
```

Em caso de erro de permissão:

```json
{
	"type": "https://example.com/errors/access-denied.html",
	"title": "Access denied",
	"status": 403,
	"detail": "User does not have permission to update this restaurant",
	"instance": "/restaurant-service/restaurants/456"
}
```

## 1) Subir ambiente completo

Use o arquivo base + override de desenvolvimento:

```bash
docker compose -f compose.yml -f compose.dev.yml up -d
```

Se alterou o `compose.dev.yml`, recrie os containers:

```bash
docker compose -f compose.yml -f compose.dev.yml up -d --force-recreate
```

## 2) Logs dos serviços Java

```bash
docker compose -f compose.yml -f compose.dev.yml logs -f service-registry api-gateway auth-service order-service payment-service restaurant-service
```

Monitorar registros no Eureka em tempo real:

```bash
docker logs -f tech-challenge-fase-03-service-registry-1 2>&1 | grep "Registered instance"
```

## 3) Interface gráfica do Eureka

Acesse: [http://localhost:8762](http://localhost:8762)

Todos os microserviços devem aparecer com status `UP`.

| Serviço | Porta interna |
|---|---|
| `SERVICE-REGISTRY` | 8762 |
| `API-GATEWAY` | 8761 |
| `AUTH-SERVICE` | dinâmica (via Eureka) |
| `CLIENT-SERVICE` | dinâmica |
## 4) Fluxo de Pedido e Pagamento

O fluxo completo está ilustrado nos diagramas:
- [arquitetura-sequencia-pedido-pagamento.puml](diagrams/arquitetura-sequencia-pedido-pagamento.puml)
- [flow-order.puml](diagrams/flow-order.puml)

Principais etapas:
1. Cliente cria pedido (JWT via API Gateway).
2. Order Service valida restaurante e itens.
3. Evento `pedido.criado` publicado (RabbitMQ/Kafka).
4. Payment Service consome evento, tenta pagamento externo (ProcPag).
5. Em caso de falha/timeout/circuit breaker, pedido fica `PENDENTE_PAGAMENTO` e evento de pendência é publicado.
6. Worker reprocessa pendências automaticamente.
7. Status do pedido atualizado por eventos.

Pontos de resiliência:
- Retry, Timeout, Circuit Breaker (Resilience4j).
- Fallback para status pendente e reprocessamento.

## 5) Testes de API

Coleções de testes estão em [docs/API](API/). Utilize Bruno/Postman/Insomnia para validar endpoints, fluxos de sucesso e cenários de indisponibilidade.

## 6) Referência aos Diagramas

Todos os fluxos principais estão ilustrados em [docs/diagrams](diagrams/). Consulte os arquivos `.puml` para visualizar sequências de autenticação, pedido, pagamento e resiliência.
| `CATALOG-SERVICE` | dinâmica |
| `ORDER-SERVICE` | dinâmica |
| `PAYMENT-SERVICE` | dinâmica |
| `RESTAURANT-SERVICE` | dinâmica |
| `ORCHESTRATOR-SERVICE` | dinâmica |

## 4) Fluxo para refletir alterações no código (Hot Reload)

Com os serviços em `spring-boot:run` e Spring Boot DevTools ativo, ao salvar o código o Spring reinicia automaticamente.

Cada serviço monta apenas sua própria pasta, isolando o reload:

- Alterações em `order-service/` reiniciam somente `order-service`.
- Alterações em `auth-service/` reiniciam somente `auth-service`.
- O mesmo vale para os demais serviços Java.

> **Nota:** O DevTools monitora as **classes compiladas** (`target/classes`). IDEs com compilação automática (IntelliJ, VS Code + Extension Pack for Java) disparam o reload no save. Em editores de texto simples, execute `mvn compile` para refletir a mudança.

Se precisar forçar restart de um serviço específico:

```bash
docker compose -f compose.yml -f compose.dev.yml restart order-service
```

## 5) Cache de dependências Maven (m2-cache)

Na primeira subida, o Maven baixa todas as dependências. Nas próximas, tudo é servido do volume Docker `m2-cache`, eliminando o overhead de I/O do WSL.

O volume é compartilhado entre todos os serviços — dependências baixadas por um serviço ficam disponíveis aos demais.

Para inspecionar o volume:

```bash
docker volume inspect tech-challenge-fase-03_m2-cache
```

Para forçar re-download de todas as dependências (limpar cache):

```bash
docker volume rm tech-challenge-fase-03_m2-cache
```

## 6) Rebuild completo (quando mudar dependências/pom)

```bash
docker compose -f compose.yml -f compose.dev.yml down
docker compose -f compose.yml -f compose.dev.yml up -d --build
```

## 7) Parar ambiente

```bash
docker compose -f compose.yml -f compose.dev.yml down
```

Para parar e remover o cache de dependências:

```bash
docker compose -f compose.yml -f compose.dev.yml down -v
```

## Estrutura dos arquivos Compose

| Arquivo | Função |
|---|---|
| `compose.yml` | Configuração base: infraestrutura (BDs, Keycloak, RabbitMQ) + imagens de produção dos MSs |
| `compose.dev.yml` | Override de desenvolvimento: substitui MSs por containers Maven com hot reload |

## Observações

- O override substitui os serviços Java para rodarem com imagem `maven:3.9.9-eclipse-temurin-21` e `spring-boot:run`.
- Bancos, Keycloak, RabbitMQ e `procpag` continuam vindos do `compose.yml` base.
- Cada serviço monta seu módulo + `pom.xml` raiz (modo leitura) para manter a herança Maven do projeto pai.
- Todos os MSs que dependem do `service-registry` usam `condition: service_healthy`, garantindo ordem de inicialização correta.
- O `orchestrator-service` foi adicionado ao compose (incluindo `Dockerfile` próprio em `orchestrator-service/Dockerfile`).

## Payment Event Flow (RabbitMQ)

Flow implemented via orchestrator:

1. `order-service` confirms the order and publishes `ORDER_CREATED` to `Orders-Orchestrator.queue`.
2. `orchestrator-service` consumes and forwards it to `Orchestrator-Payments.queue`.
3. `payment-service` processes it through `procpag` with resilience (retry + circuit breaker).
4. `payment-service` publishes:
	- `PAYMENT_APPROVED` on success;
	- `PAYMENT_PENDING` on fallback.
5. `orchestrator-service` routes both to `Orchestrator-Orders.queue`.
6. `order-service` automatically updates status to `PAID` or `PENDING_PAYMENT`.
7. On `PAYMENT_PENDING`, the orchestrator also sends to `Orchestrator-Payments-Worker.queue` for worker reprocessing.

## Coleções Bruno

As coleções de teste manual estão em `docs/API` e seguem padronização visual no nome das requests.

Padrao aplicado:

- Primeiro icone: dominio da chamada (`🔐`, `🛡️`, `👤`, `📦`, `🧾`, `💳`, `🍽️`).
- Segundo icone: nivel de acesso (`🌐` publico, `🙋` user autenticado, `👑` owner/admin, `🛠️` admin).

Essa convenção facilita identificar rapidamente o contexto e a permissão antes de executar uma chamada.

## Flags Maven em uso no modo dev

| Flag | Descrição |
|---|---|
| `compile spring-boot:run` | Compila e inicia sem deletar classes anteriores |
| `-DskipTests` | Pula compilação e execução de testes |
| `-T 1C` | 1 thread por núcleo de CPU (paralelo) |
| `MAVEN_OPTS` | Ajuste de heap e compilação JIT incremental |
