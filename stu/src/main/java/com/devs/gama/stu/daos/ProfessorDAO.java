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
import java.util.Objects;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.enums.ProceduresViewsTables;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.ProcessamentoProcedure;
import com.devs.gama.stu.utils.SqlUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class ProfessorDAO {

	@Inject
	private Application application;

	public void save(Professor professor) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {

			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_PROFESSOR.getValue(), 3, 1));

			int parametro = 1;

			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setString(parametro++, professor.getNome());
			callableStatement.setString(parametro++, professor.getEmail());
			callableStatement.setString(parametro++, hashSenha(professor.getSenha()));
			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
			ProcessamentoProcedure.closeCallableStatement(callableStatement);
		}
	}

	public void edit(Professor professor) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_EDITAR_PROFESSOR.getValue(), 4, 1));

			int parametro = 1;

			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, professor.getId());
			callableStatement.setString(parametro++, professor.getNome());
			callableStatement.setString(parametro++, professor.getEmail());
			callableStatement.setString(parametro++, hashSenha(professor.getSenha()));
			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
			ProcessamentoProcedure.closeCallableStatement(callableStatement);
		}
	}

	public void changePassword(Professor professor, String novaSenha) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_ALTERAR_SENHA_PROFESSOR.getValue(), 3, 1));

			int parametro = 1;

			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, professor.getId());
			callableStatement.setString(parametro++, hashSenha(professor.getSenha()));
			callableStatement.setString(parametro++, hashSenha(novaSenha));
			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
			ProcessamentoProcedure.closeCallableStatement(callableStatement);
		}
	}

	public void delete(Professor professor) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_DELETE_PROFESSOR.getValue(), 1, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, professor.getId());
			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
			ProcessamentoProcedure.closeCallableStatement(callableStatement);
		}

	}

	public List<Professor> findAll() throws SQLException {
		List<Professor> returnList = new ArrayList<>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(
					SqlUtils.montarViewTable(null, ProceduresViewsTables.VIEW_PROFESSOR.getValue(), null));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}

			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);
		}
		return returnList;
	}

	public List<Professor> findAllFiltered(Professor professor) throws SQLException {
		List<Professor> returnList = findAll();
		returnList.removeIf(p -> !p.equals(professor));
		return returnList;
	}

	public Professor findById(int id) throws SQLException, EntityNotFoundException {
		Professor professor = null;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(null,
					ProceduresViewsTables.VIEW_PROFESSOR.getValue(), new String[] { "id" }));
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				professor = fetch(resultSet);
			}
			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);
			if (Objects.isNull(professor)) {
				throw new EntityNotFoundException("Professor não encontrado");
			}

		}
		return professor;
	}

	public Professor fetch(ResultSet res) throws SQLException {
		Professor professor = new Professor();

		professor.setId(res.getInt("id"));
		professor.setNome(res.getString("nome"));
		professor.setEmail(res.getString("email"));
		professor.setSenha(res.getString("senha"));

		return professor;
	}

	public Professor validateLogin(String email, String senha) throws SQLException, EntityNotFoundException {
		Professor professor = null;
		try (Connection connection = application.getDataSource().getConnection()) {

			PreparedStatement preparedStatement = connection
					.prepareStatement(SqlUtils.montarViewTable("id, nome, email, senha",
							ProceduresViewsTables.VIEW_PROFESSOR.getValue(), new String[] { "email", "senha" }));

			int parametro = 1;
			preparedStatement.setString(parametro++, email);
			preparedStatement.setString(parametro++, hashSenha(senha));

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				professor = new Professor();
				professor.setId(resultSet.getInt("id"));
				professor.setNome(resultSet.getString("nome"));
				professor.setEmail(resultSet.getString("email"));
			}
			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);
			if (Objects.isNull(professor)) {
				throw new EntityNotFoundException("Professor não encontrado");
			}

		}
		return professor;
	}

	private String hashSenha(String senha) {
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
