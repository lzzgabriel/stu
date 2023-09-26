CREATE DEFINER=`root`@`localhost` TRIGGER `check_mensalidade_status` AFTER UPDATE ON `mensalidade_aberta` FOR EACH ROW BEGIN
  UPDATE mensalidade_aberta
  SET status = case when new.proximo_vencimento < current_date() then 'atrasada' else 'em aberto' end
  where old.id_aluno = new.id_aluno;
end;