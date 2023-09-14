package com.devs.gama.stu.utils;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
	
	public static boolean checkSession() {
		return FacesContext.getCurrentInstance().getExternalContext().getSession(false) != null;
	}
	
	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}
	
	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public static String getLoggedProfessorName() {
		return getSession().getAttribute("stuprofessorname").toString();
	}
	
	public static String getLoggedProfessorId() {
		return getSession().getAttribute("stuprofessorid").toString();
	}

}
