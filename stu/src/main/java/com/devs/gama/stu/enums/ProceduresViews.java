package com.devs.gama.stu.enums;

import java.util.Arrays;

import exceptions.CallNotFoundException;

public enum ProceduresViews {

	PROCEDURE_CADASTRAR_PROFESSOR("CADASTRAR_PROFESSOR"),

	PROCEDURE_CADASTRAR_ALUNO("CADASTRAR_ALUNO"),

	PROCEDURE_CADASTRAR_ALUNO_FREE("CADASTRAR_ALUNO_FREE"),

	VIEW_PROFESSOR("VIEW_PROFESSOR"),

	VIEW_ALUNO("VIEW_ALUNO"),

	VIEW_ALUNO_MENSALIDADES_COBRADAS("VIEW_ALUNO_MENSALIDADES_COBRADAS"),

	VIEW_ALUNO_MENSALIDADE_ABERTA("VIEW_ALUNO_MENSALIDADE_ABERTA");

	private final String value;

	ProceduresViews(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static ProceduresViews valueOfProceduresViews(String value) {
		return Arrays.asList(ProceduresViews.values()).stream().filter(s -> s.getValue().equalsIgnoreCase(value))
				.findFirst().orElseThrow(() -> new CallNotFoundException("Procedure or View not found: " + value));
	}
}
