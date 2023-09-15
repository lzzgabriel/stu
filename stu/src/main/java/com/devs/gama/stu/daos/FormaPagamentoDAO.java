package com.devs.gama.stu.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.devs.gama.stu.app.App;
import com.devs.gama.stu.entities.FormaPagamento;
import com.devs.gama.stu.enums.ProceduresViewsTables;

public class FormaPagamentoDAO implements DAO<FormaPagamento> {

	@Override
	public void save(FormaPagamento formaPagamento) throws SQLException {
		// TODO procedure ou insert direto na tabela
	}

	@Override
	public void edit(FormaPagamento formaPagamento) throws SQLException {
		try (Connection conn = App.getDataSource().getConnection()) {
			String sql = "UPDATE " + ProceduresViewsTables.TABELA_FORMA_PAGAMENTO.getValue()
					+ " SET descricao = ? WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setString(parametro++, formaPagamento.getDescricao());
			preparedStatement.setInt(parametro++, formaPagamento.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// registro atualizado
			} else {
				// nenhum registro atualizado
			}
		}
	}

	@Override
	public void delete(FormaPagamento formaPagamento) throws SQLException {

		try (Connection conn = App.getDataSource().getConnection()) {
			String sql = "DELETE FROM " + ProceduresViewsTables.TABELA_FORMA_PAGAMENTO + " WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, formaPagamento.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// registro excluido
			} else {
				// nenhum registro excluido
			}
		}

	}

	@Override
	public List<FormaPagamento> findAll() throws SQLException {
		List<FormaPagamento> returnList = new ArrayList<>();
		String sql = "SELECT * FROM " + ProceduresViewsTables.VIEW_FORMAS_PAGAMENTO.getValue();
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
	public List<FormaPagamento> findAllFiltered(FormaPagamento formaPagamento) throws SQLException {
		List<FormaPagamento> returnList = findAll();
		returnList.removeIf(p -> !p.equals(formaPagamento));
		return returnList;
	}

	@Override
	public FormaPagamento findById(int id) throws SQLException {
		FormaPagamento formaPagamento = null;
		String sql = "SELECT * FROM" + ProceduresViewsTables.VIEW_FORMAS_PAGAMENTO.getValue() + " WHERE id = ?";
		try (Connection conn = App.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				formaPagamento = fetch(rs);
			}
		}
		return formaPagamento;
	}

	@Override
	public FormaPagamento fetch(ResultSet res) throws SQLException {
		FormaPagamento formaPagamento = new FormaPagamento();
		formaPagamento.setId(res.getInt("id"));
		formaPagamento.setDescricao(res.getString("descricao"));
		return formaPagamento;
	}

}
