package com.devs.gama.stu.daos;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.devs.gama.stu.app.App;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.enums.ProceduresViewsTables;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.SqlUtils;

public class ProfessorDAO {

	public static void save(Professor professor) throws SQLException {
		try (Connection conn = App.getInstance().getDataSource().getConnection()) {

			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_PROFESSOR.getValue(), 4, 1));

			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setNull(parametro++, Types.INTEGER); // Deve ser passado null para registrar
			callableStatement.setString(parametro++, professor.getNome());
			callableStatement.setString(parametro++, professor.getEmail());
			callableStatement.setString(parametro++, hashSenha(professor.getSenha()));

			int linhasAfetadas = callableStatement.executeUpdate();

			if (linhasAfetadas > 0) {
				int novoId = callableStatement.getInt(1);
				if (novoId > 0) {
					System.out.println(novoId);
					// registro inserido
				}
			}
		}
	}

	public static void edit(Professor professor) throws SQLException {
		try (Connection conn = App.getInstance().getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_PROFESSOR.getValue(), 4, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, professor.getId()); // Deve ser passado o id para atualizar
			callableStatement.setString(parametro++, professor.getNome());
			callableStatement.setString(parametro++, professor.getEmail());
			callableStatement.setString(parametro++, professor.getSenha());
			int linhasAfetadas = callableStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				int idRegistroAtualizado = callableStatement.getInt(1);
				if (idRegistroAtualizado > 0) {
					System.out.println(idRegistroAtualizado);
					// registro atualizado
				}
			}
		}
	}

	public static void delete(Professor professor) throws SQLException, EntityNotFoundException {
		try (Connection conn = App.getInstance().getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_DELETE_PROFESSOR.getValue(), 1, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, professor.getId());
			int linhasAfetadas = callableStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				int result = callableStatement.getInt(1);
				if (result > 0) { // 1 -> deu certo
					System.out.println(result);
					// excluido
				}
			} else {
				throw new EntityNotFoundException("Professor não encotrado: nenhum dado deletado ");
			}
		}

	}

	public static List<Professor> findAll() throws SQLException {
		List<Professor> returnList = new ArrayList<>();
		String sql = "SELECT * FROM " + ProceduresViewsTables.VIEW_PROFESSOR.getValue();
		try (Connection conn = App.getInstance().getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}
		}
		return returnList;
	}

	public static List<Professor> findAllFiltered(Professor professor) throws SQLException {
		List<Professor> returnList = findAll();
		returnList.removeIf(p -> !p.equals(professor));
		return returnList;
	}

	public static Professor findById(int id) throws SQLException, EntityNotFoundException {
		Professor professor = null;
		String sql = "SELECT * FROM" + ProceduresViewsTables.VIEW_PROFESSOR.getValue() + " WHERE id = ?";
		try (Connection conn = App.getInstance().getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				professor = fetch(rs);
			} else {
				throw new EntityNotFoundException("Professor não encontrado");
			}
		}
		return professor;
	}

	public static Professor fetch(ResultSet res) throws SQLException {
		Professor professor = new Professor();

		professor.setId(res.getInt("id"));
		professor.setNome(res.getString("nome"));
		professor.setEmail(res.getString("email"));
		professor.setSenha(res.getString("senha"));

		return professor;
	}

	public static Professor validateLogin(String email, String senha) throws SQLException, EntityNotFoundException {
		try (Connection connection = App.getInstance().getDataSource().getConnection()) {

			String sql = "SELECT id, nome, email, senha FROM " + ProceduresViewsTables.VIEW_PROFESSOR.getValue()
					+ " WHERE email = ? AND senha = ? ";

			// TODO hash aqui ou no bean?

			PreparedStatement statement = connection.prepareStatement(sql);

			int parametro = 1;
			statement.setString(parametro++, email);
			statement.setString(parametro++, hashSenha(senha));

			ResultSet res = statement.executeQuery();

			if (res.next()) {
				Professor professor = new Professor();

				professor.setId(res.getInt("id"));
				professor.setNome(res.getString("nome"));
				professor.setEmail(res.getString("email"));

				return professor;
			} else {
				throw new EntityNotFoundException("Professor não encontrado");
			}

		} catch (SQLException e) {
			throw e;
		}
	}

	private static String hashSenha(String senha) {
		try {
			MessageDigest md = MessageDigest.getInstance("sha-256");
			md.update(senha.getBytes(StandardCharsets.UTF_8));
			byte[] digest = md.digest();

			return String.format("%064x", new BigInteger(1, digest));
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
