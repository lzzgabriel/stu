package com.devs.gama.stu.app;

import java.io.Serializable;

import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("stSession")
@SessionScoped
public class UserSession implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private MenuModel menuModel;

	public UserSession() {
	}

	public MenuModel getMenuModel() {
		if (menuModel == null) {
			menuModel = HeaderMenu.get();
		}
		return menuModel;
	}

	public void setMenuModel(DefaultMenuModel menuModel) {
		this.menuModel = menuModel;
	}
	
	public void checkSession() {
	}
	
}
