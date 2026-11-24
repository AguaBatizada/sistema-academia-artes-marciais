-- ============================================================================
-- TRABALHO DE PROGRAMAÇÃO DE BANCO DE DADOS & APS (SQL Server / T-SQL)
-- Sistema de Gestão para Academia de Artes Marciais (Alunos, Modalidades, Matriulas)
-- ============================================================================

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'academia_db')
BEGIN
    CREATE DATABASE academia_db;
END
GO

USE academia_db;
GO

-- ============================================================================
-- COMANDOS DDL (Data Definition Language)
-- ============================================================================

IF OBJECT_ID('Matriculas', 'U') IS NOT NULL DROP TABLE Matriculas;
IF OBJECT_ID('Modalidades', 'U') IS NOT NULL DROP TABLE Modalidades;
IF OBJECT_ID('Alunos', 'U') IS NOT NULL DROP TABLE Alunos;
IF OBJECT_ID('tabela_teste', 'U') IS NOT NULL DROP TABLE tabela_teste;
IF OBJECT_ID('modalidade_backup', 'U') IS NOT NULL DROP TABLE modalidade_backup;
IF OBJECT_ID('auditoria_aluno', 'U') IS NOT NULL DROP TABLE auditoria_aluno;
IF OBJECT_ID('modalidade_log', 'U') IS NOT NULL DROP TABLE modalidade_log;
GO

-- Tabela Alunos
CREATE TABLE Alunos (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(20),
    faixa VARCHAR(30) DEFAULT 'Branca',
    cpf VARCHAR(14)
);
GO

-- Tabela Modalidades (Kung Fu, Jiu-Jitsu, Muay Thai, Karate, Judo, Boxe, etc.)
CREATE TABLE Modalidades (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    professor VARCHAR(100) NOT NULL,
    mensalidade DECIMAL(10,2) NOT NULL,
    horario VARCHAR(50) NOT NULL
);
GO

-- Tabela Matriculas (Tabela Associativa / Relação N para N entre Alunos e Modalidades)
CREATE TABLE Matriculas (
    id INT IDENTITY(1,1) PRIMARY KEY,
    aluno_id INT NOT NULL,
    modalidade_id INT NOT NULL,
    data_matricula DATETIME DEFAULT GETDATE(),
    status VARCHAR(20) DEFAULT 'Ativo',
    CONSTRAINT fk_matricula_aluno FOREIGN KEY (aluno_id) REFERENCES Alunos(id),
    CONSTRAINT fk_matricula_modalidade FOREIGN KEY (modalidade_id) REFERENCES Modalidades(id)
);
GO

-- Tabela de Teste para DROP TABLE
CREATE TABLE tabela_teste (
    id INT PRIMARY KEY,
    descricao VARCHAR(50)
);
GO

-- Tabela de Backup para Trigger de Modalidades
CREATE TABLE modalidade_backup (
    id INT PRIMARY KEY,
    nome VARCHAR(100),
    professor VARCHAR(100),
    mensalidade DECIMAL(10,2),
    horario VARCHAR(50),
    data_backup DATETIME DEFAULT GETDATE()
);
GO

-- Tabela de Auditoria para Trigger de Alunos
CREATE TABLE auditoria_aluno (
    id INT IDENTITY(1,1) PRIMARY KEY,
    aluno_id INT,
    nome_antigo VARCHAR(100),
    nome_novo VARCHAR(100),
    faixa_antiga VARCHAR(30),
    faixa_nova VARCHAR(30),
    data_alteracao DATETIME DEFAULT GETDATE(),
    usuario VARCHAR(50) DEFAULT SYSTEM_USER
);
GO

-- Tabela de Log para Trigger de Modalidades
CREATE TABLE modalidade_log (
    id INT IDENTITY(1,1) PRIMARY KEY,
    modalidade_id INT,
    acao VARCHAR(50),
    preco_antigo DECIMAL(10,2),
    preco_novo DECIMAL(10,2),
    data_log DATETIME DEFAULT GETDATE()
);
GO

-- Alteração da estrutura de uma tabela (ALTER TABLE)
ALTER TABLE Alunos ADD data_cadastro DATETIME DEFAULT GETDATE();
GO

-- Exclusão de tabela de teste (DROP TABLE)
DROP TABLE IF EXISTS tabela_teste;
GO

-- ============================================================================
-- COMANDOS DML (Data Manipulation Language)
-- ============================================================================

-- Inserção de Alunos
INSERT INTO Alunos (nome, email, telefone, faixa, cpf) VALUES
('Lucas Silva', 'lucas@email.com', '(19) 99111-1111', 'Azul', '111.111.111-11'),
('Beatriz Souza', 'beatriz@email.com', '(19) 98222-2222', 'Roxa', '222.222.222-22'),
('Rafael Costa', 'rafael@email.com', '(19) 97333-3333', 'Branca', '333.333.333-33'),
('Camila Lima', 'camila@email.com', '(19) 96444-4444', 'Marrom', '444.444.444-44');
GO

-- Inserção de Modalidades (Artes Marciais)
INSERT INTO Modalidades (nome, professor, mensalidade, horario) VALUES
('Jiu-Jitsu', 'Mestre Carlos', 150.00, 'Seg/Qua/Sex 19:00'),
('Muay Thai', 'Prof. Ricardo', 140.00, 'Ter/Qui 20:00'),
('Karate', 'Sensei Tanaka', 130.00, 'Seg/Qua 18:00'),
('Judô', 'Sensei Yamamoto', 130.00, 'Ter/Qui 19:00'),
('Boxe', 'Prof. Tyson', 160.00, 'Sex 20:00'),
('Kung Fu', 'Sifu Chen', 150.00, 'Sab 10:00');
GO

-- Inserção de Matriculas (Relação Aluno <-> Modalidade)
INSERT INTO Matriculas (aluno_id, modalidade_id, status) VALUES
(1, 1, 'Ativo'),
(1, 2, 'Ativo'),
(2, 1, 'Ativo'),
(3, 3, 'Ativo'),
(4, 4, 'Ativo');
GO

-- Atualização (UPDATE)
UPDATE Modalidades
SET mensalidade = 155.00
WHERE id = 1;
GO

-- Exclusão (DELETE) de teste
INSERT INTO Matriculas (aluno_id, modalidade_id, status) VALUES (3, 5, 'Cancelado');
DELETE FROM Matriculas WHERE status = 'Cancelado';
GO

-- ============================================================================
-- COMANDOS DQL (Data Query Language) - Consultas com JOINs
-- ============================================================================

-- A) Consulta com INNER JOIN e condição específica (Matrículas ativas com valor acima de 130)
SELECT
    m.id AS idMatricula,
    a.nome AS nomeAluno,
    mod.nome AS nomeModalidade,
    mod.mensalidade,
    m.status
FROM Matriculas m
INNER JOIN Alunos a ON m.aluno_id = a.id
INNER JOIN Modalidades mod ON m.modalidade_id = mod.id
WHERE mod.mensalidade >= 140.00;
GO

-- B) Consulta com LEFT JOIN (Todos os alunos e suas matrículas)
SELECT
    a.id AS idAluno,
    a.nome AS nomeAluno,
    a.faixa,
    m.id AS idMatricula,
    m.status
FROM Alunos a
LEFT JOIN Matriculas m ON a.id = m.aluno_id;
GO

-- C) Consulta com RIGHT JOIN (Todas as modalidades e suas matrículas)
SELECT
    mod.id AS idModalidade,
    mod.nome AS nomeModalidade,
    mod.professor,
    m.id AS idMatricula
FROM Matriculas m
RIGHT JOIN Modalidades mod ON m.modalidade_id = mod.id;
GO

-- ============================================================================
-- VIEWS (Visões)
-- ============================================================================

-- A) View com 1 tabela (Resumo de Alunos)
CREATE VIEW vw_AlunosResumo AS
SELECT id, nome, faixa, email, telefone
FROM Alunos;
GO

-- B) View utilizando pelo menos 2 tabelas (Matrículas detalhadas)
CREATE VIEW vw_MatriculasDetalhadas AS
SELECT
    m.id AS idMatricula,
    a.nome AS nomeAluno,
    a.faixa AS faixaAluno,
    mod.nome AS nomeModalidade,
    mod.professor,
    mod.mensalidade,
    m.status,
    m.data_matricula
FROM Matriculas m
INNER JOIN Alunos a ON m.aluno_id = a.id
INNER JOIN Modalidades mod ON m.modalidade_id = mod.id;
GO

-- ============================================================================
-- STORED PROCEDURES (Procedimentos Armazenados)
-- ============================================================================

-- A) Procedure com estrutura condicional (IF/ELSE) para classificar o aluno por faixa
CREATE PROCEDURE classificar_faixa_aluno
    @idAluno INT
AS
BEGIN
    DECLARE @nomeAluno VARCHAR(100);
    DECLARE @faixaAluno VARCHAR(30);
    DECLARE @nivelGraduacao VARCHAR(50);

    SELECT @nomeAluno = nome, @faixaAluno = faixa
    FROM Alunos
    WHERE id = @idAluno;

    IF @nomeAluno IS NULL
    BEGIN
        PRINT 'Aluno não encontrado.';
        RETURN;
    END

    IF @faixaAluno IN ('Marrom', 'Preta')
        SET @nivelGraduacao = 'Graduação Avançada';
    ELSE IF @faixaAluno IN ('Azul', 'Roxa', 'Verde')
        SET @nivelGraduacao = 'Graduação Intermediária';
    ELSE
        SET @nivelGraduacao = 'Graduação Iniciante';

    SELECT
        @nomeAluno AS 'Aluno',
        @faixaAluno AS 'Faixa Atual',
        @nivelGraduacao AS 'Nível na Academia';
END;
GO

-- B) Procedure com cálculo (Reajuste de mensalidade por modalidade)
CREATE PROCEDURE reajustar_mensalidade_modalidade
    @idModalidade INT
AS
BEGIN
    DECLARE @nomeModalidade VARCHAR(100);
    DECLARE @mensalidadeAtual DECIMAL(10,2);
    DECLARE @percentual INT;
    DECLARE @novaMensalidade DECIMAL(10,2);

    SELECT @nomeModalidade = nome, @mensalidadeAtual = mensalidade
    FROM Modalidades
    WHERE id = @idModalidade;

    IF @nomeModalidade IS NULL
    BEGIN
        PRINT 'Modalidade não encontrada.';
        RETURN;
    END

    SET @percentual = 10;
    SET @novaMensalidade = @mensalidadeAtual + (@mensalidadeAtual * @percentual / 100.00);

    UPDATE Modalidades
    SET mensalidade = @novaMensalidade
    WHERE id = @idModalidade;

    SELECT
        @nomeModalidade AS 'Modalidade',
        @mensalidadeAtual AS 'Mensalidade Anterior',
        @percentual AS 'Reajuste (%)',
        @novaMensalidade AS 'Nova Mensalidade';
END;
GO

-- ============================================================================
-- FUNCTIONS (Funções)
-- ============================================================================

-- A) Function Escalar para cálculo de desconto na mensalidade
CREATE FUNCTION fn_calcular_desconto_mensalidade
(
    @valorMensalidade DECIMAL(10,2),
    @descontoPercentual DECIMAL(5,2)
)
RETURNS DECIMAL(10,2)
AS
BEGIN
    DECLARE @valorFinal DECIMAL(10,2);
    SET @valorFinal = @valorMensalidade - (@valorMensalidade * (@descontoPercentual / 100.00));
    RETURN @valorFinal;
END;
GO

-- B) Function Escalar para calcular o total investido pelo aluno em mensalidades
CREATE FUNCTION fn_total_investido_aluno
(
    @idAluno INT
)
RETURNS DECIMAL(10,2)
AS
BEGIN
    DECLARE @total DECIMAL(10,2);
    SELECT @total = ISNULL(SUM(mod.mensalidade), 0.00)
    FROM Matriculas m
    INNER JOIN Modalidades mod ON m.modalidade_id = mod.id
    WHERE m.aluno_id = @idAluno AND m.status = 'Ativo';
    RETURN @total;
END;
GO

-- ============================================================================
-- TRIGGERS (Gatilhos) - 3 Triggers Solicitadas no Trabalho
-- ============================================================================

-- 1) Trigger de Backup (AFTER INSERT em Modalidades)
CREATE TRIGGER trg_backup_modalidade
ON Modalidades
AFTER INSERT
AS
BEGIN
    INSERT INTO modalidade_backup (id, nome, professor, mensalidade, horario)
    SELECT id, nome, professor, mensalidade, horario FROM inserted;
END;
GO

-- 2) Trigger de Alteração de Dados (AFTER UPDATE em Alunos)
CREATE TRIGGER trg_auditoria_aluno
ON Alunos
AFTER UPDATE
AS
BEGIN
    IF UPDATE(nome) OR UPDATE(faixa)
    BEGIN
        INSERT INTO auditoria_aluno (aluno_id, nome_antigo, nome_novo, faixa_antiga, faixa_nova)
        SELECT
            d.id,
            d.nome,
            i.nome,
            d.faixa,
            i.faixa
        FROM inserted i
        INNER JOIN deleted d ON i.id = d.id
        WHERE d.nome <> i.nome OR d.faixa <> i.faixa;
    END
END;
GO

-- 3) Trigger de Log de Atualizações (AFTER UPDATE em Modalidades)
CREATE TRIGGER trg_log_modalidade_update
ON Modalidades
AFTER UPDATE
AS
BEGIN
    IF UPDATE(mensalidade)
    BEGIN
        INSERT INTO modalidade_log (modalidade_id, acao, preco_antigo, preco_novo)
        SELECT
            d.id,
            'ATUALIZACAO_MENSALIDADE',
            d.mensalidade,
            i.mensalidade
        FROM inserted i
        INNER JOIN deleted d ON i.id = d.id
        WHERE d.mensalidade <> i.mensalidade;
    END
END;
GO

-- ============================================================================
-- FIM DO SCRIPT T-SQL — ACADEMIA DE ARTES MARCIAIS
-- ============================================================================
