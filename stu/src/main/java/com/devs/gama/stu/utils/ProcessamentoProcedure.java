package com.devs.gama.stu.utils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.devs.gama.stu.exceptions.CallFailedException;

public class ProcessamentoProcedure {

	public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
		try {
			if (preparedStatement != null && !preparedStatement.isClosed()) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
			preparedStatement = null;
			throw e;
		}
	}

	public static void closeCallableStatement(CallableStatement callableStatement) throws SQLException {

		try {
			if (callableStatement != null && !callableStatement.isClosed()) {
				callableStatement.close();
			}
		} catch (SQLException e) {
			callableStatement = null;
			throw e;
		}
	}

	public static void closeResultSet(ResultSet resultSet) throws SQLException {

		try {
			if (resultSet != null && !resultSet.isClosed()) {
				resultSet.close();
			}
		} catch (SQLException e) {
			resultSet = null;
			throw e;
		}
	}

	public static void finalizarProcedure(CallableStatement callableStatement, int posicaoResult) throws SQLException {
		if (callableStatement != null && !callableStatement.isClosed()) {
			int result = callableStatement.getInt(posicaoResult);
			if (result > 0) {
				return;
			} else {
				throw new CallFailedException("Falha no processo");
			}
		}
	}

//	public void finalizarProcedure(CallableStatement callableStatement, int posicaoResult, String posicaoMsg)
//			throws SQLException {
//		if (callableStatement != null && !callableStatement.isClosed()) {
//			callableStatement.getInt(posicaoResult);
//		}
//	}

//	public void finalizarProcedure(CallableStatement callableStatement, int posicaoResult, int posicaoMsg,
//			int posicaoTimestamp) throws SQLException {
//		if (callableStatement != null && !callableStatement.isClosed()) {
//			callableStatement.getInt(posicaoResult);
//		}
//	}

}
