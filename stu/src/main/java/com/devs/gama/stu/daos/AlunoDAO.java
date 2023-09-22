package com.devs.gama.stu.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Aluno;
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
public class AlunoDAO {

	@Inject
	private Application application;

	public void save(Professor professor, Aluno aluno) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_ALUNO.getValue(), 4, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setString(parametro++, aluno.getNome());
			callableStatement.setString(parametro++, aluno.getEmail());
			callableStatement.setString(parametro++, aluno.getCelular().substring(0, 10)); // -> limitado no banco para
			callableStatement.setInt(parametro++, professor.getId());

			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
		}
	}

	public void edit(Professor professor, Aluno aluno) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_EDITAR_ALUNO.getValue(), 4, 1));

			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, aluno.getId());
			callableStatement.setString(parametro++, aluno.getNome());
			callableStatement.setString(parametro++, aluno.getEmail());
			callableStatement.setString(parametro++, aluno.getCelular());

			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
		}
	}

	public void delete(Aluno aluno) throws SQLException {

		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(SqlUtils.montarProcedure("delete_aluno", 1, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, aluno.getId());
			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);

		}

	}

	public List<Aluno> findAll() throws SQLException {
		List<Aluno> returnList = new ArrayList<>();
		String sql = "SELECT * FROM " + ProceduresViewsTables.VIEW_ALUNO.getValue();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}

			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);
		}
		return returnList;
	}

	public List<Aluno> findAllFiltered(Aluno aluno) throws SQLException {
		List<Aluno> returnList = findAll();
		returnList.removeIf(p -> !p.equals(aluno));
		return returnList;
	}

	public int findCount() throws SQLException {
		int totalRegistros = 0;
		String sql = "SELECT COUNT(id) as totalRegistros FROM " + ProceduresViewsTables.VIEW_ALUNO.getValue();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				totalRegistros = resultSet.getInt("totalRegistros");
			}

			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);
		}
		return totalRegistros;
	}

	public List<Aluno> pagination(int pagina, int padraoPaginacao) throws SQLException {
		List<Aluno> listaRetorno = new ArrayList<Aluno>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(
					SqlUtils.montarPaginacao(ProceduresViewsTables.VIEW_ALUNO.getValue(), pagina, padraoPaginacao));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				listaRetorno.add(fetch(resultSet));
			}
			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);
		}
		return listaRetorno;
	}

	public Aluno findById(int id) throws SQLException, EntityNotFoundException {
		Aluno aluno = null;
		String sql = "SELECT * FROM" + ProceduresViewsTables.VIEW_ALUNO + " WHERE id = ?";
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				aluno = fetch(resultSet);
			}
			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);
			if (aluno == null) {
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

}
