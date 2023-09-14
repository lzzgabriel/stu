package utils;

public class StringUtils {

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

	/**
	 * Método para verificar se uma string esta nula ou em branco
	 * 
	 * @param text String texto a ser validada
	 * @return Boolean
	 */
	public static Boolean isNullOrEmpty(String text) {
		return text == null || text.isBlank() || text.isEmpty();
	}
}
