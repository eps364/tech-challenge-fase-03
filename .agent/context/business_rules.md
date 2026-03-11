# Business Rules - Tech Challenge Fase 03

## Domain Logic
- **Rich Domain Model**: Implementation of behavior directly in entities. 
    - *Note*: While mandatory for new development, some existing services (like `auth-service`) may still follow an anemic model; these should be refactored when possible.
- **Immutability**: Favor immutable entities (e.g., using `withStatus` patterns) as seen in `Order`.
- **Order Flow**: Traceability of order status must be preserved across services.
- **Client Identity**: Must be extracted from JWT tokens for all protected operations.

## Payment & Orchestration Flow
- **Orchestration**: The `orchestrator-service` coordinates multi-service flows (Saga pattern) using **RabbitMQ**.
- **Payment States**:
    - `PAYMENT_APPROVED`: Triggers order status update to paid.
    - `PAYMENT_PENDING`: Triggers fallback flow, marking order as pending and notifying a background worker.
- **Resilience**: 
    - When the external service is unavailable, order status must be `PENDENTE_PAGAMENTO`.
    - Pending payments must be enqueued for automatic reprocessing.

## Events
- `pedido.criado`: Published by `order-service` via Kafka.
- `pagamento.aprovado`: Published by `payment-service` via Kafka.
- `pagamento.pendente`: Published via Kafka/RabbitMQ for fallback.

