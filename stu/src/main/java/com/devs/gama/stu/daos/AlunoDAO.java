package com.devs.gama.stu.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.devs.gama.stu.app.App;
import com.devs.gama.stu.entities.Aluno;

public class AlunoDAO implements DAO<Aluno> {
	private String tableName = "aluno";

	@Override
	public void save(Aluno t) throws SQLException {

	}

	@Override
	public void edit(Aluno t) throws SQLException {
		try (Connection conn = App.getDataSource().getConnection()) {
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
				// nenhum registro atualizado
			}
		}
	}

	@Override
	public void delete(Aluno t) throws SQLException {

		try (Connection conn = App.getDataSource().getConnection()) {
			String sql = "DELETE FROM " + tableName + " WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, t.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// registro excluido
			} else {
				// nenhum registro excluido
			}
		}

	}

	@Override
	public List<Aluno> findAll() throws SQLException {
		List<Aluno> returnList = new ArrayList<>();
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
	public List<Aluno> findAllFiltered(Aluno t) throws SQLException {
		List<Aluno> returnList = findAll();
		returnList.removeIf(p -> !p.equals(t));
		return returnList;
	}

	@Override
	public Aluno findById(int id) throws SQLException {
		Aluno aluno = null;
		String sql = "SELECT * FROM" + tableName + " WHERE id = ?";
		try (Connection conn = App.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				aluno = fetch(rs);
			}
		}
		return aluno;
	}

	@Override
	public Aluno fetch(ResultSet res) throws SQLException {
		Aluno aluno = new Aluno();
		aluno.setId(res.getInt("id"));
		aluno.setNome(res.getString("nome"));
		aluno.setEmail(res.getString("email"));
		aluno.setCelular(res.getString("celular"));
		return aluno;
	}

}
