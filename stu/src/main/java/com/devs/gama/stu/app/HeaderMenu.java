package com.devs.gama.stu.app;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

public class HeaderMenu {
	private static MenuModel menuModel;

	private HeaderMenu() {
	}

	public static MenuModel get() {
		if (menuModel == null) {
			buildMenu();
		}
		return menuModel;
	}

	private static void buildMenu() {
		menuModel = new DefaultMenuModel();

		menuModel.getElements().add(DefaultMenuItem.builder()
				.value("Home")
				.icon("pi pi-home")
				.build());
		
		menuModel.getElements().add(DefaultMenuItem.builder()
				.value("Meus dados")
				.icon("pi pi-user")
				.build());
		
		menuModel.getElements().add(DefaultMenuItem.builder()
				.value("Meus alunos")
				.icon("pi pi-book")
				.build());
		
		menuModel.getElements().add(DefaultMenuItem.builder()
				.value("Mensalidades")
				.icon("pi pi-money-bill")
				.build());

		DefaultSubMenu exitSubMenu = DefaultSubMenu.builder()
				.label("Opções")
				.icon("pi pi-cog")
				.build();
		
		exitSubMenu.getElements().add(DefaultMenuItem.builder()
				.value("Logout")
				.icon("pi pi-sign-out")
				.build());
		
		menuModel.getElements().add(exitSubMenu);
	}
}
