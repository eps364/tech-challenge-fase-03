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

## 3) Fluxo para refletir alterações no código

Com os serviços em `spring-boot:run` e hot reload ativo, ao salvar o código o Spring reinicia automaticamente.

No setup atual, cada serviço monta apenas sua própria pasta. Isso isola o reload:

- Alterações em `order-service/` reiniciam somente `order-service`.
- Alterações em `auth-service/` reiniciam somente `auth-service`.
- O mesmo vale para os demais serviços Java.

Se precisar forçar restart de um serviço específico:

```bash
docker compose -f compose.yml -f compose.dev.yml restart order-service
```

## 4) Rebuild completo (quando mudar dependências/pom)

```bash
docker compose -f compose.yml -f compose.dev.yml down
docker compose -f compose.yml -f compose.dev.yml up -d --build
```

## 5) Parar ambiente

```bash
docker compose -f compose.yml -f compose.dev.yml down
```

## Observações

- O override substitui os serviços Java para rodarem com imagem Maven e comando `spring-boot:run`.
- Bancos, Keycloak e demais dependências continuam vindos do `compose.yml` base.
- Cada serviço monta seu módulo + `pom.xml` raiz em modo leitura para manter a herança Maven do projeto pai.
