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

CREATE OR REPLACE FUNCTION public.associar_aluno_professor(id_p integer, id_a integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE
    retId INT := 0;
    professor_exists BOOLEAN;
    student_exists BOOLEAN;
BEGIN
    SELECT EXISTS (SELECT 1 FROM view_professor vp WHERE vp.id = id_p) INTO professor_exists;
    SELECT EXISTS (SELECT 1 FROM aluno a WHERE a.id = id_a) INTO student_exists;
    IF professor_exists AND student_exists THEN
        INSERT INTO aluno_de_professor(id_aluno, id_professor) VALUES (id_a, id_p);
        retId := 1;
    END IF;
    RETURN retId;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.cadastrar_aluno(a_nome character varying, a_email character varying, a_celular character varying, p_id integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE
    retId INT := 0;
    novo_id INT;
    resultAssociacao INT;
    resultGerarMensalidade INT;
BEGIN
    INSERT INTO stu.aluno(nome, email, celular)
    VALUES (a_nome, a_email, a_celular)
    RETURNING id INTO novo_id;
    PERFORM ASSOCIAR_ALUNO_PROFESSOR(resultAssociacao, p_id, novo_id);
    PERFORM GERAR_MENSALIDADE_ABERTA(resultGerarMensalidade, novo_id, 400.0, '2023-09-20');
    IF resultAssociacao = 1 AND resultGerarMensalidade = 1 THEN
        retId := novo_id;
    END IF;
    RETURN retId;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.cadastrar_forma_pagamento(f_descricao character varying)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE
    retId INT := 0;
    novo_id INT;
BEGIN
    INSERT INTO stu.forma_pagamento (descricao)
    VALUES (f_descricao)
    RETURNING id INTO novo_id;
    retId := novo_id;
    RETURN retId;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$function$
;

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

CREATE OR REPLACE FUNCTION public.confirmar_pagamento(p_id_aluno integer, p_momento_pagamento timestamp without time zone, p_id_forma_pagamento integer)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
DECLARE
    id INT;
    valor DECIMAL;
    vencimento DATE;
    mensalidade DECIMAL;
BEGIN
    SELECT id_aluno, valor_cobrar, proximo_vencimento, mensalidade
    INTO id, valor, vencimento, mensalidade
    FROM stu.mensalidade_aberta
    WHERE id_aluno = p_id_aluno;

    INSERT INTO stu.mensalidade_cobrada (id_aluno, mensalidade, valor_cobrado, data_vencimento, id_forma_pagamento, momento_pagamento)
    VALUES (id, mensalidade, valor, vencimento, p_id_forma_pagamento, p_momento_pagamento);

    UPDATE stu.mensalidade_aberta
    SET proximo_vencimento = vencimento + INTERVAL '1 month'
    WHERE id_aluno = p_id_aluno;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.delete_aluno(a_id integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE
    retId INT := 0;
    aluno_exists BOOLEAN;
BEGIN
    IF a_id IS NOT NULL THEN
        SELECT EXISTS (SELECT 1 FROM view_aluno va WHERE va.id = a_id) INTO aluno_exists;
        IF aluno_exists THEN
            DELETE FROM stu.aluno a WHERE a.id = a_id;
            retId := 1;
        END IF;
    END IF;
    RETURN retId;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.delete_forma_pagamento(f_id integer)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE
    result INT := 0;
    forma_pagamento_exists BOOLEAN;
BEGIN
    IF f_id IS NOT NULL THEN
        SELECT EXISTS (SELECT 1 FROM view_formas_pagamento vf WHERE vf.id = f_id) INTO forma_pagamento_exists;
        IF forma_pagamento_exists THEN
            DELETE FROM stu.forma_pagamento f WHERE f.id = f_id;
            result := 1;
        END IF;
    END IF;
    RETURN result;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.delete_professor(
	OUT id_retorno integer,
	IN p_id integer)
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
	SELECT EXISTS (SELECT 1 FROM professor p WHERE p.id = p_id) INTO professor_exists;
	IF professor_exists THEN
 		DELETE FROM professor WHERE id = p_id;
		id_retorno = 1;
		RETURN;
	END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.editar_aluno(a_id integer, a_nome character varying, a_email character varying, a_celular character varying)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE
    retId INT := 0;
    aluno_exists BOOLEAN;
BEGIN
    SELECT EXISTS (SELECT 1 FROM stu.aluno a WHERE a.id = a_id) INTO aluno_exists;
    IF aluno_exists THEN
        UPDATE stu.aluno a SET a.nome = a_nome, a.email = a_email, a.celular = a_celular
        WHERE a.id = a_id;
        retId := 1;
    END IF;
    RETURN retId;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.editar_forma_pagamento(f_id integer, f_descricao character varying)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE
    result INT := 0;
    forma_pagamento_exists BOOLEAN;
BEGIN
    SELECT EXISTS (SELECT 1 FROM stu.forma_pagamento fp WHERE fp.id = f_id) INTO forma_pagamento_exists;
    IF forma_pagamento_exists THEN
        UPDATE stu.forma_pagamento fp SET fp.descricao = f_descricao WHERE fp.id = f_id;
        result := 1;
    END IF;
    RETURN result;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.editar_mensalidade_aberta(a_id integer, valor_atualizado numeric, nova_data date)
 RETURNS integer
 LANGUAGE plpgsql
AS $function$
DECLARE
    retId INT := 0;
    mensalidade_aberta_exists BOOLEAN;
BEGIN
    IF a_id IS NOT NULL THEN
        SELECT EXISTS (SELECT 1 FROM stu.mensalidade_aberta ma WHERE ma.id_aluno = a_id) INTO mensalidade_aberta_exists;
        IF mensalidade_aberta_exists THEN
            UPDATE stu.mensalidade_aberta ma SET ma.valor_cobrar = valor_atualizado, ma.proximo_vencimento = nova_data WHERE ma.id_aluno = a_id;
            retId := 1;
        END IF;
    END IF;
    RETURN retId;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE;
END;
$function$
;

CREATE OR REPLACE FUNCTION public.editar_professor(
	OUT id_retorno integer,
	p_id integer,
	p_nome character varying,
	p_email character varying,
	p_senha character varying)
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