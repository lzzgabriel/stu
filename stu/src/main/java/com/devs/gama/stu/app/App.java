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
	private DataSource dataSource;

	public App() {
		PoolProperties poolProperties = new PoolProperties();
		//poolProperties.setUrl("jdbc:mysql://localhost:5432/" + "my_database_name");
		//poolProperties.setDriverClassName("org.postgresql.Driver");
		//poolProperties.setUsername("someUserName");
		//poolProperties.setPassword("somePassword");
		//dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
