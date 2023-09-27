-- ALTERAR_SENHA_PROFESSOR
DROP PROCEDURE IF EXISTS `ALTERAR_SENHA_PROFESSOR`;
DELIMITER $$
CREATE PROCEDURE `ALTERAR_SENHA_PROFESSOR`(OUT retId INT, IN id_professor INT, 
	IN senhaAtual VARCHAR(100), IN senhaDestino VARCHAR(100))
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
SET retId = 0;
IF id_professor IS NOT NULL THEN 
	IF EXISTS (SELECT 1 FROM view_professor vp WHERE vp.id = id_professor AND vp.senha = senhaAtual) THEN
		UPDATE stu.professor p SET p.senha = senhaDestino WHERE p.id = id_professor;
		SET retId = 1;
	END IF;
END IF;
END$$
DELIMITER ;

-- ASSOCIAR_ALUNO_PROFESSOR
DROP PROCEDURE IF EXISTS `ASSOCIAR_ALUNO_PROFESSOR`;
DELIMITER $$
CREATE PROCEDURE `ASSOCIAR_ALUNO_PROFESSOR`(OUT retId INT, IN id_p INT, IN id_a INT)
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
SET retId = 0;
IF id_p IS NOT NULL AND id_a IS NOT NULL THEN
	IF EXISTS (SELECT 1 FROM view_professor vp WHERE vp.id = id_p) AND EXISTS
	(SELECT 1 FROM aluno a WHERE a.id = id_a) THEN
		INSERT INTO aluno_de_professor(id_aluno, id_professor) VALUES (id_a, id_p);
        SET retId = 1;
    END IF;
END IF;
end$$
DELIMITER ;

-- CADASTRAR_ALUNO
DROP PROCEDURE IF EXISTS `CADASTRAR_ALUNO`;
DELIMITER $$
CREATE PROCEDURE `CADASTRAR_ALUNO`(
OUT retId INT,
IN a_nome varchar(100),
IN a_email varchar(100),
IN a_celular varchar(11),
IN p_id INT)
BEGIN
DECLARE novo_id INT;
DECLARE resultAssociacao INT;
DECLARE resultGerarMensalidade INT;

BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;

SET resultAssociacao = 0, retId = 0, resultGerarMensalidade = 0;
INSERT INTO stu.aluno(nome, email, celular)
VALUES (a_nome, a_email, a_celular); -- inserir tabela aluno
SET novo_id = LAST_INSERT_ID(); -- recuperar novo id
CALL ASSOCIAR_ALUNO_PROFESSOR(resultAssociacao, p_id, novo_id);
CALL GERAR_MENSALIDADE_ABERTA(resultGerarMensalidade, novo_id, 400.0, '2023-09-20');
IF resultAssociacao = 1 AND resultGerarMensalidade = 1 THEN
	SET retId = novo_id;
END IF;
END$$
DELIMITER ;

-- CADASTRAR_FORMA_PAGAMENTO
DROP PROCEDURE IF EXISTS `CADASTRAR_FORMA_PAGAMENTO`;
DELIMITER $$
CREATE PROCEDURE `CADASTRAR_FORMA_PAGAMENTO`(OUT retId INT,
         IN f_id INT,
         IN f_descricao VARCHAR(100))
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
IF f_id IS NULL THEN
	INSERT INTO stu.forma_pagamento (descricao) VALUES (f_descricao);
	SET retId = LAST_INSERT_ID();
ELSE
	SET retId = 0;
	IF EXISTS (SELECT 1 FROM view_formas_pagamento fp WHERE fp.id = f_id) THEN
		UPDATE stu.forma_pagamento fp SET fp.descricao = f_descricao WHERE fp.id = f_id;
		SET retId = f_id;
    END IF;
END IF;
END$$
DELIMITER ;

-- CADASTRAR_PROFESSOR
DROP PROCEDURE IF EXISTS `CADASTRAR_PROFESSOR`;
DELIMITER $$
CREATE PROCEDURE `CADASTRAR_PROFESSOR`(OUT retId INT,
         IN p_nome VARCHAR(100),
         IN p_email VARCHAR(100),
         IN p_senha VARCHAR(100))
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK;
END;
	INSERT INTO stu.professor (nome, email, senha) 
    VALUES (p_nome, p_email, p_senha);
	SET retId = LAST_INSERT_ID();
END$$
DELIMITER ;

-- CONFIRMAR_PAGAMENTO
DROP PROCEDURE IF EXISTS `CONFIRMAR_PAGAMENTO`;
DELIMITER $$
CREATE PROCEDURE `CONFIRMAR_PAGAMENTO`(in p_id_aluno int, in p_momento_pagamento timestamp, in p_id_forma_pagamento int)
begin
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
	select 
	ma.id_aluno,
	ma.valor_cobrar,
	ma.proximo_vencimento,
	ma.mensalidade 
	
	into
	@id,
	@valor,
	@vencimento,
	@mensalidade
	
	from stu.mensalidade_aberta ma
	where ma.id_aluno = p_id_aluno;

	insert into mensalidade_cobrada
	(id_aluno,
	mensalidade,
	valor_cobrado,
	data_vencimento,
	id_forma_pagamento,
	momento_pagamento)
	
	values (@id, @mensalidade, @valor, @vencimento,
			p_id_forma_pagamento, p_momento_pagamento);
		
	update mensalidade_aberta
	set
	proximo_vencimento = adddate(@vencimento, interval 1 month)
	where id_aluno = p_id_aluno;
END$$
DELIMITER ;

-- DELETE_FORMA_PAGAMENTO
DROP PROCEDURE IF EXISTS `DELETAR_FORMA_PAGAMENTO`;
DELIMITER $$
CREATE PROCEDURE `DELETAR_FORMA_PAGAMENTO`(OUT result INT, IN f_id INT)
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
IF f_id IS NULL THEN
	SET result = 0;
ELSE
	SET result = 0;
	IF EXISTS (SELECT 1 FROM view_formas_pagamento vf WHERE vf.id = f_id) THEN
		DELETE FROM forma_pagamento f WHERE f.id = f_id;
		SET result = 1;
	END IF;
END IF;
END$$
DELIMITER ;

-- DELETE_ALUNO
DROP PROCEDURE IF EXISTS `DELETE_ALUNO`;
DELIMITER $$
CREATE PROCEDURE `DELETE_ALUNO`(OUT retId INT, IN a_id INT)
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
IF a_id IS NULL THEN
	SET retId = 0;
ELSE
	SET retId = 0;
	IF EXISTS (SELECT 1 FROM view_aluno va WHERE va.id = a_id) THEN
		DELETE FROM aluno a WHERE a.id = a_id;
		SET retId = 1;
	END IF;
END IF;
END$$
DELIMITER ;

-- DELETE_PROFESSOR
DROP PROCEDURE IF EXISTS `DELETE_PROFESSOR`;
DELIMITER $$
CREATE PROCEDURE `DELETE_PROFESSOR`(OUT result INT, IN p_id INT)
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;

IF p_id IS NULL THEN
	SET result = 0;
ELSE
	SET result = 0;
	 IF EXISTS (SELECT 1 FROM view_professor vp WHERE vp.id = p_id) THEN
		DELETE FROM professor p WHERE p.id = p_id;
		 SET result = 1;
	 END IF;
END IF;
END$$
DELIMITER ;

-- EDITAR_ALUNO
DROP PROCEDURE IF EXISTS `EDITAR_ALUNO`;
DELIMITER $$
CREATE PROCEDURE `EDITAR_ALUNO`(
OUT retId INT,
IN a_id INT,
IN a_nome varchar(100),
IN a_email varchar(100),
IN a_celular varchar(11))
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
SET retId = 0;
IF EXISTS(SELECT 1 FROM stu.aluno a WHERE a.id = a_id) THEN
	UPDATE stu.aluno a SET a.nome = a_nome, a.email = a_email, a.celular = a_celular
	WHERE a.id = a_id; 
	SET retId = 1;
END IF;
END$$
DELIMITER ;

-- EDITAR_MENSALIDADE_ABERTA
DELIMITER $$
CREATE PROCEDURE `EDITAR_MENSALIDADE_ABERTA`(OUT retId INT, IN a_id INT, IN valor_atualizado DECIMAL,
IN nova_data DATE)
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
SET retId = 0;
IF a_id IS NOT NULL AND EXISTS (SELECT 1 FROM stu.mensalidade_aberta ma WHERE ma.id_aluno = a_id) THEN
	UPDATE stu.mensalidade_aberta ma SET ma.valor_cobrar = valor_atualizado, 
	ma.proximo_vencimento = nova_data WHERE ma.id_aluno = a_id; 
	SET retId = 1;
END IF;
END$$
DELIMITER ;

-- EDITAR_PROFESSOR
DROP PROCEDURE IF EXISTS `EDITAR_PROFESSOR`;
DELIMITER $$
CREATE PROCEDURE `EDITAR_PROFESSOR`(OUT retId INT,
         IN p_id INT,
         IN p_nome VARCHAR(100),
         IN p_email VARCHAR(100),
         IN p_senha VARCHAR(100))
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
SET retId = 0;
IF EXISTS (SELECT 1 FROM professor p WHERE p.id = p_id AND p.ativo = 1) THEN
	UPDATE stu.professor p SET p.nome = p_nome, p.email = p_email WHERE p.id = p_id;
	SET retId = 1;
END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `FILTRAR_ALUNOS`(in id_aluno INT, in nome_aluno VARCHAR(100),
 in ativo TINYINT, in momento_cadastro TIMESTAMP)
BEGIN
SELECT * FROM stu.aluno a WHERE (id_aluno IS NULL OR a.id =  id_aluno) 
AND (nome_aluno IS NULL OR a.nome =  nome_aluno) AND (ativo IS NULL OR a.ativo =  ativo) 
AND (momento_cadastro IS NULL OR a.momento_cadastro =  momento_cadastro);
END$$
DELIMITER ;

-- GERAR_MENSALIDADE_ABERTA
DROP PROCEDURE IF EXISTS `GERAR_MENSALIDADE_ABERTA`;
DELIMITER $$
CREATE PROCEDURE `GERAR_MENSALIDADE_ABERTA`(OUT retId INT, IN id_aluno INT, 
IN valor_cobrar DECIMAL, IN mensalidade DATE)
BEGIN
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    ROLLBACK; 
END;
SET retId = 0;
IF id_aluno IS NOT NULL AND EXISTS (SELECT 1 FROM stu.aluno a WHERE a.id = id_aluno) THEN
	INSERT INTO mensalidade_aberta(id_aluno, proximo_vencimento, valor_cobrar) 
    VALUES (id_aluno, mensalidade, valor_cobrar);
    SET retId = 1;
END IF;
END$$
DELIMITER ;