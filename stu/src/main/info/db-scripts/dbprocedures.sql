-- Cadastrar Aluno
CREATE DEFINER=`root`@`localhost` PROCEDURE `CADASTRAR_ALUNO`(
OUT retId INT,
IN a_nome varchar(100),
IN a_email varchar(100),
IN a_celular varchar(11),
IN p_id INT)
BEGIN
DECLARE novo_id INT;
DECLARE resultAssoc INT;
SET resultAssoc = 0, retId = 0;
INSERT INTO stu.aluno(nome, email, celular)
VALUES (a_nome, a_email, a_celular); -- inserir tabela aluno
SET novo_id = LAST_INSERT_ID(); -- recuperar novo id
CALL ASSOCIAR_ALUNO_PROFESSOR(resultAssoc, p_id, novo_id);
IF resultAssoc = 1 THEN
	SET retId = novo_id;
END IF;
END

-- Editar aluno
CREATE DEFINER=`root`@`localhost` PROCEDURE `EDITAR_ALUNO`(
OUT retId INT,
IN a_id INT,
IN a_nome varchar(100),
IN a_email varchar(100),
IN a_celular varchar(11))
BEGIN
SET retId = 0;
IF EXISTS(SELECT 1 FROM stu.aluno a WHERE a.id = a_id) THEN
	UPDATE stu.aluno a SET a.nome = a_nome, a.email = a_email, a.celular = a_celular
	WHERE a.id = a_id; 
	SET retId = 1;
END IF;
END

-- Cadastrar professor
CREATE DEFINER=`root`@`localhost` PROCEDURE `CADASTRAR_PROFESSOR`(OUT retId INT,
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
	IF EXISTS (SELECT 1 FROM view_professor vp WHERE vp.id = p_id AND vp.ativo = 1) THEN
		UPDATE stu.professor p SET p.nome = p_nome, p.email = p_email WHERE p.id = p_id;
		SET retId = 1;
    END IF;
END IF;
END

-- Alterar senha professor
CREATE DEFINER=`root`@`localhost` PROCEDURE `ALTERAR_SENHA_PROFESSOR`(OUT retId INT, IN id_professor INT, 
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

-- Associar aluno a professor
CREATE DEFINER=`root`@`localhost` PROCEDURE `stu`.`ASSOCIAR_ALUNO_PROFESSOR`(OUT retId INT, IN id_p INT, IN id_a INT)
BEGIN
SET retId = 0;
IF id_p IS NOT NULL AND id_a IS NOT NULL THEN
	IF EXISTS (SELECT 1 FROM view_professor vp WHERE vp.id = id_p) AND EXISTS
	(SELECT 1 FROM aluno a WHERE a.id = id_a) THEN
		INSERT INTO aluno_de_professor(id_aluno, id_professor) VALUES (id_a, id_p);
        SET retId = 1;
    END IF;
END IF;
end

-- Cadastrar forma pagamento
CREATE DEFINER=`root`@`localhost` PROCEDURE `CADASTRAR_FORMA_PAGAMENTO`(OUT retId INT,
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

-- Deletar forma pagamento (pode lançar exceção, não corrigido)
CREATE DEFINER=`root`@`localhost` PROCEDURE `DELETAR_FORMA_PAGAMENTO`(OUT result INT, IN f_id INT)
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

-- Deletar aluno (pode lançar exceção, não corrigido)
CREATE DEFINER=`root`@`localhost` PROCEDURE `DELETE_ALUNO`(OUT retId INT, IN a_id INT)
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

-- Deletar professor (pode lançar exceção, não corrigido)
CREATE DEFINER=`root`@`localhost` PROCEDURE `DELETE_PROFESSOR`(OUT result INT, IN p_id INT)
BEGIN
/*DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
	ROLLBACK;
    SELECT 'An error ocurred' as msgError;
END;*/

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

-- Confirmar Pagamento
CREATE PROCEDURE stu.confirmar_pagamento(in p_id_aluno int, in p_momento_pagamento timestamp, in p_id_forma_pagamento int)
begin
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
	proximo_vencimento = adddate(@vencimento, interval 1 month),
	mensalidade = adddate(@mensalidade, interval 1 month)
	where id_aluno = p_id_aluno;
END;

-- Gerar mensalidade em aberto (necessário confirmar o processo)
CREATE DEFINER=`root`@`localhost` PROCEDURE `GERAR_MENSALIDADE_ABERTA`(OUT retId INT, IN id_aluno INT, 
IN valor_cobrar DECIMAL, IN mensalidade DATE)
BEGIN
SET retId = 0;
IF id_aluno IS NOT NULL AND EXISTS (SELECT 1 FROM view_aluno va WHERE va.id = id_aluno) THEN
	INSERT INTO mensalidade_aberta(id_aluno, proximo_vencimento, valor_cobrar) 
    VALUES (id_aluno, adddate(mensalidade, interval 1 month), valor_cobrar);
    SET retId = 1;
END IF;
END