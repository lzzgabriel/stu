package com.devs.gama.stu.utils;

import java.util.Objects;

public final class StringUtils {

	// No no no
	private StringUtils() {
		
	}

	/**
	 * Método para repetir a String de acordo com a quantidade
	 * 
	 * @param texto      String Texto a ser repetido
	 * @param quantidade int Quantidade de vezes a repetir o texto
	 * @return String o texto repetido
	 */
	public static String stringReplicate(String text, int quantidade) {
		StringBuffer sb = new StringBuffer("");
		try {
			for (int i = 0; i < quantidade; i++) {
				sb.append(text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static String formattedTelephone(String s) {
		if (!containsOnlyDigits(s))
			throw new IllegalArgumentException();
		if (s.length() == 10) {
			return "(" + s.substring(0, 2) + ")" + s.substring(2, 7) + "-" + s.substring(6);
		} else if (s.length() == 11) {
			return "(" + s.substring(0, 2) + ")" + s.substring(2, 7) + "-" + s.substring(6);
		} else
			throw new IllegalArgumentException();
	}

	/**
	 * Método para verificar se uma string esta nula ou em branco
	 * 
	 * @param text String texto a ser validada
	 * @return Boolean
	 */
	public static boolean isNullOrEmpty(String text) {
		return Objects.isNull(text) || text.isBlank() || text.isEmpty();
	}

	/**
	 * Retorna {@code true} se a {@code String} inserida tiver somente números, e
	 * {@code false} se não.
	 * 
	 * @param text
	 * @return um booleano dizendo se há somente números ou não
	 */
	public static boolean containsOnlyDigits(String text) {
		if (Objects.isNull(text))
			return false;
		if (text.isEmpty() || text.isBlank())
			return false;
		try {
			Long.valueOf(text);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
