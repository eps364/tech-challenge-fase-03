# Roteiro Fluxo Feliz (Bruno)

Este roteiro orienta o teste do fluxo feliz do sistema usando a collection Bruno.

## Passos do Fluxo Feliz

1. **Registrar usuário**
   - Request: 🔐🌐 Auth Service - Register

   - Body exemplo:

```json
{
  "username": "userfeliz",
  "password": "senha123",
  "email": "userfeliz@example.com",
  "firstName": "Feliz",
  "lastName": "Usuario"
}
```


2. **Login**
   - Request: 🔐🌐 Auth Service - Login

   - Body exemplo:

```json
{
  "username": "userfeliz",
  "password": "senha123"
}
```

   - O token JWT será salvo automaticamente na variável `jwt_token`.

3. **Criar profile (CPF e endereço)**
   - Request: 👤🙋 Client - Create

   - Body exemplo:

```json
{
  "cpf": "12345678901",
  "address": {
    "street": "Rua das Flores",
    "number": "123",
    "neighborhood": "Centro",
    "complement": "Apto 101",
    "city": "Sao Paulo",
    "state": "SP",
    "zipCode": "01001-000"
  }
}
```

   - O `user_id` já estará preenchido pela collection após o login.

4. **Listar produtos**
   - Request: 📦🌐 Catalog - List Products
   - Copie um `productId` válido da resposta para usar no pedido.

5. **Listar restaurantes**
   - Request: 🍽️🌐 Restaurant - Listar Restaurantes
   - Copie um `restaurantId` válido da resposta para usar no pedido.

6. **Criar pedido de um produto de um restaurante válido**
   - Request: 🧾🙋 Order - Criar Pedido

   - Body exemplo:

```json
{
  "restaurantId": "<restaurantId>",
  "items": [
    {
      "productId": "<productId>",
      "quantity": 2
    }
  ]
}
```
   - Substitua `<restaurantId>` e `<productId>` pelos valores copiados dos passos anteriores.

7. **Ver meus pedidos**
   - Request: 🧾🙋 Order - Listar Meus Pedidos
   - Confirme que o pedido criado aparece na lista.

---

**Dica:** Execute cada request na ordem acima, usando as variáveis e exemplos para facilitar o teste integrado.
