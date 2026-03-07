# Desenvolvimento com Docker Compose

## 1) Subir ambiente em modo desenvolvimento

Use o arquivo base + override de desenvolvimento:

```bash
docker compose -f compose.yml -f compose.dev.yml up -d
```

Se acabou de alterar o `compose.dev.yml`, recrie os containers:

```bash
docker compose -f compose.yml -f compose.dev.yml up -d --force-recreate
```

## 2) Ver logs dos serviços Java

```bash
docker compose -f compose.yml -f compose.dev.yml logs -f service-registry api-gateway auth-service order-service payment-service restaurant-service
```

Monitorar registros no Eureka em tempo real:

```bash
docker logs -f tech-challenge-fase-03-service-registry-1 2>&1 | grep "Registered instance"
```

## 3) Verificar interface gráfica do Eureka

Acesse no browser: [http://localhost:8762](http://localhost:8762)

Todos os microserviços devem aparecer na tabela **"Instances currently registered with Eureka"** com status `UP`:

| Serviço | Porta interna |
|---|---|
| `SERVICE-REGISTRY` | 8762 |
| `API-GATEWAY` | 8761 |
| `AUTH-SERVICE` | dinâmica (via Eureka) |
| `CLIENT-SERVICE` | dinâmica |
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

## Flags Maven em uso no modo dev

| Flag | Descrição |
|---|---|
| `compile spring-boot:run` | Compila e inicia sem deletar classes anteriores |
| `-DskipTests` | Pula compilação e execução de testes |
| `-T 1C` | 1 thread por núcleo de CPU (paralelo) |
| `MAVEN_OPTS` | Ajuste de heap e compilação JIT incremental |
