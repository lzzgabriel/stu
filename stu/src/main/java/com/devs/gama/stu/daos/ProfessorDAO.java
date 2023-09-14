package com.devs.gama.stu.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.devs.gama.stu.app.App;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.enums.ProceduresViews;
import com.devs.gama.stu.utils.SqlUtils;

import jakarta.inject.Inject;

public class ProfessorDAO implements DAO<Professor> {
	// Falta trocar locais de consulta, tirar a tabela e adicionar a view e
	// metodos de cadastro de aluno
	private String tableName = "professor";
	@Inject
	private AlunoDAO alunoDao;

	@Override
	public void save(Professor professor) throws SQLException {
		//TODO vamos deixar esse check?
		if (findById(professor.getId()) == null) {
			try (Connection conn = App.getDataSource().getConnection()) {
				CallableStatement callableStatement = conn.prepareCall(
						SqlUtils.montarProcedure(ProceduresViews.PROCEDURE_CADASTRAR_PROFESSOR.getValue(), 3, 0));
				int parametro = 1;
				callableStatement.setString(parametro++, professor.getNome());
				callableStatement.setString(parametro++, professor.getEmail());
				callableStatement.setString(parametro++, professor.getSenha());
				callableStatement.execute();
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
		} else {
			edit(professor);
		}
	}

	@Override
	public void edit(Professor professor) throws SQLException {
		try (Connection conn = App.getDataSource().getConnection()) {
			String sql = "UPDATE " + tableName + " SET nome = ?, email = ?, senha = ? WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setString(parametro++, professor.getNome());
			preparedStatement.setString(parametro++, professor.getEmail());
			preparedStatement.setString(parametro++, professor.getSenha());
			preparedStatement.setInt(parametro++, professor.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// registro atualizado
			} else {
				// nenhum registro atualizado
			}
		}
	}

	@Override
	public void delete(Professor professor) throws SQLException {

		try (Connection conn = App.getDataSource().getConnection()) {
			String sql = "DELETE FROM " + tableName + " WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, professor.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// registro excluido
			} else {
				// nenhum registro excluido
			}
		}

	}

	@Override
	public List<Professor> findAll() throws SQLException {
		List<Professor> returnList = new ArrayList<>();
		String sql = "SELECT * FROM " + tableName;
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
		String sql = "SELECT * FROM" + tableName + " WHERE id = ?";
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
			CallableStatement callableStatement = conn
					.prepareCall(SqlUtils.montarProcedure(ProceduresViews.PROCEDURE_CADASTRAR_ALUNO.getValue(), 6, 0));
			int parametro = 1;
			callableStatement.setString(parametro++, aluno.getNome());
			callableStatement.setString(parametro++, aluno.getEmail());
			callableStatement.setString(parametro++, aluno.getCelular());
			callableStatement.setInt(parametro++, professor.getId());
			callableStatement.setDouble(parametro++, valorCobrado);
			callableStatement.setDate(parametro++, Date.valueOf(calendar.getTime().toString()));

			callableStatement.execute();
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
