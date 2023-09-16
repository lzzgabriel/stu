package com.devs.gama.stu.app;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

	private static App instance = new App();
	
	private Logger logger = LogManager.getLogger();

	private App() {
		try {
			Context c = InitialContext.doLookup("java:comp/env");
			dataSource = (DataSource) c.lookup("jdbc/StuDB");
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public static App getInstance() {
		return instance;
	}

}
