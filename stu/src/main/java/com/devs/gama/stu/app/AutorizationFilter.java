package com.devs.gama.stu.app;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(filterName = "StuFilter", urlPatterns = "*.xhtml")
public class AutorizationFilter implements Filter {

	Logger logger = LogManager.getLogger();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			
			HttpSession session = req.getSession(false);
			
			boolean userLogged = true;
			
			if (session == null) {
				userLogged = false;
			} else if (session.getAttribute("stuprofessornome") == null) {
				userLogged = false;
			} else if (session.getAttribute("stuprofessoremail") == null) {
				userLogged = false;
			} else if (session.getAttribute("stuprofessorid") == null) {
				userLogged = false;
			}
			
			String reqUri = req.getRequestURI();
			
			if (reqUri.contains("jakarta.faces.resource")) {
				chain.doFilter(request, response);
				return;
			}
			
			if (userLogged && reqUri.indexOf("/login.xhtml") >= 0) {
				resp.sendRedirect(req.getContextPath() + "/index.xhtml");
				return;
			}
			if (!userLogged && reqUri.indexOf("/login.xhtml") < 0) {
				resp.sendRedirect(req.getContextPath() + "/login.xhtml");
				return;
			}
			
			chain.doFilter(request, response);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
