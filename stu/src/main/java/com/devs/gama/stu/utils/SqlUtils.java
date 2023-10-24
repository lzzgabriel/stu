package com.devs.gama.stu.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class SqlUtils {

	@Deprecated
	/**
	 * Método para montar a estrutura de uma procedure
	 * 
	 * @param nomeProcedure     String Texto que será colocado no nome da procedure
	 * @param parametrosEntrada Total de parametros de entrada
	 * @param parametrosRetorno Total de parametros de retorno
	 * @return String o texto em String
	 */
	public static String montarProcedure(String nomeProcedure, int parametrosEntrada, int parametrosRetorno) {
		StringBuilder procedure = new StringBuilder("{call " + nomeProcedure + "(");
		procedure.append(parametrosRetorno > 0 ? "?" + StringUtils.stringReplicate(", ?", parametrosRetorno - 1) : "");
		procedure.append(parametrosRetorno > 0 ? ", " : "");
		procedure.append(parametrosEntrada > 0 ? "?" + StringUtils.stringReplicate(", ?", parametrosEntrada - 1) : "");
		procedure.append(")}");
		return procedure.toString();
	}

	/**
	 * Método para montar a estrutura de uma função
	 * 
	 * @param nomeFuncao        String Texto que será colocado no nome da funcao
	 * @param parametrosEntrada Total de parametros de entrada
	 * @return String o texto em String
	 */
	public static String montarFuncao(String nomeFuncao, int parametrosEntrada) {
		StringBuilder funcao = new StringBuilder("select * FROM " + nomeFuncao + "(");
		funcao.append(parametrosEntrada > 0 ? "?" + StringUtils.stringReplicate(", ?", parametrosEntrada - 1) : "");
		funcao.append(")");
		return funcao.toString();
	}

	/**
	 * Método para montar a estrutura de uma query com páginação
	 * 
	 * @param campos          Campos que serão buscados no select
	 * @param nomeView        String Texto que será colocado no nome da view/tabela
	 * @param camposWhere     Campos que serão utilizados na chave where
	 * @param posicao         Posicao em que a paginacao se encontra
	 * @param padraoPaginacao Padrao de paginacao a ser seguido (10 em 10, 20 em
	 *                        20...)
	 * @return String o select em String
	 */
	public static String montarPaginacao(String campos, String nomeViewTable, String camposWhere, int posicao,
			int padraoPaginacao) {
		StringBuilder paginacao = new StringBuilder(
				"select " + (Objects.nonNull(campos) && !campos.isEmpty() ? campos : "*") + " from " + nomeViewTable);
		paginacao.append(Objects.nonNull(camposWhere) && !camposWhere.isEmpty() ? (" where " + camposWhere) : "");
		paginacao.append(" LIMIT " + padraoPaginacao + " OFFSET " + posicao);
		return paginacao.toString();
	}

	/**
	 * Método para montar a estrutura de uma query
	 * 
	 * @param campos      Campos que serão buscados no select
	 * @param nomeView    String Texto que será colocado no nome da view/tabela
	 * @param camposWhere Campos que serão utilizados na chave where
	 * 
	 * @return String o select em String
	 */
	public static String montarViewTable(String campos, String nomeViewTable, String[] camposWhere) {
		List<String> listWhere = StringUtils.arrayToList(camposWhere);
		StringBuilder viewTable = new StringBuilder(
				"select " + (Objects.nonNull(campos) && !campos.isEmpty() ? campos : "*") + " from " + nomeViewTable);
		StringBuilder whereClause = new StringBuilder();
		if (Objects.nonNull(listWhere) && !listWhere.isEmpty()) {
			whereClause.append(" WHERE ");
			for (int i = 0; i < listWhere.size(); i++) {
				whereClause.append((i > 0 ? " AND " : "") + listWhere.get(i) + " = ?");
			}
		}
		viewTable.append(whereClause.toString());
		return viewTable.toString();
	}

	/**
	 * Metódo para transformar um LocalDate no TimeZone local da máquina para o
	 * TimeZone UTC e em seguida transformar em um java.sql.Date
	 * 
	 * @param date LocalDate que será transformado
	 * @return Date da API java.sql.Date
	 */
	public static Date localDateToDateUTC(LocalDate date) {
		SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");

		formatoData.setTimeZone(TimeZone.getTimeZone("UTC"));

		String dataUTC = formatoData.format(Date.valueOf(date));

		return Date.valueOf(dataUTC);
	}

	/**
	 * Metódo para transformar um LocalDateTime no TimeZone local da máquina para um
	 * LocalDateTime no TimeZone UTC
	 * 
	 * @param date LocalDateTime que será transformado
	 * @return Timestamp da API java.sql.Timestamp
	 */
	public static LocalDateTime localDateTimeToLocalDateTimeUTC(LocalDateTime date) {
		return date.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
	}

	/**
	 * Metódo para transformar um Date vindo do banco no TimeZone UTC para o
	 * TimeZone da máquina e em seguida transformar em um LocalDate
	 * 
	 * @param date java.sql.Date que será transformado
	 * @return LocalDate da API java.time.LocalDate
	 */
	public static LocalDate dateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("UTC")).toLocalDate();
	}

	/**
	 * Metódo para transformar um Timestamp vindo do banco no TimeZone UTC para o
	 * TimeZone da máquina e em seguida transformar em um LocalDateTime
	 * 
	 * @param timestamp java.sql.Timestamp que será transformado
	 * @return LocalDateTime da API java.time.LocalDateTime
	 */
	public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
		return Objects.isNull(timestamp) ? null : LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("UTC"));
	}
	
	public static ZonedDateTime timestampToZonedDateTime(Timestamp timestamp) {
		return Objects.isNull(timestamp) ? null : ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("UTC"));
	}

}
