package com.devs.gama.stu.utils;

import java.io.IOException;

import jakarta.faces.context.FacesContext;

public class NavigationUtils {
	
	public static void redirect(String url) throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(url);
	}

}
