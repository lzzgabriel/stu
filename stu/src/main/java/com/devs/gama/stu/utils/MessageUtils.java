package com.devs.gama.stu.utils;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.context.FacesContext;

public class MessageUtils {

	public static void addInfoMessage(String summary) {
		addInfoMessage(summary, null);
	}

	public static void addInfoMessage(String summary, String clientId) {
		addInfoMessage(summary, clientId, null);
	}
	
	public static void addInfoMessage(String summary, String clientId, String detail) {
		addMessage(FacesMessage.SEVERITY_INFO, summary, detail, clientId);
	}
	
	public static void addWarningMessage(String summary) {
		addWarningMessage(summary, null);
	}

	public static void addWarningMessage(String summary, String clientId) {
		addWarningMessage(summary, clientId, null);
	}
	
	public static void addWarningMessage(String summary, String clientId, String detail) {
		addMessage(FacesMessage.SEVERITY_WARN, summary, detail, clientId);
	}
	
	public static void addErrorMessage(String summary) {
		addErrorMessage(summary, null);
	}

	public static void addErrorMessage(String summary, String clientId) {
		addErrorMessage(summary, clientId, null);
	}
	
	public static void addErrorMessage(String summary, String clientId, String detail) {
		addMessage(FacesMessage.SEVERITY_ERROR, summary, detail, clientId);
	}
	
	public static void addFatalMessage(String summary, String clientId, String detail) {
		addMessage(FacesMessage.SEVERITY_FATAL, summary, detail, clientId);
	}

	private static void addMessage(Severity severity, String summary, String detail, String clientId) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(clientId, message);
	}

}
