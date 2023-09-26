-- stu.view_aluno_mensalidade_aberta source

create or replace
algorithm = UNDEFINED view `view_aluno_mensalidade_aberta` as
select
    `ma`.`id_aluno` as `id_aluno`,
    `a`.`nome` as `aluno_nome`,
    `ma`.`valor_cobrar` as `valor_cobrar`,
    `ma`.`status` as `status`,
    `ma`.`proximo_vencimento` as `proximo_vencimento`
from
    (`mensalidade_aberta` `ma`
join `aluno` `a` on
    ((`ma`.`id_aluno` = `a`.`id`)));


-- stu.view_aluno_mensalidades_cobradas source

create or replace
algorithm = UNDEFINED view `view_aluno_mensalidades_cobradas` as
select
    `mc`.`id_aluno` as `id_aluno`,
    `a`.`nome` as `aluno_nome`,
    `mc`.`mensalidade` as `mensalidade`,
    `mc`.`valor_cobrado` as `valor_cobrado`,
    `mc`.`data_vencimento` as `data_vencimento`,
    `mc`.`id_forma_pagamento` as `id_forma_pagamento`,
    `mc`.`momento_pagamento` as `momento_pagamento`
from
    (`mensalidade_cobrada` `mc`
left join `aluno` `a` on
    ((`mc`.`id_aluno` = `a`.`id`)));


-- stu.view_formas_pagamento source

create or replace
algorithm = UNDEFINED view `view_formas_pagamento` as
select
    `fp`.`id` as `id`,
    `fp`.`descricao` as `descricao`
from
    `forma_pagamento` `fp`;


-- stu.view_professor source

create or replace
algorithm = UNDEFINED view `view_professor` as
select
    `p`.`id` as `id`,
    `p`.`nome` as `nome`,
    `p`.`email` as `email`,
    `p`.`senha` as `senha`,
    `p`.`momento_cadastro` as `momento_cadastro`
from
    `professor` `p`;
	
-- stu.view_aluno_de_professor`

create or replace
algorithm = UNDEFINED view `view_aluno_de_professor` as
select
    `adp`.`id_aluno` as `id_aluno`,
    `adp`.`id_professor` as `id_professor`,
    `a`.`nome` as `nome`,
    `a`.`email` as `email`,
    `a`.`celular` as `celular`,
    `a`.`ativo` as `ativo`,
    `a`.`momento_cadastro` as `momento_cadastro`,
    `ma`.`valor_cobrar` as `valor_cobrar`,
    `ma`.`status` as `status`
from
    ((`aluno_de_professor` `adp`
left join `aluno` `a` on
    ((`adp`.`id_aluno` = `a`.`id`)))
left join `mensalidade_aberta` `ma` on
    ((`adp`.`id_aluno` = `ma`.`id_aluno`)));