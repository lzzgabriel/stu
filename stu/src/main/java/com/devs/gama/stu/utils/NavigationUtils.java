package com.devs.gama.stu.utils;

import java.io.IOException;

import com.devs.gama.stu.pages.Pages;

import jakarta.faces.context.FacesContext;

public class NavigationUtils {
	
	public static void redirect(String url) throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(url);
	}
	
	public static void redirect(Pages page) throws IOException {
		redirect(page.url);
	}

}
