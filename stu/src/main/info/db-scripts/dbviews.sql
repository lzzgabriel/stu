-- public.view_aluno_de_professor source

CREATE OR REPLACE VIEW public.view_aluno_de_professor
AS SELECT adp.id_aluno,
    adp.id_professor,
    a.nome,
    a.email,
    a.celular,
    a.ativo,
    a.momento_cadastro,
    ma.valor_cobrar,
    ma.status
   FROM aluno_de_professor adp
     LEFT JOIN aluno a ON adp.id_aluno = a.id
     LEFT JOIN mensalidade_aberta ma ON adp.id_aluno = ma.id_aluno;


-- public.view_aluno_mensalidade_aberta source

CREATE OR REPLACE VIEW public.view_aluno_mensalidade_aberta
 AS
 SELECT ma.id_aluno,
    a.nome AS aluno_nome,
    ma.valor_cobrar,
    ma.status,
    ma.proximo_vencimento,
    adp.id_professor
   FROM mensalidade_aberta ma
     JOIN aluno a ON ma.id_aluno = a.id
     LEFT JOIN aluno_de_professor adp ON adp.id_aluno = ma.id_aluno;


-- public.view_aluno_mensalidades_cobradas source

CREATE OR REPLACE VIEW public.view_aluno_mensalidades_cobradas
 AS
 SELECT mc.id_aluno,
    a.nome AS aluno_nome,
    mc.valor_cobrado,
    mc.data_vencimento,
    mc.id_forma_pagamento,
    mc.momento_pagamento,
    adp.id_professor
   FROM mensalidade_cobrada mc
     LEFT JOIN aluno a ON mc.id_aluno = a.id
     LEFT JOIN aluno_de_professor adp ON adp.id_aluno = mc.id_aluno;


-- public.view_formas_pagamento source

CREATE OR REPLACE VIEW public.view_formas_pagamento
AS SELECT fp.id,
    fp.descricao
   FROM forma_pagamento fp;


-- public.view_professor source

CREATE OR REPLACE VIEW public.view_professor
AS SELECT p.id,
    p.nome,
    p.email,
    p.senha,
    p.momento_cadastro,
	p.ativo
   FROM professor p;