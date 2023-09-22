CREATE EVENT check_mensalidades_diario
ON SCHEDULE EVERY 1 DAY
STARTS timestamp(current_date())
ON COMPLETION NOT PRESERVE
DISABLE ON SLAVE
DO begin 
update mensalidade_aberta set status = 'atrasado'
where proximo_vencimento < current_date();
end;