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
import com.devs.gama.stu.entities.FormaPagamento;
import com.devs.gama.stu.enums.ProceduresViewsTables;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.ProcessamentoFuncoes;
import com.devs.gama.stu.utils.SqlUtils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class FormaPagamentoDAO {

	@Inject
	private Application application;

	public void save(FormaPagamento formaPagamento) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(SqlUtils
					.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_FORMA_PAGAMENTO.getValue(), 1, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setString(parametro++, formaPagamento.getDescricao());
			callableStatement.execute();

			ProcessamentoFuncoes.finalizarProcedure(callableStatement, 1);
			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
		}
	}

	public void edit(FormaPagamento formaPagamento) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_EDITAR_FORMA_PAGAMENTO.getValue(), 2, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, formaPagamento.getId());
			callableStatement.setString(parametro++, formaPagamento.getDescricao());
			callableStatement.execute();

			ProcessamentoFuncoes.finalizarProcedure(callableStatement, 1);
			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
		}
	}

	public void delete(FormaPagamento formaPagamento) throws SQLException {

		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_DELETE_FORMA_PAGAMENTO.getValue(), 1, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, formaPagamento.getId());
			callableStatement.execute();

			ProcessamentoFuncoes.finalizarProcedure(callableStatement, 1);
			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
		}

	}

	public List<FormaPagamento> findAll() throws SQLException {
		List<FormaPagamento> returnList = new ArrayList<>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(
					SqlUtils.montarViewTable(null, ProceduresViewsTables.VIEW_FORMAS_PAGAMENTO.getValue(), null));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return returnList;
	}

	public List<FormaPagamento> findAllFiltered(FormaPagamento formaPagamento) throws SQLException {
		// -> procedure de filtragem
		return null;
	}

	public FormaPagamento findById(int id) throws SQLException, EntityNotFoundException {
		FormaPagamento formaPagamento = null;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(null,
					ProceduresViewsTables.VIEW_FORMAS_PAGAMENTO.getValue(), new String[] { "id" }));
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				formaPagamento = fetch(resultSet);
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
			if (formaPagamento == null) {
				throw new EntityNotFoundException("Forma de pagamento não encontrada");
			}
		}
		return formaPagamento;
	}

	public FormaPagamento fetch(ResultSet res) throws SQLException {
		FormaPagamento formaPagamento = new FormaPagamento();

		formaPagamento.setId(res.getInt("id"));
		formaPagamento.setDescricao(res.getString("descricao"));

		return formaPagamento;
	}

}
