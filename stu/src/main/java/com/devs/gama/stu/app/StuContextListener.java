package com.devs.gama.stu.app;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

public class StuContextListener implements ServletContextListener {
	
	private Logger logger = LogManager.getLogger();

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		AbandonedConnectionCleanupThread.checkedShutdown();
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}

		}
	}
}
