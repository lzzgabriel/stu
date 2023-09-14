package com.devs.gama.stu.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {

	void save(T t) throws SQLException;

	void edit(T t) throws SQLException;

	void delete(T t) throws SQLException;

	List<T> findAll() throws SQLException;

	List<T> findAllFiltered(T t) throws SQLException;

	T findById(int id) throws SQLException;
	
	T fetch(ResultSet res) throws SQLException;
	
}
