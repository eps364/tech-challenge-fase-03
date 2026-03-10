# Antigravity Instructions - Tech Challenge Fase 03

Este arquivo define as diretrizes obrigatórias para a atuação do Antigravity neste repositório, focando nos objetivos da Fase 3: arquitetura distribuída, segurança, comunicação assíncrona e resiliência.

## 1. Arquitetura e Estrutura
- **Clean Architecture**: Cada microsserviço deve seguir a separação clara entre `core` (domínio/casos de uso) e `infra` (persistência, mensageria, gateways externos).
- **Módulos Existentes**: Respeitar a estrutura de: `api-gateway`, `auth-service`, `catalog-service`, `client-service`, `orchestrator-service`, `order-service`, `payment-service`, `restaurant-service`, `service-registry`.
- **Banco de Dados**: Um banco por serviço (`<service-name>-db`), usando Postgres com credenciais padrão (`postgres`/`password`) para ambiente Docker.

## 2. Requisitos de Resiliência (Resilience4j)
Na integração com o serviço de pagamento externo:
- **Circuit Breaker**: Impedir chamadas a serviços instáveis.
- **Retry & Timeout**: Configurar tentativas de reexecução e tempos limite.
- **Fallback**: Em caso de falha/timeout, o status deve ser `PENDENTE_PAGAMENTO` e uma mensagem deve ser enfileirada para reprocessamento automático quando o serviço normalizar.

## 3. Comunicação Assíncrona (Kafka)
Eventos obrigatórios e fluxo:
- `pedido.criado` (publicado pelo `order-service`).
- `pagamento.aprovado` (publicado pelo `payment-service`).
- `pagamento.pendente` (publicado em caso de fallback).
- Evitar acoplamento direto; preferir eventos para manter a consistência eventual.

## 4. Segurança (Spring Security + JWT)
- Identidade do cliente extraída obrigatoriamente do token JWT.
- Diferenciação de perfis (client/owner/admin).
- Endpoints de login/registro abertos; pedidos/pagamentos protegidos.

## 5. Padrões de Desenvolvimento e Qualidade
- **Conventional Commits**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`. Mensagens em inglês.
- **Documentação**: Manter diagramas de sequência em `docs/diagrams` atualizados.
- **Testes**: Sempre adicionar ou ajustar testes unitários ao alterar regras de negócio. Priorizar testes de integração para fluxos entre serviços.
- **Ambiente**: Garantir que o `compose.yml` e o build (`./mvnw clean install`) continuem funcionando após alterações.

## 6. Regra de Ouro
Toda sugestão de código deve preservar a rastreabilidade do status do pedido e a clareza arquitetural dos microsserviços. Não introduzir dependências que quebrem a execução via Docker Compose.
