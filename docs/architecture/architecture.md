# Arquitetura Proposta

![Exemplo de requisição e resposta no
Postman](docs/imagens/diagrama_em_camadas.png)

## 1. Visão Geral das Camadas

Aqui a ideia central é simples: separar responsabilidades de forma clara para manter a 
organização e facilitar a manutenção do sistema

### **Interface (UI)**

-   Pode ser Web ou Mobile, tanto faz. É onde o usuário vai clicar,
    digitar e ver resultado.
-   Coleta dados financeiros, mostra propostas e cuida de
    login/cadastro.
-   Sempre conversa com o sistema via **API Gateway** (ninguém pula fila
    aqui).

### **API Gateway**

-   É o porteiro do sistema. Todo mundo passa por ele.
-   Faz autenticação, controla acesso, gera log, evita abuso de
    requisições.
-   Resumindo: dá segurança, organiza e facilita integrar tanto web
    quanto mobile.

### **Serviços (Aplicação)**

-   **Usuário**:
    -   Cuida de cadastro, login, perfil e histórico.
    -   Endpoints básicos:
        -   `POST /users`
        -   `GET /users/{id}`
        -   `PUT /users/{id}`
        -   `DELETE /users/{id}`
-   **Simulação**:
    -   Recebe dados, gera simulação de empréstimo e guarda histórico do
        usuário.
    -   Endpoints:
        -   `POST /simulations` → cria simulação nova
        -   `GET /simulations` → lista simulações de um usuário

### **Persistência**

-   **Banco Relacional (Usuários)**: pra login, cadastro e dados que
    precisam ser consistentes.
-   **Relacional ou NoSQL (Simulações)**: flexível, já que o formato
    pode variar (JSON com regras diferentes).

### **Serviços Externos / Assíncronos**

-   **Fila de Mensagens (SQS/Kafka)**: processa simulações sem travar o
    sistema. Ideal pra alto volume.
-   **Cache (Redis/ElastiCache)**: acelera quando o usuário insiste em
    simular mil vezes seguidas.

------------------------------------------------------------------------

## 2. Escolhas de Tecnologia

Cada camada tem sua função bem definida, sem sobreposição desnecessária.
- **Arquitetura em camadas**: facilita a manutenção e evolução do sistema de forma segura.
- **API Gateway (AWS)**: centraliza autenticação, segurança e monitoramento.
- **Banco relacional para usuários**: garante consistência e confiabilidade em dados sensíveis, como login e cadastro.
- **Banco flexível para simulações**: liberdade pra salvar dados variados.
- **Cache**: reduz a latência e melhora a experiência do usuário em consultas frequentes.
- **Fila de mensagens**: possibilita o processamento assíncrono, garantindo resiliência
e desempenho em casos de picos de acesso.

------------------------------------------------------------------------

## 3. Boas Práticas

-   **Repository Pattern**: banco separado da regra de negócio.
-   **DTOs**: segurança na troca de dados.
-   **Circuit Breaker / Retry**: não confiar cegamente em serviços externos.
-   **JWT**: autenticação simples e escalável.
-   **Least Privilege**: cada papel só acessa o que precisa (admin não é usuário comum).

------------------------------------------------------------------------

## 4. Escalabilidade

-   **Containers (ECS/EKS)**: escalar microsserviços na horizontal.
-   **Auto Scaling**: sobe ou desce instâncias conforme uso real.
-   **Banco escalável (Aurora/DynamoDB)**: cada tipo de dado no banco certo.
-   **Cache distribuído**: alivia pressão do banco.
-   **Fila de mensagens**: segura os picos de requisições sem travar
    tudo.

------------------------------------------------------------------------

## 5. Exemplos de API

### Criar simulação

**Request**

``` json
POST /simulations
{
  "userId": "123",
  "valor": 10000,
  "prazoMeses": 24,
  "rendaMensal": 5000
}
```

**Response**

``` json
{
  "simulationId": "abc123",
  "parcelas": 24,
  "valorParcela": 520.45,
  "taxaJuros": 1.8,
  "status": "PROCESSADO"
}
```

### Listar simulações do usuário

**Request**

``` http
GET /simulations?userId=123
```

**Response**

``` json
[
  {
    "simulationId": "abc123",
    "valor": 10000,
    "prazoMeses": 24,
    "valorParcela": 520.45
  },
  {
    "simulationId": "def456",
    "valor": 5000,
    "prazoMeses": 12,
    "valorParcela": 430.20
  }
]
```

------------------------------------------------------------------------
