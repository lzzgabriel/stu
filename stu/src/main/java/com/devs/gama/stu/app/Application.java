package com.devs.gama.stu.app;

import java.io.Serializable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("stApplication")
@ApplicationScoped
public class Application implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getRootLogger();
	
	private DataSource dataSource;
	
	@PostConstruct
	public void init() {
		try {
			Context c = InitialContext.doLookup("java:");
			dataSource = (DataSource) c.lookup("/StuDB");
			
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

}
