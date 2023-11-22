package com.devs.gama.stu.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.FormaPagamento;
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
public class FormaPagamentoDAO {

	@Inject
	private Application application;

	/**
	 * <h1>Método para cadastro de novas formas de pagamentos</h1>
	 * <p>
	 * - Método utilizado para cadastrar uma nova forma de pagamento no banco de
	 * dados
	 * </p>
	 * 
	 * @param formaPagamento Informações da nova forma de pagamento que será
	 *                       registrada
	 * @throws SQLException
	 */
	public void save(FormaPagamento formaPagamento) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_CADASTRAR_FORMA_PAGAMENTO.getValue(), 1));
			int parametro = 1;
			FuncoesUtils.setString(parametro++, formaPagamento.getDescricao(), preparedStatement);
			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Método para edição de formas de pagamentos</h1>
	 * 
	 * <p>
	 * - Método utilizado para edição de formas de pagamento
	 * </p>
	 * 
	 * @param formaPagamento Forma de pagamento que terá seus dados alterados
	 * @throws SQLException
	 */
	public void edit(FormaPagamento formaPagamento) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareCall(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_EDITAR_FORMA_PAGAMENTO.getValue(), 2));
			int parametro = 1;
			FuncoesUtils.setInt(parametro++, formaPagamento.getId(), preparedStatement);
			FuncoesUtils.setString(parametro++, formaPagamento.getDescricao(), preparedStatement);
			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Método para deletar uma forma de pagamento do banco de dados</h1>
	 * 
	 * @param formaPagamento forma de pagamento que será excluida
	 * @throws SQLException
	 */
	public void delete(FormaPagamento formaPagamento) throws SQLException {

		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareCall(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_DELETE_FORMA_PAGAMENTO.getValue(), 1));
			int parametro = 1;
			FuncoesUtils.setInt(parametro++, formaPagamento.getId(), preparedStatement);
			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}

	}

	/**
	 * <h1>Método para retornar todas as formas de pagamentos</h1>
	 * <p>
	 * - Método deve ser utilizado para fazer uma listagem de todas as formas de
	 * pagamentos,
	 * </p>
	 * 
	 * @return Retorna um {@code List<FormaPagamento>}
	 * @throws SQLException
	 */
	public List<FormaPagamento> findAll() throws SQLException {
		List<FormaPagamento> returnList = new ArrayList<>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(
					SqlUtils.montarViewTable(null, FuncoesViewsTables.VIEW_FORMAS_PAGAMENTO.getValue(), null));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return returnList;
	}

	/**
	 * <h1>Metódo para buscar uma forma de pagamento por id</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um id, para ser feita a busca da forma
	 * de pagamento
	 * </p>
	 * 
	 * @param id Id da forma de pagamento que será feita a busca
	 * @return Novo objeto {@code FormaPagamento} ou um objeto
	 *         {@code FormaPagamento} nulo no caso de não encontrar no banco
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 */
	public FormaPagamento findById(int id) throws SQLException, EntityNotFoundException {
		FormaPagamento formaPagamento = null;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_FORMAS_PAGAMENTO.getValue(), new String[] { "id" }));
			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);
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

	/**
	 * <h1>Metódo de parse automático</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um ResultSet, populando um objeto
	 * {@code FormaPagamento} e retornando o mesmo
	 * </p>
	 * 
	 * @param res {@code ResultSet} que será usado fazer o parse automatico
	 * @return Novo objeto {@code FormaPagamento}
	 * @throws SQLException
	 */
	public FormaPagamento fetch(ResultSet res) throws SQLException {
		FormaPagamento formaPagamento = new FormaPagamento();

		formaPagamento.setId(res.getInt("id"));
		formaPagamento.setDescricao(res.getString("descricao"));

		return formaPagamento;
	}

}
