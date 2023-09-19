package com.devs.gama.stu.app;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("stSession")
@SessionScoped
public class UserSession implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public UserSession() {
	}

}
