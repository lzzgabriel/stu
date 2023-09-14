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

CREATE DEFINER=`root`@`localhost` PROCEDURE `stu`.`cadastrar_professor`(in p_nome varchar(100), in p_email varchar(100), in p_senha varchar(100))
begin
	insert into stu.professor (nome, email, senha)
	values (p_nome, p_emal, p_senha);
END;