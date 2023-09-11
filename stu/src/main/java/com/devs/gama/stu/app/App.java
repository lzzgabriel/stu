package com.devs.gama.stu.app;

import java.io.Serializable;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("stApp")
@ApplicationScoped
public class App implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Locale locale = new Locale("pt", "BR");
	private static DataSource dataSource;
	
	private String dbName = "stu";

	public App() {
		PoolProperties poolProperties = new PoolProperties();
		poolProperties.setUrl("jdbc:mysql://localhost:3306/" + dbName);
		poolProperties.setDriverClassName("com.mysql.jdbc.Driver");
		poolProperties.setUsername("stustd");
		poolProperties.setPassword("senha");
		dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

}
