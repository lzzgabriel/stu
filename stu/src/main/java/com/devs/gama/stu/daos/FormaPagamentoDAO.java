package com.devs.gama.stu.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.devs.gama.stu.app.App;
import com.devs.gama.stu.entities.FormaPagamento;
import com.devs.gama.stu.enums.ProceduresViewsTables;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.SqlUtils;

public class FormaPagamentoDAO {

	public static void save(FormaPagamento formaPagamento) throws SQLException {
		try (Connection conn = App.getInstance().getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(SqlUtils
					.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_FORMA_PAGAMENTO.getValue(), 2, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setNull(parametro++, Types.INTEGER); // Deve ser passado null para registrar
			callableStatement.setString(parametro++, formaPagamento.getDescricao());
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

	public static void edit(FormaPagamento formaPagamento) throws SQLException, EntityNotFoundException {
		try (Connection conn = App.getInstance().getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(SqlUtils
					.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_FORMA_PAGAMENTO.getValue(), 2, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, formaPagamento.getId()); // Deve ser passado o id para atualizar
			callableStatement.setString(parametro++, formaPagamento.getDescricao());
			int linhasAfetadas = callableStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				int idRegistroAtualizado = callableStatement.getInt(1);
				if (idRegistroAtualizado > 0) {
					System.out.println(idRegistroAtualizado);
					// registro atualizado
				}
			} else {
				throw new EntityNotFoundException("Forma de pagamento não encontrada: nenhum registro atualizado");
			}
		}
	}

	public static void delete(FormaPagamento formaPagamento) throws SQLException, EntityNotFoundException {

		try (Connection conn = App.getInstance().getDataSource().getConnection()) {
			String sql = "DELETE FROM " + ProceduresViewsTables.TABELA_FORMA_PAGAMENTO + " WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, formaPagamento.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// registro excluido
			} else {
				throw new EntityNotFoundException("Forma de pagamento não encontrada: nenhum registro excluído");
			}
		}

	}

	public static List<FormaPagamento> findAll() throws SQLException {
		List<FormaPagamento> returnList = new ArrayList<>();
		String sql = "SELECT * FROM " + ProceduresViewsTables.VIEW_FORMAS_PAGAMENTO.getValue();
		try (Connection conn = App.getInstance().getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}
		}
		return returnList;
	}

	public static List<FormaPagamento> findAllFiltered(FormaPagamento formaPagamento) throws SQLException {
		List<FormaPagamento> returnList = findAll();
		returnList.removeIf(p -> !p.equals(formaPagamento));
		return returnList;
	}

	public static FormaPagamento findById(int id) throws SQLException, EntityNotFoundException {
		FormaPagamento formaPagamento = null;
		String sql = "SELECT * FROM" + ProceduresViewsTables.VIEW_FORMAS_PAGAMENTO.getValue() + " WHERE id = ?";
		try (Connection conn = App.getInstance().getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				formaPagamento = fetch(rs);
			} else {
				throw new EntityNotFoundException("Forma de pagamento não encontrada");
			}
		}
		return formaPagamento;
	}

	public static FormaPagamento fetch(ResultSet res) throws SQLException {
		FormaPagamento formaPagamento = new FormaPagamento();

		formaPagamento.setId(res.getInt("id"));
		formaPagamento.setDescricao(res.getString("descricao"));

		return formaPagamento;
	}

}
