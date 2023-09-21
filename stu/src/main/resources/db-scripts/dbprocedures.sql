-- Cadastrar Aluno
CREATE DEFINER=`stustd`@`localhost` PROCEDURE `cadastrar_aluno`(
OUT retId INT,
IN a_id INT,
IN a_nome varchar(100),
IN a_email varchar(100),
IN a_celular varchar(11),
IN a_id_professor int
/*IN a_valor_cobrar decimal(10,2),
IN a_proximo_vencimento date,
IN a_mensalidade date*/)
BEGIN
DECLARE novo_id INT;
IF a_id IS NULL THEN
	IF EXISTS(SELECT 1 FROM view_professor vp WHERE vp.id = a_id_professor) THEN
		INSERT INTO stu.aluno(nome, email, celular, id_professor)
		VALUES (a_nome, a_email, a_celular, a_id_professor); -- inserir tabela aluno
        SET novo_id = LAST_INSERT_ID(); -- recuperar novo id
        SET retId = novo_id;
	ELSE
		INSERT INTO stu.aluno(nome, email, celular, id_professor) -- Para professor que não existir será passado nulo
		VALUES (a_nome, a_email, a_celular, NULL);
        SET novo_id = LAST_INSERT_ID(); -- recuperar novo id
        SET retId = novo_id;
	END IF;
ELSE
	SET retId = 0;
	IF EXISTS(SELECT 1 FROM view_aluno va WHERE va.id = a_id) THEN
		IF EXISTS(SELECT 1 FROM view_professor vp WHERE vp.id = a_id_professor) THEN
			UPDATE stu.aluno a SET a.nome = a_nome, a.email = a_email, a.celular = a_celular, a.id_professor = a_id_professor 
            WHERE a.id = a_id;
            SET retId = 1;
		ELSE
			UPDATE stu.aluno a SET a.nome = a_nome, a.email = a_email, a.celular = a_celular, a.id_professor = NULL 
            WHERE a.id = a_id; -- Para professor que não existir será passado nulo
			SET retId = 1;
        END IF;
	END IF;
END IF;    
	/*INSERT INTO stu.aluno_de_professor (id_aluno , id_professor) -- Retirado por hora, provavelmente será separado.
	VALUES (novo_id, p_id_professor); -- associar aluno com professor
    
	INSERT INTO mensalidade_aberta(id_aluno, valor_cobrar, proximo_vencimento, mensalidade)
		VALUES (novo_id, p_valor_cobrar, p_proximo_vencimento, p_mensalidade); -- inserir tabela mensalidade aberta*/
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