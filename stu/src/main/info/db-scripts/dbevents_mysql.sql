CREATE EVENT check_mensalidades_diario
ON SCHEDULE EVERY 1 DAY
STARTS timestamp(utc_date)
ON COMPLETION NOT PRESERVE
DISABLE ON SLAVE
DO begin 
update mensalidade_aberta set status = 'atrasada'
where proximo_vencimento < current_date();
end;