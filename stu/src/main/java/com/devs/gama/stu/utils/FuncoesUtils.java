package com.devs.gama.stu.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class FuncoesUtils {

	/**
	 * Metódo para setar uma String no PreparedStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo String
	 * @param preparedStatement preparedStatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setString(int parametro, String value, PreparedStatement preparedStatement) throws SQLException {
		if (Objects.isNull(value) || value.isEmpty()) {
			preparedStatement.setNull(parametro, Types.VARCHAR);
		} else {
			preparedStatement.setString(parametro, value);
		}
	}

	/**
	 * Metódo para setar um Integer no PreparedStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo Integer
	 * @param preparedStatement preparedStatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setInt(int parametro, Integer value, PreparedStatement preparedStatement) throws SQLException {
		if (Objects.isNull(value)) {
			preparedStatement.setNull(parametro, Types.INTEGER);
		} else {
			preparedStatement.setInt(parametro, value.intValue());
		}
	}

	/**
	 * Metódo para setar um Double no PreparedStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo Double
	 * @param preparedStatement preparedStatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setDouble(int parametro, Double value, PreparedStatement preparedStatement) throws SQLException {
		if (Objects.isNull(value)) {
			preparedStatement.setNull(parametro, Types.DECIMAL);
		} else {
			preparedStatement.setDouble(parametro, value.doubleValue());
		}
	}

	/**
	 * Metódo para setar um Date no PreparedStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo LocalDate
	 * @param preparedStatement preparedStatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setDate(int parametro, LocalDate value, PreparedStatement preparedStatement)
			throws SQLException {
		if (Objects.isNull(value)) {
			preparedStatement.setNull(parametro, Types.DATE);
		} else {
			preparedStatement.setDate(parametro, SqlUtils.localDateToDateUTC(value));
		}
	}

	/**
	 * Metódo para setar um Boolean no PreparedStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo Boolean
	 * @param preparedStatement preparedStatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setBoolean(int parametro, Boolean value, PreparedStatement preparedStatement)
			throws SQLException {
		if (Objects.isNull(value)) {
			preparedStatement.setNull(parametro, Types.BOOLEAN);
		} else {
			preparedStatement.setBoolean(parametro, value);
		}
	}

	/**
	 * Metódo para setar um Timestamp no PreparedStatement
	 * 
	 * @param parametro         posição do parâmetro
	 * @param value             valor no tipo Timestamp
	 * @param preparedStatement preparedStatement que será passado o parametro
	 * @throws SQLException
	 */
	public static void setTimestamp(int parametro, LocalDateTime value, PreparedStatement preparedStatement)
			throws SQLException {
		if (Objects.isNull(value)) {
			preparedStatement.setNull(parametro, Types.TIMESTAMP);
		} else {
			preparedStatement.setTimestamp(parametro, SqlUtils.localDateTimeToTimestampUTC(value));
		}
	}

}
