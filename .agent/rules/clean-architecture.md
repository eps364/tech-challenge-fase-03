# Regra: Clean Architecture (Core/Infra)

Neste projeto de microsserviços, a arquitetura deve ser dividida rigorosamente entre **CORE** e **INFRA**.

## 1. Módulo Core
O `core` deve ser o coração da aplicação, livre de frameworks externos (sempre que possível) e focado em regras de negócio.
- **`core.domain`**: Contém as entidades, agregados, objetos de valor (VOs) e exceções de domínio.
- **`core.usecase`**: Contém os casos de uso (Lógica de Orquestração).
- **`core.gateway`**: Interfaces (Portas de Saída) que definem como o sistema interage com o externo (ex: `GerarPagamentoGateway`, `SalvarPedidoGateway`).

## 2. Módulo Infra
O `infra` contém as implementações técnicas e integrações.
- **`infra.persistence`**: Implementações de adaptadores para bancos de dados (JPA, Mongo, etc).
- **`infra.messaging`**: Implementações de produtores/consumidores Kafka.
- **`infra.gateway`**: Implementações reais das portas definidas no Core (consumo de APIs externas).
- **`infra.controller`**: Adaptadores de entrada (RESTEasy, Controllers Spring, Webflux).

## 3. Diretrizes de Injeção de Dependência
- O `core` não deve conhecer classes do `infra`.
- O `infra` conhece as interfaces do `core.gateway` para implementá-las.
- Configurações de Beans do Spring devem preferivelmente ficar em pacotes específicos de configuração no `infra` para injetar implementações `infra` nas portas do `core`.
