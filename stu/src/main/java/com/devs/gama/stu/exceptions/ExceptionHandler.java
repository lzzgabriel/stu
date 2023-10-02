package com.devs.gama.stu.exceptions;

import java.io.IOException;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.pages.Pages;
import com.devs.gama.stu.utils.NavigationUtils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("stExceptionHandler")
@RequestScoped
public class ExceptionHandler {
	
	@Inject
	private Application application;
	
	public void sendHome() {
		try {
			NavigationUtils.redirect(Pages.home);
		} catch (IOException e) {
			application.getLogger().error(e.getMessage(), e);
		}
	}
	
	public void reload() {
	}
	
}
