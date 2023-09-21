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
import com.devs.gama.stu.utils.ProcessamentoProcedure;
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
					.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_FORMA_PAGAMENTO.getValue(), 2, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setNull(parametro++, Types.INTEGER); // Deve ser passado null para registrar
			callableStatement.setString(parametro++, formaPagamento.getDescricao());
			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
		}
	}

	public void edit(FormaPagamento formaPagamento) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(SqlUtils
					.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_FORMA_PAGAMENTO.getValue(), 2, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, formaPagamento.getId()); // Deve ser passado o id para atualizar
			callableStatement.setString(parametro++, formaPagamento.getDescricao());
			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
		}
	}

	public void delete(FormaPagamento formaPagamento) throws SQLException {

		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn
					.prepareCall(SqlUtils.montarProcedure("deletar_forma_pagamento", 1, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, formaPagamento.getId());
			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
		}

	}

	public List<FormaPagamento> findAll() throws SQLException {
		List<FormaPagamento> returnList = new ArrayList<>();
		String sql = "SELECT * FROM " + ProceduresViewsTables.VIEW_FORMAS_PAGAMENTO.getValue();
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

	public List<FormaPagamento> findAllFiltered(FormaPagamento formaPagamento) throws SQLException {
		List<FormaPagamento> returnList = findAll();
		returnList.removeIf(p -> !p.equals(formaPagamento));
		return returnList;
	}

	public FormaPagamento findById(int id) throws SQLException, EntityNotFoundException {
		FormaPagamento formaPagamento = null;
		String sql = "SELECT * FROM" + ProceduresViewsTables.VIEW_FORMAS_PAGAMENTO.getValue() + " WHERE id = ?";
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				formaPagamento = fetch(resultSet);
			}
			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);
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
