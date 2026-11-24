# Sistema de Gestão para Academia de Artes Marciais

[![Build](https://github.com/AguaBatizada/sistema-academia-artes-marciais/actions/workflows/build.yml/badge.svg)](https://github.com/AguaBatizada/sistema-academia-artes-marciais/actions/workflows/build.yml)

Aplicação desktop em **Java (Swing)** para gerenciar alunos, modalidades e matrículas de uma academia de artes marciais, com banco de dados **SQL Server (T-SQL)**.

Projeto acadêmico desenvolvido em grupo na **APS (Atividade Prática Supervisionada)** e no trabalho de **Programação de Banco de Dados** do curso de Ciência da Computação da **UNIP Campinas** (2026/2).

## Objetivo

Centralizar em um único sistema o cadastro de alunos, as modalidades oferecidas e o vínculo entre eles, guardando o histórico de alterações diretamente no banco de dados.

<!-- Adicione aqui capturas de tela: docs/img/tela-principal.png, docs/img/tela-alunos.png -->

## Funcionalidades

- Cadastro, edição, listagem e exclusão de **alunos** (nome, e-mail, telefone, faixa, CPF)
- Cadastro de **modalidades** (nome, professor, mensalidade, horário)
- **Matrículas** ligando alunos e modalidades (relação N:N com tabela associativa)

## Tecnologias e conceitos

- Java 17+, Swing, JDBC com `PreparedStatement`
- Tratamento de erros com exceção própria (`DAOException`): falhas do banco chegam à interface com mensagem clara
- Integração contínua com GitHub Actions (compilação a cada push)
- Arquitetura **MVC** + padrão **DAO**
- SQL Server: tabelas com PK/FK, **views**, **stored procedures**, **functions** e **triggers** (auditoria, backup e log de reajuste)

## Estrutura

```
src/
├── conexao/     ConexaoBanco            -> conexão JDBC com o SQL Server
├── model/       Aluno, Modalidade, Matricula
├── dao/         AlunoDAO, ModalidadeDAO, MatriculaDAO, DAOException   (SQL)
├── controller/  AlunoController, ModalidadeController, MatriculaController
└── view/        TelaPrincipal, TelaAluno, TelaModalidade, TelaMatricula (Swing)
script_banco_dados.sql   -> DDL, DML, views, procedures, functions e triggers
docs/SISTEMA_EXPLICACAO.md -> documentação técnica detalhada
```

Fluxo de uma ação: **View (botão) → Controller → DAO (SQL) → SQL Server**.

## Banco de dados (`academia_db`)

| Tabela | Tipo | Chaves |
|---|---|---|
| `Alunos` | Entidade | PK `id` |
| `Modalidades` | Entidade | PK `id` |
| `Matriculas` | Associativa (N:N) | PK `id`, FK `aluno_id`, FK `modalidade_id` |

Tabelas de apoio: `auditoria_aluno`, `modalidade_backup`, `modalidade_log`, `tabela_teste`.

## Como executar

1. **SQL Server:** ative a autenticação mista (SQL Server e Windows) e um login com senha.
2. **Banco:** abra `script_banco_dados.sql` no SSMS e execute (F5). Ele cria o `academia_db` com dados de exemplo fictícios.
3. **Driver JDBC:** baixe o [Microsoft JDBC Driver for SQL Server](https://learn.microsoft.com/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server) e coloque o `.jar` em `lib/mssql-jdbc.jar`.
4. **Credenciais:** defina as variáveis de ambiente `DB_USUARIO` (padrão: `sa`) e `DB_SENHA`.
5. **VS Code:** com o *Extension Pack for Java*, abra a pasta do projeto e execute `src/view/TelaPrincipal.java`.

## Aprendizados

- Separar a aplicação em camadas (view, controller, DAO, model) e entender o caminho de uma ação até o banco
- Usar `PreparedStatement` para evitar SQL Injection
- Modelar uma relação N:N com tabela associativa e chaves estrangeiras
- Levar regras para o banco com procedures, functions e triggers de auditoria
- Manter credenciais fora do código com variáveis de ambiente
- Propagar erros do banco até a interface, em vez de exibir sucesso quando a operação falha

## Melhorias futuras

- Validar campos (e-mail, CPF, faixa a partir de uma lista fixa)
- Gerenciar dependências e build com Maven
- Testes automatizados para controllers e DAOs
- Evoluir para uma API REST com Spring Boot, reaproveitando o modelo de dados

## Autoria

Projeto em grupo (5 integrantes) — Ciência da Computação, UNIP Campinas.
Publicado por [Diego Citolin Roland](https://www.linkedin.com/in/diego-citolin-roland-491742334/).
