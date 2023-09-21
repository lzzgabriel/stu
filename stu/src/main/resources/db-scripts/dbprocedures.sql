-- Cadastrar Aluno
CREATE DEFINER=`stustd`@`localhost` PROCEDURE `CADASTRAR_ALUNO`(
OUT retId INT,
IN a_id INT,
IN a_nome varchar(100),
IN a_email varchar(100),
IN a_celular varchar(11))
BEGIN
DECLARE novo_id INT;
IF a_id IS NULL THEN
	INSERT INTO stu.aluno(nome, email, celular)
	VALUES (a_nome, a_email, a_celular); -- inserir tabela aluno
	SET novo_id = LAST_INSERT_ID(); -- recuperar novo id
	SET retId = novo_id;
ELSE
	SET retId = 0;
	IF EXISTS(SELECT 1 FROM view_aluno va WHERE va.id = a_id) THEN
		UPDATE stu.aluno a SET a.nome = a_nome, a.email = a_email, a.celular = a_celular
		WHERE a.id = a_id; 
		SET retId = 1;
	END IF;
END IF;
END

-- Deletar Aluno
CREATE DEFINER=`stustd`@`localhost` PROCEDURE `delete_aluno`(OUT retId INT, IN a_id INT)
BEGIN
IF a_id IS NULL THEN
	SET retId = 0;
ELSE
	SET retId = 0;
	IF EXISTS (SELECT 1 FROM view_aluno va WHERE va.id = a_id) THEN
		DELETE FROM aluno a WHERE a.id = a_id;
		SET retId = 1;
	END IF;
END IF;
END

-- Cadastrar professor
CREATE DEFINER=`root`@`localhost` PROCEDURE `cadastrar_professor`(OUT retId INT,
         IN p_id INT,
         IN p_nome VARCHAR(100),
         IN p_email VARCHAR(100),
         IN p_senha VARCHAR(100))
BEGIN
IF p_id IS NULL THEN
	INSERT INTO stu.professor (nome, email, senha) 
    VALUES (p_nome, p_email, p_senha);
	SET retId = LAST_INSERT_ID();
ELSE
	SET retId = 0;
	IF EXISTS (SELECT 1 FROM view_professor vp WHERE vp.id = p_id) THEN
		UPDATE stu.professor p SET p.nome = p_nome, p.email = p_email WHERE p.id = p_id;
		SET retId = 1;
    END IF;
END IF;
END


-- Deletar professor
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_professor`(OUT result INT, IN p_id INT)
BEGIN
IF p_id IS NULL THEN
	SET result = 0;
ELSE
	SET result = 0;
	IF EXISTS (SELECT 1 FROM view_professor vp WHERE vp.id = p_id) THEN
		DELETE FROM professor p WHERE p.id = p_id;
		SET result = 1;
	END IF;
END IF;
END

-- Alterar senha professor
CREATE DEFINER=`root`@`localhost` PROCEDURE `alterar_senha_professor`(OUT retId INT, IN id_professor INT, 
	IN senhaAtual VARCHAR(100), IN senhaDestino VARCHAR(100))
BEGIN
SET retId = 0;
IF id_professor IS NOT NULL THEN 
	IF EXISTS (SELECT 1 FROM view_professor vp WHERE vp.id = id_professor AND vp.senha = senhaAtual) THEN
		UPDATE stu.professor p SET p.senha = senhaDestino WHERE p.id = id_professor;
		SET retId = 1;
	END IF;
END IF;
END

-- Cadastrar forma pagamento
CREATE DEFINER=`stustd`@`localhost` PROCEDURE `cadastrar_forma_pagamento`(OUT retId INT,
         IN f_id INT,
         IN f_descricao VARCHAR(100))
BEGIN
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
END

-- Deletar forma pagamento
CREATE DEFINER=`stustd`@`localhost` PROCEDURE `deletar_forma_pagamento`(OUT result INT, IN f_id INT)
BEGIN
IF f_id IS NULL THEN
	SET result = 0;
ELSE
	SET result = 0;
	IF EXISTS (SELECT 1 FROM view_formas_pagamento vf WHERE vf.id = f_id) THEN
		DELETE FROM forma_pagamento f WHERE f.id = f_id;
		SET result = 1;
	END IF;
END IF;
END