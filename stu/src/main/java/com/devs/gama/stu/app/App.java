package com.devs.gama.stu.app;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;

public class App {

	private static DataSource dataSource;

	public static DataSource getDataSource() {
		if (dataSource == null) {
			PoolProperties poolProperties = new PoolProperties();
			
			poolProperties.setUrl("jdbc:mysql://localhost:3306/stu");
			poolProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
			poolProperties.setUsername("stustd");
			poolProperties.setPassword("senha");
			
			dataSource = new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);
		}
		return dataSource;
	}

}
