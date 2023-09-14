package com.devs.gama.stu.utils;

public class SqlUtils {

	/**
	 * Método para montar a estrutura de uma procedure
	 * 
	 * @param nomeProcedure     String Texto que será colocado no nome da procedure
	 * @param parametrosEntrada Total de parametros de entrada
	 * @param parametrosRetorno Total de parametros de retorno
	 * @return String o texto repetido
	 */
	public static String montarProcedure(String nomeProcedure, int parametrosEntrada, int parametrosRetorno) {
		StringBuilder procedure = new StringBuilder("{call " + nomeProcedure + "(");
		procedure.append(parametrosEntrada > 0 ? "?" + StringUtils.stringReplicate(", ?", parametrosEntrada - 1) : "");
		procedure
				.append(parametrosRetorno > 0 ? ", ?" + StringUtils.stringReplicate(", ?", parametrosRetorno - 1) : "");
		procedure.append(")}");
		return procedure.toString();
	}

}
