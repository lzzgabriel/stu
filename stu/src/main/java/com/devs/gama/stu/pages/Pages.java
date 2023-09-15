package com.devs.gama.stu.pages;

public enum Pages {
	INDEX("index.xhtml"),
	LOGIN("login.xhtml");

	public String url;

	private Pages(String url) {
		this.url = url;
	}
}
