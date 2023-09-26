package com.devs.gama.stu.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.TimeZone;

public class SqlUtils {

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
	 * Método para montar a estrutura de uma páginação
	 * 
	 * @param campos          Campos que serão buscados no select
	 * @param nomeView        String Texto que será colocado no nome da procedure
	 * @param camposWhere     Campos que serão utilizados na chave where
	 * @param pagina          Pagina em que a paginacao se encontra
	 * @param padraoPaginacao Padrao de paginacao a ser seguido (10 em 10, 20 em
	 *                        20...)
	 * @return String o texto em String
	 */
	public static String montarPaginacao(String campos, String nomeViewTable, String camposWhere, int pagina,
			int padraoPaginacao) {
		StringBuilder paginacao = new StringBuilder(
				"select " + (campos != null && !campos.isEmpty() ? campos : "*") + " from " + nomeViewTable);
		paginacao.append(camposWhere != null && !camposWhere.isEmpty() ? (" where " + camposWhere) : "");
		paginacao.append(" LIMIT " + pagina + ", " + padraoPaginacao);
		return paginacao.toString();
	}

	public static Date transformarDataUTC(LocalDate date) {
		SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");

		formatoData.setTimeZone(TimeZone.getTimeZone("UTC"));

		String horaUTC = formatoData.format(date);

		return Date.valueOf(LocalDate.parse(horaUTC));
	}

}
