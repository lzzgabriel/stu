package utils;

public class SqlUtils {

	public static String montarProcedure(String nomeProcedure, int parametrosEntrada, int parametrosSaida) {
		StringBuilder procedure = new StringBuilder("{call " + nomeProcedure + "(");
		for (int i = 0; i < parametrosEntrada + parametrosSaida; i++) {
			procedure.append("?, ");
		}
		procedure.append(")}");
		return procedure.toString();
	}

}
