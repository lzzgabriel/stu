CREATE OR REPLACE FUNCTION public.alterar_senha_professor(
	OUT id_retorno integer,
	IN id_professor integer,
	IN senhaatual character varying,
	IN senhadestino character varying)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    professor_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF id_professor IS NULL THEN
		RETURN;
	END IF;
    SELECT EXISTS (SELECT 1 from professor p WHERE p.id = id_professor AND p.senha = senhaAtual) INTO professor_exists;
    IF professor_exists THEN
        UPDATE professor SET senha = senhaDestino WHERE id = id_professor;
        id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.associar_aluno_professor(
	OUT id_retorno integer,
	id_p integer,
	id_a integer)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    professor_exists BOOLEAN;
    student_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF id_p IS NULL OR id_a IS NULL THEN
		RETURN;
	END IF;
    SELECT EXISTS (SELECT 1 FROM professor p WHERE p.id = id_p) INTO professor_exists;
    SELECT EXISTS (SELECT 1 FROM aluno a WHERE a.id = id_a) INTO student_exists;
    IF professor_exists AND student_exists THEN
        INSERT INTO aluno_de_professor(id_aluno, id_professor) VALUES (id_a, id_p);
        id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.cadastrar_aluno(
	OUT id_retorno integer,
	a_nome character varying,
	a_email character varying,
	a_celular character varying,
	p_id integer,
	valor numeric,
	mensalidade date)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    novo_id INT;
    resultado_associacao INT;
    resultado_gerar_mensalidade INT;
BEGIN
	id_retorno = 0;
	IF p_id IS NULL THEN
		RETURN;
	END IF;
    INSERT INTO aluno(nome, email, celular) VALUES (a_nome, a_email, a_celular)
    RETURNING id INTO novo_id;
    select * from ASSOCIAR_ALUNO_PROFESSOR(p_id, novo_id) INTO resultado_associacao;
    select * from GERAR_MENSALIDADE_ABERTA(novo_id, valor, mensalidade) INTO resultado_gerar_mensalidade;
    IF resultado_associacao = 1 AND resultado_gerar_mensalidade = 1 THEN
        id_retorno = novo_id;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.cadastrar_forma_pagamento(
	OUT id_retorno integer,
	f_descricao character varying)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    novo_id INT;
BEGIN
	id_retorno = 0;
    INSERT INTO forma_pagamento (descricao)
    VALUES (f_descricao)
    RETURNING id INTO novo_id;
    id_retorno = novo_id;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.cadastrar_professor(
	OUT id_retorno integer,
	p_nome character varying,
	p_email character varying,
	p_senha character varying)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    novo_id INT;
BEGIN
	id_retorno = 0;
	INSERT INTO professor (nome, email, senha)
    VALUES (p_nome, p_email, p_senha)
    RETURNING id INTO novo_id;
    id_retorno := novo_id;
	
	EXCEPTION WHEN OTHERS THEN
		RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.confirmar_pagamento(
	OUT id_retorno integer,
	p_id_aluno integer,
	p_momento_pagamento timestamp without time zone,
	p_id_forma_pagamento integer)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    id INT;
    valor DECIMAL;
    vencimento DATE;
    mensalidade DECIMAL;
	mensalidade_exists boolean;
BEGIN
	id_retorno = 0;
	IF p_id_aluno IS NULL THEN
		RETURN;
	END IF;
	SELECT EXISTS (SELECT 1 FROM mensalidade_aberta ma WHERE ma.id_aluno = p_id_aluno) INTO mensalidade_exists;
	IF mensalidade_exists THEN
		SELECT id_aluno, valor_cobrar, proximo_vencimento, mensalidade
		FROM mensalidade_aberta
		WHERE id_aluno = p_id_aluno INTO id, valor, vencimento, mensalidade;

		INSERT INTO mensalidade_cobrada (id_aluno, valor_cobrado, data_vencimento, id_forma_pagamento, momento_pagamento)
		VALUES (id, valor, vencimento, p_id_forma_pagamento, p_momento_pagamento);

		UPDATE mensalidade_aberta
		SET proximo_vencimento = vencimento + INTERVAL '1 month'
		WHERE id_aluno = p_id_aluno;
		id_retorno = 1;
	END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.ativar_aluno(
	OUT id_retorno integer,
	a_id integer)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    aluno_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF a_id IS NULL THEN
		RETURN;
	END IF;
	SELECT EXISTS (SELECT 1 FROM aluno WHERE id = a_id) INTO aluno_exists;
	IF aluno_exists THEN
		UPDATE aluno SET ativo = true WHERE id = a_id;
		id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.inativar_aluno(
	OUT id_retorno integer,
	a_id integer)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    aluno_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF a_id IS NULL THEN
		RETURN;
	END IF;
	SELECT EXISTS (SELECT 1 FROM aluno WHERE id = a_id) INTO aluno_exists;
	IF aluno_exists THEN
		UPDATE aluno SET ativo = false WHERE id = a_id;
		id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.delete_forma_pagamento(
	OUT id_retorno integer,
	f_id integer)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    forma_pagamento_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF f_id IS NULL THEN
		RETURN;
	END IF;
	SELECT EXISTS (SELECT 1 FROM forma_pagamento f WHERE f.id = f_id) INTO forma_pagamento_exists;
	IF forma_pagamento_exists THEN
		DELETE FROM forma_pagamento WHERE id = f_id;
		id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.ativar_professor(
	OUT id_retorno integer,
	p_id integer)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    professor_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF p_id IS NULL THEN
		RETURN;
	END IF;
	SELECT EXISTS (SELECT 1 FROM professor WHERE id = p_id) INTO professor_exists;
	IF professor_exists THEN
		UPDATE professor SET ativo = true WHERE id = p_id;
		id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.inativar_professor(
	OUT id_retorno integer,
	p_id integer)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    professor_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF p_id IS NULL THEN
		RETURN;
	END IF;
	SELECT EXISTS (SELECT 1 FROM professor WHERE id = p_id) INTO professor_exists;
	IF professor_exists THEN
		UPDATE professor SET ativo = false WHERE id = p_id;
		id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.editar_aluno(
	OUT id_retorno integer,
	a_id integer,
	a_nome character varying,
	a_email character varying,
	a_celular character varying)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    aluno_exists BOOLEAN;
BEGIN
	id_retorno = 0;
    SELECT EXISTS (SELECT 1 FROM aluno WHERE id = a_id) INTO aluno_exists;
    IF aluno_exists THEN
        UPDATE aluno SET nome = a_nome, email = a_email, celular = a_celular
        WHERE id = a_id;
        id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.editar_forma_pagamento(
	OUT id_retorno integer,
	f_id integer,
	f_descricao character varying)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    forma_pagamento_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF f_id IS NULL THEN
		RETURN;
	END IF;
    SELECT EXISTS (SELECT 1 FROM forma_pagamento fp WHERE fp.id = f_id) INTO forma_pagamento_exists;
    IF forma_pagamento_exists THEN
        UPDATE forma_pagamento SET descricao = f_descricao WHERE id = f_id;
        id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.editar_mensalidade_aberta(
	OUT id_retorno integer,
	a_id integer,
	valor_atualizado numeric,
	nova_data date)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    mensalidade_aberta_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF a_id IS NULL THEN
		RETURN;
	END IF;
    
	SELECT EXISTS (SELECT 1 FROM mensalidade_aberta ma WHERE ma.id_aluno = a_id) INTO mensalidade_aberta_exists;
	IF mensalidade_aberta_exists THEN
		UPDATE mensalidade_aberta SET valor_cobrar = valor_atualizado, proximo_vencimento = nova_data WHERE id_aluno = a_id;
		id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.editar_professor(
	OUT id_retorno integer,
	p_id integer,
	p_nome character varying,
	p_email character varying)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    professor_exists BOOLEAN;
BEGIN
	id_retorno = 0;
	IF p_id IS NULL THEN
		RETURN;
	END IF;
    SELECT EXISTS (SELECT 1 FROM professor p WHERE p.id = p_id AND p.ativo = true) INTO professor_exists;
    IF professor_exists THEN
        UPDATE professor SET nome = p_nome, email = p_email WHERE id = p_id;
        id_retorno = 1;
		RETURN;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.gerar_mensalidade_aberta(
	OUT id_retorno integer,
	a_id integer,
	valor_cobrar numeric,
	mensalidade date)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
	aluno_exists boolean;
BEGIN
	id_retorno = 0;
	IF a_id IS NULL THEN
		RETURN;
	END IF;
	SELECT EXISTS (SELECT 1 FROM aluno a WHERE a.id = a_id) INTO aluno_exists;
    IF aluno_exists THEN
		INSERT INTO mensalidade_aberta(id_aluno, proximo_vencimento, valor_cobrar) VALUES (a_id, mensalidade, valor_cobrar);
		id_retorno = 1;
    	RETURN;
	END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.atualizar_status(
	)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    
    UPDATE mensalidade_aberta
    SET status = 'atrasada'
    WHERE proximo_vencimento < CURRENT_DATE;

END;
$BODY$;