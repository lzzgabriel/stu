-- Função da trigger
CREATE OR REPLACE FUNCTION public.check_mensalidade_status()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
	IF new.proximo_vencimento < CURRENT_DATE THEN
		new.status = 'atrasada';
	ELSE
		new.status = 'em aberto';
	END IF;
	RETURN NEW;
EXCEPTION
    WHEN OTHERS THEN
        RAISE;
END;
$BODY$;

-- trigger
CREATE OR REPLACE TRIGGER check_mensalidade_status
    BEFORE INSERT OR UPDATE 
    ON public.mensalidade_aberta
    FOR EACH ROW
    EXECUTE FUNCTION public.check_mensalidade_status();