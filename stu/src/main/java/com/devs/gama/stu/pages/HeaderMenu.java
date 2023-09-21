package com.devs.gama.stu.pages;

import java.io.IOException;
import java.io.Serializable;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.utils.MessageUtils;
import com.devs.gama.stu.utils.NavigationUtils;
import com.devs.gama.stu.utils.SessionUtils;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

@Named("stMenu")
@SessionScoped
public class HeaderMenu implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Application application;
	
	public void logout() {
		try {
			HttpSession session = SessionUtils.getSession();
			session.invalidate();
			NavigationUtils.redirect(Pages.login.url);
		} catch (IOException e) {
			application.getLogger().error(e.getMessage());
			MessageUtils.addErrorMessage("Erro ao redirecionar a página", null, e.getMessage());
		}
	}

}
