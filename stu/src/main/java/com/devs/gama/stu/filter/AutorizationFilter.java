package com.devs.gama.stu.filter;

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
			
			String reqUri = req.getRequestURI();
			if (reqUri.indexOf(reqUri) >= 0 || (session != null && session.getAttribute("stuprofessorname") != null) || reqUri.indexOf("/public/") >= 0 || reqUri.contains("jakarta.faces.resource")) {
				chain.doFilter(request, response);
			} else {
				resp.sendRedirect(req.getContextPath() + "/faces/login.xhtml");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
