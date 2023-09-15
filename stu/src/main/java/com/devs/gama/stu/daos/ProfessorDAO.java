package com.devs.gama.stu.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.devs.gama.stu.app.App;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.enums.ProceduresViewsTables;
import com.devs.gama.stu.utils.SqlUtils;

import jakarta.inject.Inject;

public class ProfessorDAO implements DAO<Professor> {
	// Falta trocar locais de consulta, tirar a tabela e adicionar a view e
	// metodos de cadastro de aluno
	@Inject
	private AlunoDAO alunoDao;

	@Override
	public void save(Professor professor) throws SQLException {
		try (Connection conn = App.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_PROFESSOR.getValue(), 4, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setNull(parametro++, Types.INTEGER); // Deve ser passado null para registrar
			callableStatement.setString(parametro++, professor.getNome());
			callableStatement.setString(parametro++, professor.getEmail());
			callableStatement.setString(parametro++, professor.getSenha());
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

	@Override
	public void edit(Professor professor) throws SQLException {
		try (Connection conn = App.getDataSource().getConnection()) {
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

	@Override
	public void delete(Professor professor) throws SQLException {

		try (Connection conn = App.getDataSource().getConnection()) {
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
				System.out.println("Falha exclusão");
			}
		}

	}

	@Override
	public List<Professor> findAll() throws SQLException {
		List<Professor> returnList = new ArrayList<>();
		String sql = "SELECT * FROM " + ProceduresViewsTables.VIEW_PROFESSOR.getValue();
		try (Connection conn = App.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}
		}
		return returnList;
	}

	@Override
	public List<Professor> findAllFiltered(Professor professor) throws SQLException {
		List<Professor> returnList = findAll();
		returnList.removeIf(p -> !p.equals(professor));
		return returnList;
	}

	@Override
	public Professor findById(int id) throws SQLException {
		Professor professor = null;
		String sql = "SELECT * FROM" + ProceduresViewsTables.VIEW_PROFESSOR.getValue() + " WHERE id = ?";
		try (Connection conn = App.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				professor = fetch(rs);
			}
		}
		return professor;
	}

	@Override
	public Professor fetch(ResultSet res) throws SQLException {
		Professor professor = new Professor();
		professor.setId(res.getInt("id"));
		professor.setNome(res.getString("nome"));
		professor.setEmail(res.getString("email"));
		professor.setSenha(res.getString("senha"));
		return professor;
	}

	public void saveNewAluno(Professor professor, Aluno aluno, Double valorCobrado) throws SQLException {
		try (Connection conn = App.getDataSource().getConnection()) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, 1);
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_ALUNO.getValue(), 7, 0));
			int parametro = 1;
			callableStatement.setString(parametro++, aluno.getNome());
			callableStatement.setString(parametro++, aluno.getEmail());
			callableStatement.setString(parametro++, aluno.getCelular());
			callableStatement.setInt(parametro++, professor.getId());
			callableStatement.setDouble(parametro++, valorCobrado);
			callableStatement.setDate(parametro++, Date.valueOf(calendar.getTime().toString()));
			callableStatement.setDate(parametro++, new Date(new java.util.Date().getTime())); // verificar uso
			int linhasAfetadas = callableStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				ResultSet chavesGeradas = callableStatement.getGeneratedKeys();
				if (chavesGeradas.next()) {
					int novoId = chavesGeradas.getInt(1);
					System.out.println(novoId);
					// registro inserido
				} else {
					// nenhum registro inserido
				}
			}
		}
	}

	public void saveNewAlunoFree(Professor professor, Aluno aluno) throws SQLException {
		try (Connection conn = App.getDataSource().getConnection()) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, 1);
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_ALUNO_FREE.getValue(), 4, 0));
			int parametro = 1;
			callableStatement.setString(parametro++, aluno.getNome());
			callableStatement.setString(parametro++, aluno.getEmail());
			callableStatement.setString(parametro++, aluno.getCelular());
			callableStatement.setInt(parametro++, professor.getId());
			callableStatement.setDate(parametro++, Date.valueOf(calendar.getTime().toString()));
			int linhasAfetadas = callableStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				ResultSet chavesGeradas = callableStatement.getGeneratedKeys();
				if (chavesGeradas.next()) {
					int novoId = chavesGeradas.getInt(1);
					System.out.println(novoId);
					// registro inserido
				} else {
					// nenhum registro inserido
				}
			}
		}
	}

}
