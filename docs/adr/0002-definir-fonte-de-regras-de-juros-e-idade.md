# ADR 0002 – Definição da fonte de regras de idade e juros

## Contexto
O sistema de simulação de crédito exige regras de intervalo de idade e percentuais de juros.  
Essas regras devem ser configuráveis, mas o prazo do projeto exige entrega rápida da primeira versão.

## Decisão
Optei por implementar as regras em um `enum` Java, pois:
- Permite validação simples e rápida
- Acelera o tempo de desenvolvimento
- Facilita testes unitários iniciais

## Consequências
- ✅ Desenvolvimento mais rápido para atender o prazo
- ✅ Código simples e legível
- ❌ Regras fixas exigem alteração de código e novo deploy para mudanças
- ❌ Dificuldade de manutenção por usuários não técnicos

## Alternativas Consideradas
- **Banco de Dados (planejado para o futuro):**  
  Permitir armazenamento dinâmico das regras de juros e idade
  Isso permite que novas configurações sejam feitas sem perder histórico das simulações já realizadas

## Status
Decisão adotada em **19/09/2025**.  
Planejamento de evolução: mover regras para um banco de dados