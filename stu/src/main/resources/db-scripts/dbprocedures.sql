CREATE DEFINER=`root`@`localhost` PROCEDURE `stu`.`cadastrar_aluno`(
in p_nome varchar(100),
in p_email varchar(100),
in p_celular varchar(11),
in p_id_professor int,
in p_valor_cobrar decimal(10,2),
in p_proximo_vencimento date)
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
	insert into mensalidade_aberta(
		valor_cobrar,
		proximo_vencimento
		)
		values (
		p_valor_cobrar,
		p_proximo_vencimento);
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