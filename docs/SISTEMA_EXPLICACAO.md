# 🥋 Sistema de Gestão para Academia de Artes Marciais
## Documentação Técnica e Guia Explicativo do Projeto (APS & Trabalho de Banco de Dados)

---

## 📌 Sumário
1. [Visão Geral do Tema](#1-visão-geral-do-tema)
2. [Arquitetura do Software (MVC e DAO)](#2-arquitetura-do-software-mvc-e-dao)
3. [Modelagem e Banco de Dados (`academia_db`)](#3-modelagem-e-banco-de-dados-academia_db)
4. [Rotinas Avançadas em T-SQL (SQL Server)](#4-rotinas-avançadas-em-t-sql-sql-server)
5. [Interface Gráfica e Componentes Java (Swing)](#5-interface-gráfica-e-componentes-java-swing)
6. [Resumo dos Requisitos Atendidos](#6-resumo-dos-requisitos-atendidos)

---

## 1. Visão Geral do Tema
O sistema foi desenvolvido para atender às necessidades de gerenciamento de uma **Academia de Artes Marciais**. O software permite o controle centralizado de:
- **Alunos:** Praticantes cadastrados na academia (Jiu-Jitsu, Muay Thai, Karate, Judô, Boxe, Kung Fu, etc.).
- **Modalidades:** As artes marciais ofertadas, seus respectivos professores, horários e mensalidades.
- **Matrículas:** A tabela associativa que realiza o relacionamento *N para N* entre os alunos e as modalidades que frequentam.

---

## 2. Arquitetura do Software (MVC e DAO)
Para garantir organização, manutenibilidade e separação de responsabilidades (conforme exigido pela APS), o código Java foi estruturado em camadas estritas:

- **`conexao`**: Contém a classe `ConexaoBanco.java`, responsável por estabelecer a conexão JDBC com o SQL Server.
- **`model`**: Classes de domínio (`Aluno.java`, `Modalidade.java`, `Matricula.java`) que representam as entidades do negócio com seus atributos e encapsulamento (`getters`/`setters`).
- **`dao`** *(Data Access Object)*: Classes (`AlunoDAO.java`, `ModalidadeDAO.java`, `MatriculaDAO.java`) que encapsulam todas as instruções SQL (`INSERT`, `UPDATE`, `DELETE`, `SELECT`) utilizando `PreparedStatement` para segurança contra *SQL Injection*.
- **`controller`**: Classes intermediárias (`AlunoController.java`, `ModalidadeController.java`, `MatriculaController.java`) que recebem as requisições das telas e acionam o DAO.
- **`view`**: Telas gráficas construídas em **Java Swing** (`TelaPrincipal.java`, `TelaAluno.java`, `TelaModalidade.java`, `TelaMatricula.java`), proporcionando interação visual amigável.

---

## 3. Modelagem e Banco de Dados (`academia_db`)

O banco relacional foi modelado em **T-SQL (SQL Server)** contendo as seguintes estruturas principais:

### 📊 As Tabelas
1. **`Alunos` (Entidade)**:
   - `id` (PK, `IDENTITY`)
   - `nome`, `email`, `telefone`, `faixa`, `cpf`, `data_cadastro`
2. **`Modalidades` (Entidade)**:
   - `id` (PK, `IDENTITY`)
   - `nome`, `professor`, `mensalidade`, `horario`
3. **`Matriculas` (Tabela Associativa - Relação N:N)**:
   - `id` (PK, `IDENTITY`)
   - `aluno_id` (FK referência a `Alunos`)
   - `modalidade_id` (FK referência a `Modalidades`)
   - `status`, `data_matricula`
4. **Tabelas de Apoio (Backup, Log e Testes)**:
   - `tabela_teste`: Utilizada para demonstrar comandos `DROP TABLE`.
   - `modalidade_backup`: Armazena cópias geradas pela trigger de inserção.
   - `auditoria_aluno`: Registra o histórico de alterações cadastrais.
   - `modalidade_log`: Armazena o histórico de reajustes de preços.

---

## 4. Rotinas Avançadas em T-SQL (SQL Server)

O projeto implementa todos os recursos avançados de banco de dados solicitados no trabalho:

### 🔍 Consultas Avançadas (DQL com JOINs)
- **`INNER JOIN`**: Lista as matrículas ativas cruzando dados de alunos e modalidades com filtro de valor (`WHERE`).
- **`LEFT JOIN`**: Lista **todos os alunos**, mesmo aqueles que não possuem matrículas ativas.
- **`RIGHT JOIN`**: Lista **todas as modalidades**, mesmo as que não possuem alunos inscritos.

### 👁️ Visões (Views)
- **`vw_AlunosResumo`**: Visão baseada em 1 tabela (`Alunos`) para consulta rápida.
- **`vw_MatriculasDetalhadas`**: Visão unindo múltiplas tabelas (`Matriculas`, `Alunos`, `Modalidades`) para relatórios gerenciais.

### ⚙️ Procedimentos Armazenados (Stored Procedures)
1. **`classificar_faixa_aluno`**: Recebe o ID de um aluno e utiliza estrutura condicional (`IF / ELSE`) para classificá-lo em *Graduação Iniciante*, *Intermediária* ou *Avançada*.
2. **`reajustar_mensalidade_modalidade`**: Recebe o ID de uma modalidade, aplica um cálculo de reajuste percentual de 10% e atualiza o valor.

### 📐 Funções (User-Defined Functions)
1. **`fn_calcular_desconto_mensalidade`**: Função escalar que calcula o valor líquido de uma mensalidade aplicando um percentual de desconto.
2. **`fn_total_investido_aluno`**: Função escalar que calcula a soma total investida por um aluno em todas as suas matrículas ativas.

### ⚡ Gatilhos (Triggers)
1. **`trg_backup_modalidade` (`AFTER INSERT`)**: Insere automaticamente um registro na tabela `modalidade_backup` ao cadastrar nova modalidade.
2. **`trg_auditoria_aluno` (`AFTER UPDATE`)**: Registra alterações de nome ou faixa na tabela `auditoria_aluno`.
3. **`trg_log_modalidade_update` (`AFTER UPDATE`)**: Monitora reajustes de mensalidade gravando valores antigos e novos na tabela `modalidade_log`.

---

## 5. Interface Gráfica e Componentes Java (Swing)
As telas foram projetadas com foco em usabilidade e experiência do usuário:
- **`TelaPrincipal`**: Painel central de navegação com acesso rápido aos módulos de Alunos, Modalidades e Matrículas.
- **`TelaAluno`**: Cadastro completo com tabela interativa, filtros de busca por nome e ordenação por múltiplos critérios (por nome ou faixa).
- **`TelaModalidade`**: Gestão das artes marciais oferecidas, permitindo controle de horários, professores e valores, com ordenação por preço.
- **`TelaMatricula`**: Associa dinamicamente alunos e modalidades através de menus suspensos (`JComboBox`), impedindo inconsistências e exibindo a listagem em tempo real.

---

## 6. Resumo dos Requisitos Atendidos
| Requisito do Trabalho / APS | Status | Implementação no Projeto |
| :--- | :---: | :--- |
| **Arquitetura MVC / DAO** | ✅ | Pacotes separados (`model`, `dao`, `controller`, `view`) |
| **Banco de Dados Relacional** | ✅ | SQL Server (`academia_db`) |
| **Mínimo 3 Tabelas (com Associativa)** | ✅ | `Alunos`, `Modalidades`, `Matriculas` |
| **Comandos DDL / DML / DQL** | ✅ | `CREATE`, `ALTER`, `DROP`, `INSERT`, `UPDATE`, `DELETE`, `JOINs` |
| **Views (1 e Múltiplas Tabelas)** | ✅ | `vw_AlunosResumo`, `vw_MatriculasDetalhadas` |
| **Stored Procedures (Condição e Cálculo)** | ✅ | `classificar_faixa_aluno`, `reajustar_mensalidade_modalidade` |
| **Functions (Escalares)** | ✅ | `fn_calcular_desconto_mensalidade`, `fn_total_investido_aluno` |
| **Triggers (Backup, Auditoria e Log)** | ✅ | `trg_backup_modalidade`, `trg_auditoria_aluno`, `trg_log_modalidade_update` |
| **Interface Gráfica (Java Swing)** | ✅ | Telas interativas com CRUD completo, busca e ordenação |
