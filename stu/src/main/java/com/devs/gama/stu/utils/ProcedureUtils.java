package com.devs.gama.stu.utils;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ProcedureUtils {

	/**
	 * Metódo para setar uma String no CallableStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo String
	 * @param callableStatement callablestatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setString(int parametro, String value, CallableStatement callableStatement) throws SQLException {
		if (Objects.isNull(value) || value.isEmpty()) {
			callableStatement.setNull(parametro, Types.VARCHAR);
		} else {
			callableStatement.setString(parametro, value);
		}
	}

	/**
	 * Metódo para setar um Integer no CallableStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo Integer
	 * @param callableStatement callablestatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setInt(int parametro, Integer value, CallableStatement callableStatement) throws SQLException {
		if (Objects.isNull(value)) {
			callableStatement.setNull(parametro, Types.INTEGER);
		} else {
			callableStatement.setInt(parametro, value);
		}
	}

	/**
	 * Metódo para setar um Double no CallableStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo Double
	 * @param callableStatement callablestatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setDouble(int parametro, Double value, CallableStatement callableStatement) throws SQLException {
		if (Objects.isNull(value)) {
			callableStatement.setNull(parametro, Types.DECIMAL);
		} else {
			callableStatement.setDouble(parametro, value.doubleValue());
		}
	}

	/**
	 * Metódo para setar um Date no CallableStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo LocalDate
	 * @param callableStatement callablestatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setDate(int parametro, LocalDate value, CallableStatement callableStatement)
			throws SQLException {
		if (Objects.isNull(value)) {
			callableStatement.setNull(parametro, Types.DATE);
		} else {
			callableStatement.setDate(parametro, SqlUtils.localDateToDateUTC(value));
		}
	}

	/**
	 * Metódo para setar um Boolean no CallableStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo Boolean
	 * @param callableStatement callablestatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setBoolean(int parametro, Boolean value, CallableStatement callableStatement)
			throws SQLException {
		if (Objects.isNull(value)) {
			callableStatement.setNull(parametro, Types.TINYINT); // Boolean no mysql é Tinyint
		} else {
			callableStatement.setBoolean(parametro, value);
		}
	}

	/**
	 * Metódo para setar um Timestamp no CallableStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo Timestamp
	 * @param callableStatement callablestatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setTimestamp(int parametro, LocalDateTime value, CallableStatement callableStatement)
			throws SQLException {
		if (Objects.isNull(value)) {
			callableStatement.setNull(parametro, Types.TIMESTAMP);
		} else {
			callableStatement.setTimestamp(parametro, SqlUtils.localDateTimeToTimestampUTC(value));
		}
	}

}
