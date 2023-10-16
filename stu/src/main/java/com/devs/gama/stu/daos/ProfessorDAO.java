package com.devs.gama.stu.daos;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.enums.FuncoesViewsTables;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.FuncoesUtils;
import com.devs.gama.stu.utils.ProcessamentoFuncoes;
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

			PreparedStatement preparedStatement = conn
					.prepareCall(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_CADASTRAR_PROFESSOR.getValue(), 3));

			int parametro = 1;

			FuncoesUtils.setString(parametro++, professor.getNome(), preparedStatement);
			FuncoesUtils.setString(parametro++, professor.getEmail(), preparedStatement);
			FuncoesUtils.setString(parametro++, hashSenha(professor.getSenha()), preparedStatement);
			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public void edit(Professor professor) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareCall(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_EDITAR_PROFESSOR.getValue(), 3));

			int parametro = 1;

			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setString(parametro++, professor.getNome(), preparedStatement);
			FuncoesUtils.setString(parametro++, professor.getEmail(), preparedStatement);
			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public void changePassword(Professor professor, String novaSenha) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_ALTERAR_SENHA_PROFESSOR.getValue(), 3));

			int parametro = 1;

			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setString(parametro++, hashSenha(professor.getSenha()), preparedStatement);
			FuncoesUtils.setString(parametro++, hashSenha(novaSenha), preparedStatement);
			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public List<Professor> findAll() throws SQLException {
		List<Professor> returnList = new ArrayList<>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_PROFESSOR.getValue(), new String[] { "ativo" }));

			int parametro = 1;
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return null;
	}

	public Professor findById(int id) throws SQLException, EntityNotFoundException {
		Professor professor = null;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_PROFESSOR.getValue(), new String[] { "id", "ativo" }));
			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				professor = fetch(resultSet);
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
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

		return professor;
	}

	public Professor validateLogin(String email, String senha) throws SQLException, EntityNotFoundException {
		Professor professor = null;
		try (Connection connection = application.getDataSource().getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_PROFESSOR.getValue(), new String[] { "email", "senha" }));

			int parametro = 1;
			FuncoesUtils.setString(parametro++, email, preparedStatement);
			FuncoesUtils.setString(parametro++, hashSenha(senha), preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				professor = new Professor();
				professor.setId(resultSet.getInt("id"));
				professor.setNome(resultSet.getString("nome"));
				professor.setEmail(resultSet.getString("email"));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
			if (Objects.isNull(professor)) {
				throw new EntityNotFoundException("Professor não encontrado");
			}

		}
		return professor;
	}

	public void inativarProfessor(int id) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_INATIVAR_PROFESSOR.getValue(), 1));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public void ativarProfessor(int id) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_ATIVAR_PROFESSOR.getValue(), 1));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
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
