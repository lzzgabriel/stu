CREATE DEFINER=`root`@`localhost` PROCEDURE `stu`.`cadastrar_aluno`(
in p_nome varchar(100),
in p_email varchar(100),
in p_celular varchar(11),
in p_id_professor int,
in p_valor_cobrar decimal(10,2),
in p_proximo_vencimento date,
in p_mensalidade date)
begin
	insert into stu.aluno(
		nome,
		email,
		celular)
		values (
		p_nome,
		p_email,
		p_celular);
	select @novo_id := (SELECT LAST_INSERT_ID(a.id) from aluno a order by LAST_INSERT_ID(a.id) desc limit 1);
	insert into stu.aluno_de_professor (id_aluno , id_professor)
	values (@novo_id, p_id_professor);
	insert into mensalidade_aberta(
		id_aluno,
		valor_cobrar,
		proximo_vencimento,
		mensalidade
		)
		values (
		@novo_id,
		p_valor_cobrar,
		p_proximo_vencimento,
		p_mensalidade);
END;

CREATE DEFINER=`root`@`localhost` PROCEDURE `stu`.`cadastrar_aluno_free`(
in p_nome varchar(100),
in p_email varchar(100),
in p_celular varchar(11),
in p_id_professor int)
begin
	insert into stu.aluno(
		nome,
		email,
		celular,
		id_professor)
		values (
		p_nome,
		p_email,
		p_celular,
		p_id_professor);
END;

CREATE DEFINER=`root`@`localhost` PROCEDURE `cadastrar_professor`PROCEDURE `cadastrar_professor`(OUT retId INT,
         IN p_id INT,
         IN p_nome VARCHAR(100),
         IN p_email VARCHAR(100),
         IN p_senha VARCHAR(100))
BEGIN
	IF p_id IS NULL THEN
 INSERT INTO stu.professor (nome,
         email,
         senha) VALUES (p_nome,
         p_email,
         p_senha);
 SET retId = LAST_INSERT_ID();
 ELSE
 INSERT INTO stu.professor (id,
         nome,
         email,
         senha) VALUES (p_id,
         p_nome,
         p_email,
         p_senha)
    ON DUPLICATE KEY UPDATE
 nome = VALUES(nome),
 email = VALUES(email),
 senha = VALUES(senha);
 SET retId = p_id;
    END IF;
END

CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_professor`(OUT result INT, IN p_id INT)
BEGIN
	DELETE FROM professor p WHERE p.id = p_id;
    SET result = 1;
END