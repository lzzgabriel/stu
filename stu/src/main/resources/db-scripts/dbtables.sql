-- stu.forma_pagamento definition

CREATE TABLE `forma_pagamento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descricao` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- stu.professor definition

CREATE TABLE `professor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `senha` varchar(100) NOT NULL,
  `momento_cadastro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `professor_un` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- stu.aluno definition

CREATE TABLE `aluno` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `celular` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_professor` int NOT NULL,
  `momento_cadastro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_professor` (`id_professor`),
  CONSTRAINT `fk_professor` FOREIGN KEY (`id_professor`) REFERENCES `professor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- stu.mensalidade_aberta definition

CREATE TABLE `mensalidade_aberta` (
  `id_aluno` int NOT NULL,
  `valor_cobrar` decimal(10,2) NOT NULL,
  `proximo_vencimento` date NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'em aberto',
  `mensalidade` date NOT NULL,
  `momento_ultimo_pagamento` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_aluno`),
  CONSTRAINT `mensalidade_aluno_ativa_FK` FOREIGN KEY (`id_aluno`) REFERENCES `aluno` (`id`),
  CONSTRAINT `check_status` CHECK ((`status` in (_utf8mb4'em aberto',_utf8mb4'atrasada')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- stu.mensalidade_cobrada definition

CREATE TABLE `mensalidade_cobrada` (
  `id_aluno` int NOT NULL,
  `mensalidade` date NOT NULL,
  `valor_cobrado` decimal(10,0) DEFAULT NULL,
  `data_vencimento` date DEFAULT NULL,
  `id_forma_pagamento` int NOT NULL,
  `momento_pagamento` timestamp NOT NULL,
  PRIMARY KEY (`id_aluno`,`mensalidade`),
  KEY `mensalidade_cobrada_FK` (`id_forma_pagamento`),
  CONSTRAINT `mensalidade_cobrada_FK` FOREIGN KEY (`id_forma_pagamento`) REFERENCES `forma_pagamento` (`id`),
  CONSTRAINT `mensalidades_cobradas_FK` FOREIGN KEY (`id_aluno`) REFERENCES `aluno` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;