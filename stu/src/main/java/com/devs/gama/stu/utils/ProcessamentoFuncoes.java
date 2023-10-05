package com.devs.gama.stu.utils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import com.devs.gama.stu.exceptions.CallFailedException;

public final class ProcessamentoFuncoes {

	private ProcessamentoFuncoes() {
	}

	public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
		try {
			if (Objects.nonNull(preparedStatement) && !preparedStatement.isClosed()) {
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
			if (Objects.nonNull(resultSet) && !resultSet.isClosed()) {
				resultSet.close();
			}
		} catch (SQLException e) {
			resultSet = null;
			throw e;
		}
	}

	@Deprecated
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

	public static void finalizarFuncao(PreparedStatement preparedStatement) throws SQLException {
		if (Objects.isNull(preparedStatement) || preparedStatement.isClosed()) {
			return;
		}
		try (ResultSet resultSet = preparedStatement.getResultSet()) {
			if (Objects.nonNull(resultSet) && resultSet.next()) {
				int result = resultSet.getInt("id_retorno");
				if (result > 0) {
					return;
				} else {
					throw new CallFailedException("Falha no processo");
				}
			}
		}
	}

}
