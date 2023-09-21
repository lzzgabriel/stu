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

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.enums.ProceduresViewsTables;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.ProcessamentoProcedure;
import com.devs.gama.stu.utils.SqlUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Model;
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
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_PROFESSOR.getValue(), 4, 1));

			int parametro = 1;

			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setNull(parametro++, Types.INTEGER); // Deve ser passado null para registrar
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
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_PROFESSOR.getValue(), 4, 1));

			int parametro = 1;

			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, professor.getId()); // Deve ser passado o id para atualizar
			callableStatement.setString(parametro++, professor.getNome());
			callableStatement.setString(parametro++, professor.getEmail());
			callableStatement.setString(parametro++, professor.getSenha());
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
		String sql = "SELECT * FROM " + ProceduresViewsTables.VIEW_PROFESSOR.getValue();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}
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
		String sql = "SELECT * FROM " + ProceduresViewsTables.VIEW_PROFESSOR.getValue() + " WHERE id = ?";
		try (Connection conn = application.getDataSource().getConnection()) {
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

	public Professor fetch(ResultSet res) throws SQLException {
		Professor professor = new Professor();

		professor.setId(res.getInt("id"));
		professor.setNome(res.getString("nome"));
		professor.setEmail(res.getString("email"));
		professor.setSenha(res.getString("senha"));

		return professor;
	}

	public Professor validateLogin(String email, String senha) throws SQLException, EntityNotFoundException {
		try (Connection connection = application.getDataSource().getConnection()) {

			String sql = "SELECT id, nome, email, senha FROM " + ProceduresViewsTables.VIEW_PROFESSOR.getValue()
					+ " WHERE email = ? AND senha = ? ";

			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			int parametro = 1;
			preparedStatement.setString(parametro++, email);
			preparedStatement.setString(parametro++, hashSenha(senha));

			ResultSet res = preparedStatement.executeQuery();

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
