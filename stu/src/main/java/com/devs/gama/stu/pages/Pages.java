package com.devs.gama.stu.pages;

public enum Pages {
	home("home.xhtml"),
	login("login.xhtml"),
	meusDados("meus_dados.xhtml"),
	meusAlunos("meus_alunos.xhtml")
	;

	public String url;

	private Pages(String url) {
		this.url = url;
	}
}
