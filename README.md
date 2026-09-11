# Sistema Impacta 🌱

Projeto acadêmico da disciplina de **Programação Orientada a Objetos (POO Avançado)** — UNIFACISA.

O sistema simula o gerenciamento de ações voluntárias de impacto socioambiental (plantio de mudas, mutirões de reciclagem e oficinas ecológicas), permitindo o cadastro de voluntários, inscrição em ações e cálculo de pontuação de impacto acumulado.

## ✨ Funcionalidades

- Cadastro de voluntários, com validação de nome e e-mail e geração automática de matrícula.
- Cadastro de três tipos de ação, cada uma com sua própria regra de pontuação:
    - **Plantio de Mudas** — pontuação baseada na quantidade de mudas plantadas.
    - **Mutirão de Reciclagem** — pontuação baseada na duração (horas).
    - **Oficina Ecológica** — pontuação baseada na duração, com bônus se houver kit de material.
- Inscrição de voluntários em ações, com validações de:
    - ação/voluntário inexistentes;
    - inscrição duplicada;
    - ação lotada (capacidade máxima atingida).
- Cálculo e acúmulo automático da pontuação de impacto de cada voluntário ao se inscrever em uma ação.
- Listagem de voluntários ordenada por pontuação (decrescente) e, em caso de empate, por nome.
- Exibição de detalhes de ações e voluntários.

## 🧱 Arquitetura

O projeto segue o paradigma de orientação a objetos, com separação entre **modelo**, **serviço** e **exceções**:

```
src/br/unifacisa/
├── model/
│   ├── Acao.java                 # Classe abstrata base das ações
│   ├── PlantioDeMudas.java       # Acao -> Plantio de mudas
│   ├── MutiraoDeReciclagem.java  # Acao -> Mutirão de reciclagem
│   ├── OficinaEcologica.java     # Acao -> Oficina ecológica
│   └── Voluntario.java           # Dados e progresso do voluntário
├── service/
│   ├── AcaoService.java          # Regras de negócio das ações
│   └── VoluntarioService.java    # Regras de negócio dos voluntários
├── exception/
│   ├── AcaoLotadaException.java
│   ├── EmailCadastradoException.java
│   └── EmailInvalidoException.java
└── test/
    ├── AcaoTest.java             # Testes unitários de Acao/AcaoService
    └── VoluntarioTest.java       # Testes unitários de Voluntario/VoluntarioService
```

### Modelo de domínio

- `Acao` é uma classe **abstrata** que centraliza os dados comuns a toda ação (título, descrição, data, capacidade máxima, voluntários inscritos) e define o método abstrato `calcularPontuacao()`, implementado de forma diferente por cada subtipo:

  | Tipo de Ação            | Fórmula de pontuação                          |
    |--------------------------|------------------------------------------------|
  | Plantio de Mudas         | `5 + (2 × quantidade de mudas)`                |
  | Mutirão de Reciclagem    | `4 × duração em horas`                         |
  | Oficina Ecológica        | `3 × duração em horas` (+10 se houver kit)     |

- `Voluntario` guarda nome, e-mail, matrícula (gerada automaticamente no formato `2026XXX`), quantidade de ações realizadas e pontuação de impacto acumulada.

### Camada de serviço

- `VoluntarioService` cuida do cadastro e da busca de voluntários, lançando `EmailInvalidoException` (e-mail sem `@`) ou `EmailCadastradoException` (e-mail já existente).
- `AcaoService` cuida do cadastro das ações (delegando a criação ao tipo correto) e da inscrição de voluntários, lançando `AcaoLotadaException` quando a capacidade máxima já foi atingida.

## ✅ Testes

Os testes automatizados usam **JUnit Jupiter (JUnit 5/6)** e cobrem:

- Cadastro de cada tipo de ação e validações de entrada (título vazio, capacidade inválida).
- Geração de IDs únicos para as ações.
- Inscrição de voluntários (sucesso, ação/voluntário inexistente, inscrição duplicada, ação lotada).
- Cálculo correto da pontuação para cada tipo de ação.
- Cadastro de voluntários e suas exceções (e-mail inválido, e-mail duplicado, nome vazio).
- Ordenação da listagem de voluntários por pontuação.