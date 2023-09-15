package com.devs.gama.stu.enums;

import java.util.Arrays;

import com.devs.gama.stu.exceptions.CallNotFoundException;

public enum ProceduresViewsTables {

	TABELA_PROFESSOR("PROFESSOR"),

	TABELA_ALUNO("ALUNO"),

	TABELA_FORMA_PAGAMENTO("FORMA_PAGAMENTO"),

	TABELA_MENSALIDADE_ABERTA("MENSALIDADE_ABERTA"),

	TABELA_MENSALIDADE_COBRADA("MENSALIDADE_COBRADA"),

	PROCEDURE_CADASTRAR_PROFESSOR("CADASTRAR_PROFESSOR"),
	
	PROCEDURE_DELETE_PROFESSOR("DELETE_PROFESSOR"),
	
	PROCEDURE_CADASTRAR_ALUNO("CADASTRAR_ALUNO"),
	
	PROCEDURE_CADASTRAR_ALUNO_FREE("CADASTRAR_ALUNO_FREE"),

	VIEW_PROFESSOR("VIEW_PROFESSOR"),

	VIEW_ALUNO("VIEW_ALUNO"),

	VIEW_FORMAS_PAGAMENTO("VIEW_FORMAS_PAGAMENTO"),

	VIEW_ALUNO_MENSALIDADES_COBRADAS("VIEW_ALUNO_MENSALIDADES_COBRADAS"),

	VIEW_ALUNO_MENSALIDADE_ABERTA("VIEW_ALUNO_MENSALIDADE_ABERTA");

	private final String value;

	ProceduresViewsTables(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static ProceduresViewsTables valueOfProceduresViews(String value) {
		return Arrays.asList(ProceduresViewsTables.values()).stream().filter(s -> s.getValue().equalsIgnoreCase(value))
				.findFirst().orElseThrow(() -> new CallNotFoundException("Procedure or View not found: " + value));
	}
}
