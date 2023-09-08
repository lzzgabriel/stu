package com.devs.gama.stu.app;

import org.primefaces.model.menu.MenuModel;

public class HeaderMenu {
	private static MenuModel menuModel;
	private HeaderMenu() {}
	public static MenuModel get() {
		if (menuModel == null) {
			buildMenu();
		}
		return menuModel;
	}
	private static void buildMenu() {
		
	}
}
