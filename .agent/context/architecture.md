# Software Architecture - Tech Challenge Fase 03

## Distributed Architecture
- **Microservices**: Each service has its own responsibility and dedicated database.
- **Orchestration (Saga)**: Uses an central `orchestrator-service` to coordinate flows via **RabbitMQ**.
- **Communication Patterns**:
    - **Events (Kafka)**: Used for asynchronous broadcasts and eventual consistency (e.g., Client notification).
    - **Orchestration (RabbitMQ)**: Used for precise coordination between services in a workflow (Order -> Payment -> Kitchen).
    - **REST (OpenFeign)**: Used for synchronous integrations and data validation during order creation.
- **Modules**:
    - `api-gateway`: Entry point for all requests.
    - `auth-service`: Security, user management, and Keycloak integration.
    - `catalog-service`: Product and menu management.
    - `client-service`: Customer information.
    - `orchestrator-service`: Central dispatcher for cross-service workflows.
    - `order-service`: Order lifecycle management.
    - `payment-service`: Payment processing and external integration.
    - `service-registry`: Eureka for service discovery.

## Clean Architecture Layers (Mandatory)

### Core (Independent Core - Pure Java)
- **Regra de Ouro**: Nenhuma classe de `infra` (frameworks, bibliotecas externas, JDBC, JPA, Spring annotations, Jackson) pode ser usada dentro do `core`.
- **Independência**: O `core` deve possuir apenas **Java puro**. Isso garante que a regra de negócio seja testável e protegida contra mudanças tecnológicas.
- `core/domain/entity/`: Entidades de domínio ricas (contêm lógica e validações).
- `core/domain/valueobject/`: Objetos imutáveis com lógica de negócio.
- `core/usecase/`: Orquestração de regras de negócio da aplicação.
- `core/gateway/`: Interfaces (portas de saída) que definem o contrato para o mundo externo.
- `core/dto/`: DTOs de aplicação (Java `record` preferencialmente).

### Infra (Framework-specific)
- Toda tecnologia, framework ou biblioteca externa deve estar restrita a esta camada.
- `infra/entity/`: Entidades JPA com anotações de persistência.
- `infra/gateway/`: Adapters que implementam os gateways do `core`.
- `infra/web/controller/`: Controllers REST (Spring Boot).
- `infra/repository/`: Spring Data JPA repositories.
- `infra/adapters/inbound/messaging`: Consumidores RabbitMQ/Kafka.
- `infra/adapters/outbound/messaging`: Produtores RabbitMQ/Kafka.

## Mapping Standards
- **Manual Mapping**: Mapeamento entre Domain e Entity (Persistência) deve ser feito manualmente nos Adapters para evitar dependências de bibliotecas de mapeamento no `core`.
- **DTOs**: O `core` usa DTOs (Records) para entrada e saída de Use Cases.

## Saga Orchestration
- **Pattern**: Orchestrator-based Saga.
- **Workflow**: `OrderCreated` event -> `Orchestrator` -> `ProcessPayment` -> `PaymentApproved/Failed` -> `Orchestrator` -> `UpdateOrder`.
- **Resilience**: RabbitMQ DLQs and Retry Workers (as seen in `payment-service`).

## Database Pattern
- **Database per Service**: One Postgres database per microservice named `<service-name>-db`.
- **Credentials**: Standardized `postgres`/`password` for Docker environments.
