-- public.aluno definition

-- Drop table

-- DROP TABLE aluno;

CREATE TABLE aluno (
	id bigserial NOT NULL,
	nome text NOT NULL,
	email text NULL,
	celular varchar(11) NOT NULL,
	ativo bool NULL DEFAULT true,
	momento_cadastro timestamp NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT aluno_pk PRIMARY KEY (id),
	CONSTRAINT aluno_un UNIQUE (email)
);


-- public.forma_pagamento definition

-- Drop table

-- DROP TABLE forma_pagamento;

CREATE TABLE forma_pagamento (
	id bigserial NOT NULL,
	descricao text NULL,
	CONSTRAINT forma_pagamento_pk PRIMARY KEY (id)
);


-- public.professor definition

-- Drop table

-- DROP TABLE professor;

CREATE TABLE professor (
	id bigserial NOT NULL,
	nome text NOT NULL,
	email text NOT NULL,
	senha text NOT NULL,
	ativo bool NOT NULL DEFAULT true,
	momento_cadastro timestamp NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT professor_pk PRIMARY KEY (id),
	CONSTRAINT professor_un UNIQUE (email)
);


-- public.aluno_de_professor definition

-- Drop table

-- DROP TABLE aluno_de_professor;

CREATE TABLE aluno_de_professor (
	id_aluno int8 NOT NULL,
	id_professor int8 NOT NULL,
	CONSTRAINT aluno_de_professor_pk PRIMARY KEY (id_aluno, id_professor),
	CONSTRAINT aluno_de_professor_fk FOREIGN KEY (id_aluno) REFERENCES aluno(id),
	CONSTRAINT aluno_de_professor_fk_1 FOREIGN KEY (id_professor) REFERENCES professor(id)
);


-- public.mensalidade_aberta definition

-- Drop table

-- DROP TABLE mensalidade_aberta;

CREATE TABLE mensalidade_aberta (
	id_aluno int8 NOT NULL,
	valor_cobrar numeric(10, 2) NOT NULL,
	proximo_vencimento date NOT NULL,
	status varchar(10) NOT NULL DEFAULT 'em aberto'::character varying,
	momento_ultimo_pagamento timestamp NULL,
	CONSTRAINT mensalidade_aberta_check CHECK (((status)::text = ANY ((ARRAY['em aberto'::character varying, 'atrasada'::character varying])::text[]))),
	CONSTRAINT mensalidade_aberta_pk PRIMARY KEY (id_aluno),
	CONSTRAINT mensalidade_aberta_fk FOREIGN KEY (id_aluno) REFERENCES aluno(id)
);


-- public.mensalidade_cobrada definition

-- Drop table

-- DROP TABLE mensalidade_cobrada;

CREATE TABLE mensalidade_cobrada (
	id_aluno int8 NOT NULL,
	valor_cobrado numeric(10, 2) NULL DEFAULT NULL::numeric,
	data_vencimento date NOT NULL,
	id_forma_pagamento int8 NOT NULL,
	momento_pagamento timestamp NOT NULL,
	CONSTRAINT mensalidade_cobrada_pk PRIMARY KEY (id_aluno, data_vencimento),
	CONSTRAINT mensalidade_cobrada_fk FOREIGN KEY (id_forma_pagamento) REFERENCES forma_pagamento(id),
	CONSTRAINT mensalidade_cobrada_fk_2 FOREIGN KEY (id_aluno) REFERENCES aluno(id)
);