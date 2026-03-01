# Tech Challenge Fase 03 - Instruções para o Copilot

Este arquivo define como o Copilot deve atuar neste repositório.
O objetivo é manter as entregas aderentes ao desafio da Fase 3: arquitetura distribuída, segurança com JWT, comunicação assíncrona e resiliência.

## 1) Contexto do desafio

Implementar um sistema de pedidos online para restaurante com múltiplos serviços em Java, cobrindo:

- criação de clientes e autenticação;
- autenticação/autorização de usuários;
- criação e consulta de pedidos;
- integração com serviço externo de pagamento;
- tratamento de indisponibilidade com status pendente;
- reprocessamento automático via eventos.

## 2) Objetivo da fase

Construir uma solução resiliente e segura, com fluxo completo de pedido e pagamento, capaz de tolerar falhas/timeout e manter consistência de status entre serviços.

## 3) Requisitos de entrega e avaliação

Sempre considerar estes itens como obrigatórios nas sugestões do Copilot:

- aplicação funcionando com os serviços necessários;
- arquivo de testes de endpoint (Bruno/Postman/Insomnia);
- `compose.yml` para subir o ambiente completo com um comando;
- documentação da arquitetura e do fluxo principal;
- identificação dos pontos de resiliência;
- código-fonte versionado de todos os componentes implementados.
- criação de diagramas de sequencia na pasta `docs/diagrams` para ilustrar os fluxos de pedido e pagamento, destacando os eventos e pontos de resiliência.
- cada microserviço deve respeitar a arquitetura limpa, com separação clara entre domínio e infraestrutura, e seguir as melhores práticas de segurança e resiliência (pastas core/infra)
- cada microservice possue um banco de dados dedicado, seguindo o padrão de nomenclatura `<service-name>-db` e utilizando as mesmas credenciais (postgres/password) para facilitar a configuração e execução via Docker Compose.

## 4) Requisitos Funcionais (RF)

### RF01 - Gerenciamento de usuários
- Criar cliente.
- Autenticar cliente.

### RF02 - Criar pedido
Ao criar pedido, considerar:
- ID do cliente extraído do token JWT;
- dados do restaurante;
- lista de itens com: `idProduto`, `nome`, `quantidade`, `preco`.

Resultado esperado:
- cálculo do valor total;
- retorno com `idPedido` e `valorTotal`;
- pedido de confirmação do pedido.

Observação: não é obrigatório CRUD completo de itens; pode existir catálogo pré-definido.
- criar catalog-service para manter produtos e preços

### RF03 - Consultas
- Consultar pedido por ID.
- Consultar pedidos do cliente autenticado.

### RF04 - Processamento de pagamento
- Integrar com serviço externo `processamento-pagamento-externo` (fornecido pelos professores).
- O serviço externo é eventualmente disponível.
- Quando disponível, o pagamento deve ser autorizado.

### RF05 - Pagamento pendente
Quando houver falha/timeout/circuito aberto no pagamento:
- não falhar o fluxo do pedido;
- marcar pedido como `PENDENTE_PAGAMENTO`;
- publicar/enfileirar pendência para reprocessamento.

### RF06 - Reprocessamento automático
Quando o serviço de pagamento normalizar:
- reprocessar pendências automaticamente;
- atualizar status para `PAGO` quando confirmado.

### RF07 - Atualização automática de status
Após confirmação de pagamento:
- atualizar status do pedido automaticamente;
- opcionalmente notificar/encadear próximos serviços (ex.: produção).

## 5) Requisitos Não Funcionais (RNF)

### RNF01 - Arquitetura em múltiplos serviços
No mínimo:
- serviço de autenticação;
- `order-service`;
- `payment-service` com integração externa.

Opcional no desafio, mas presente neste repositório:
- `restaurant-service`;
- `catalog-service`;
- `api-gateway`;
- `service-registry`.

### RNF02 - Segurança com Spring Security + JWT

- Login com geração/validação de token JWT.
- Perfis de acesso (ex.: client/owner/admin).
- Endpoints de pedido protegidos.
- Endpoints login/register abertos.
- Identidade do cliente deve vir do token.

### RNF03 - Comunicação assíncrona com Kafka
Eventos obrigatórios:
- `pedido.criado`;
- `pagamento.aprovado`;
- `pagamento.pendente`.

Fluxo mínimo esperado:
- `order-service` publica `pedido.criado`;
- `payment-service` consome, tenta pagamento externo e publica resultado;
- worker/consumidor reprocessa pendências;
- `order-service` atualiza status por eventos.

### RNF04 - Resiliência com Resilience4j
Na integração de pagamento aplicar:
- Circuit Breaker;
- Retry;
- Timeout;
- Fallback para `PENDENTE_PAGAMENTO` + publicação de pendência.

### RNF05 - Boas práticas de arquitetura
Seguir separação em camadas/Clean ou Hexagonal:
- `controller` (entrada);
- `service` / `use case` (orquestração);
- `domain` (regras de negócio);
- `infra` (integrações externas, persistência, mensageria).

## 6) Diretrizes específicas deste repositório

### 6.1 Estrutura principal
Trabalhar respeitando os módulos existentes:

- `api-gateway`
- `auth-service`
- `order-service`
- `payment-service`
- `restaurant-service`
- `service-registry`

### 6.2 Convenções de implementação
- Preferir mudanças pequenas, focadas e coerentes com o módulo alvo.
- Não misturar responsabilidades entre serviços.
- Evitar acoplamento direto quando o fluxo exigir evento assíncrono.
- Manter contratos de API estáveis (DTOs, payloads, status).
- Atualizar documentação ao alterar fluxos, endpoints ou variáveis de ambiente.

### 6.3 Banco de dados e containers
- Considerar bancos dedicados por serviço no Compose.
- Preservar a execução integrada por `docker compose`.
- Não introduzir dependências que quebrem o ambiente atual sem necessidade.

## 7) Testes e qualidade

- Sempre que alterar regra de negócio, adicionar/ajustar testes unitários.
- Em fluxos entre serviços, priorizar testes de integração/contrato quando viável.
- Validar cenário de sucesso e cenário de indisponibilidade no pagamento.
- Garantir que o build dos módulos alterados continue passando.

## 8) Documentação obrigatória

Ao propor mudanças relevantes, manter documentação alinhada com:

- arquitetura (componentes/fluxo);
- tópicos/eventos e responsabilidades por serviço;
- estratégia de resiliência;
- instruções de execução local e via Docker;
- testes de API (coleções existentes em `docs/API`).

## 9) Diretrizes de commits

Nunca commitar sem revisão humana.

- mensagens de commit em inglês;
- agrupar alterações por unidade lógica;
- seguir Conventional Commits.

Formato:

```text
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

Tipos permitidos:
- `feat`
- `fix`
- `docs`
- `style`
- `refactor`
- `test`
- `chore`

Exemplos:

```text
feat(order): add pending payment fallback flow
fix(payment): handle timeout from external processor
docs(architecture): update event-driven payment sequence
test(order): add integration test for payment pending scenario
```

## 10) Regra de ouro para o Copilot

Toda sugestão de código deve preservar os objetivos da Fase 3:

- cada microservice deve estar alinhado com arquitetura limpa (core/infra);
- segurança (JWT);
- resiliência (fallback + reprocessamento);
- rastreabilidade de status do pedido;
- clareza arquitetural em microsserviços.
