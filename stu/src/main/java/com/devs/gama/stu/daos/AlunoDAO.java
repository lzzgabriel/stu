package com.devs.gama.stu.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.enums.ProceduresViewsTables;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.SqlUtils;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

@Model
public class AlunoDAO {

	private static String tableName = "aluno";
	
	@Inject
	private Application application;

	public void edit(Aluno t) throws SQLException, EntityNotFoundException {
		try (Connection conn = application.getDataSource().getConnection()) {

			// TODO cadastro, atualização e delete serão por procedures

			String sql = "UPDATE " + tableName + " SET nome = ?, email = ?, senha = ? WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setString(parametro++, t.getNome());
			preparedStatement.setString(parametro++, t.getEmail());
			preparedStatement.setString(parametro++, t.getCelular());
			preparedStatement.setInt(parametro++, t.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// registro atualizado
			} else {
				throw new EntityNotFoundException("Aluno não encontrado: nenhum dado atualizado na tabela");
			}
		}
	}

	public void delete(Aluno t) throws SQLException, EntityNotFoundException {

		try (Connection conn = application.getDataSource().getConnection()) {
			String sql = "DELETE FROM " + tableName + " WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, t.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// registro excluido
			} else {
				throw new EntityNotFoundException("Aluno não encontrado: nenhum dado deletado");
			}
		}

	}

	public List<Aluno> findAll() throws SQLException {
		List<Aluno> returnList = new ArrayList<>();
		String sql = "SELECT * FROM " + tableName;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}
		}
		return returnList;
	}

	public List<Aluno> findAllFiltered(Aluno t) throws SQLException {
		List<Aluno> returnList = findAll();
		returnList.removeIf(p -> !p.equals(t));
		return returnList;
	}

	public Aluno findById(int id) throws SQLException, EntityNotFoundException {
		Aluno aluno = null;
		String sql = "SELECT * FROM" + tableName + " WHERE id = ?";
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				aluno = fetch(rs);
			} else {
				throw new EntityNotFoundException("Nenhum aluno encontrado");
			}
		}
		return aluno;
	}

	public Aluno fetch(ResultSet res) throws SQLException {
		Aluno aluno = new Aluno();

		aluno.setId(res.getInt("id"));
		aluno.setNome(res.getString("nome"));
		aluno.setEmail(res.getString("email"));
		aluno.setCelular(res.getString("celular"));

		return aluno;
	}

	public void save(Professor professor, Aluno aluno, Double valorCobrado) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
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
			callableStatement.setDate(parametro++, Date.valueOf(LocalDate.now())); // verificar uso
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
