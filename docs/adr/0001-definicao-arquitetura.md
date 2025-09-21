# ADR 0001 – Definição da Arquitetura do Sistema

## Contexto
O projeto consiste em um Simulador de Crédito garantindo:
- Suporte evolução futura
- Facilidade da separação de responsabilidades
- Atendesse o prazo de entrega do projeto

## Decisão
Opteipor pela **Arquitetura em Camadas (Layered Architecture)**
As camadas principais são:
- **Controller (interface)** – entrada de requisições REST (Spring Boot)
- **Service (aplicação/negócio)** – contém a lógica principal da simulação
- **Domain (modelo)** – concentra as entidades e regras de negócio centrais

## Consequências
- ✅ Organização clara e bem conhecida pela comunidade Java
- ✅ Facilidade de leitura do código
- ❌ Menor flexibilidade para mudanças radicais em integrações (comparado com Hexagonal).
- ❌ Dependência mais forte entre camadas internas

## Alternativas Consideradas
- **Arquitetura Hexagonal (Ports & Adapters):**  
  Oferece melhor desacoplamento entre domínio e infraestrutura, mas aumenta a complexidade 
inicial e o tempo de desenvolvimento
  Avaliada como desnecessária para o escopo atual do projeto

## Status
Decisão adotada em **19/09/2025**.  
