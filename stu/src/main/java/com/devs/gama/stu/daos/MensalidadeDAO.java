package com.devs.gama.stu.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.FormaPagamento;
import com.devs.gama.stu.entities.Mensalidade;
import com.devs.gama.stu.entities.Professor;
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
public class MensalidadeDAO {

	@Inject
	private Application application;

	public void saveMensalidadeAberta(Mensalidade mensalidade) throws SQLException {
		try (Connection connection = application.getDataSource().getConnection()) {

			PreparedStatement preparedStatement = connection.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_GERAR_MENSALIDADE_ABERTA.getValue(), 3));

			int param = 1;

			FuncoesUtils.setInt(param++, mensalidade.getAluno().getId(), preparedStatement);
			FuncoesUtils.setBigDecimal(param++, mensalidade.getValor(), preparedStatement);
			FuncoesUtils.setDate(param++, mensalidade.getMensalidade(), preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public void editMensalidadeAberta(Mensalidade mensalidade) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_EDITAR_MENSALIDADE_ABERTA.getValue(), 3));

			var venc = mensalidade.getVencimento();
			mensalidade.setVencimento(venc.withDayOfMonth(mensalidade.getDiaVencimento()));
			
			int parametro = 1;
			FuncoesUtils.setInt(parametro++, mensalidade.getAluno().getId(), preparedStatement);
			FuncoesUtils.setBigDecimal(parametro++, mensalidade.getValor(), preparedStatement);
			FuncoesUtils.setDate(parametro++, mensalidade.getVencimento(), preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public Mensalidade findByIdMensalidadeAberta(Aluno aluno) throws SQLException, EntityNotFoundException {

		Mensalidade mensalidade = null;

		try (Connection connection = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_ALUNO_MENSALIDADE_ABERTA.getValue(), new String[] { "id_aluno", "ativo" }));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, aluno.getId(), preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				mensalidade = fetch(resultSet, true);
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);

			if (Objects.isNull(mensalidade)) {
				throw new EntityNotFoundException("Nenhuma mensalidade encontrada");
			}
		}
		return mensalidade;
	}

	public Mensalidade findByIdMensalidadeCobrada(Aluno aluno) throws SQLException, EntityNotFoundException {

		Mensalidade mensalidade = null;

		try (Connection connection = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(
					SqlUtils.montarViewTable(null, FuncoesViewsTables.VIEW_ALUNO_MENSALIDADES_COBRADAS.getValue(),
							new String[] { "id_aluno", "ativo" }));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, aluno.getId(), preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				mensalidade = fetch(resultSet, false);
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);

			if (Objects.isNull(mensalidade)) {
				throw new EntityNotFoundException("Nenhuma mensalidade encontrada");
			}
		}
		return mensalidade;
	}

	public List<Mensalidade> findAllMensalidadeAberta(Professor professor) throws SQLException {
		List<Mensalidade> returnList = new ArrayList<>();

		try (Connection connection = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(
					SqlUtils.montarViewTable(null, FuncoesViewsTables.VIEW_ALUNO_MENSALIDADE_ABERTA.getValue(),
							new String[] { "id_professor", "ativo" }));
			int parametro = 1;

			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				returnList.add(fetch(resultSet, true));
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);

			return returnList;
		}
	}

	public List<Mensalidade> findAllMensalidadeCobrada(Professor professor, Aluno aluno) throws SQLException {
		List<Mensalidade> returnList = new ArrayList<>();

		try (Connection connection = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_ALUNO_MENSALIDADES_COBRADAS.getValue(), new String[] { "id_aluno" }));
			int parametro = 1;

			FuncoesUtils.setInt(parametro++, aluno.getId(), preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				returnList.add(fetch(resultSet, false));
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);

			return returnList;
		}
	}

	public int findCountMensalidadeAberta(Professor professor) throws SQLException {
		int totalRegistros = 0;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(
					"COUNT(id_aluno) as totalRegistros", FuncoesViewsTables.VIEW_ALUNO_MENSALIDADE_ABERTA.getValue(),
					new String[] { "id_professor", "ativo" }));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				totalRegistros = resultSet.getInt("totalRegistros");
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return totalRegistros;
	}

	public List<Mensalidade> paginationMensalidadeAberta(Professor professor, int posicao, int padraoPaginacao)
			throws SQLException {
		List<Mensalidade> listaRetorno = new ArrayList<Mensalidade>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarPaginacao(null,
					FuncoesViewsTables.VIEW_ALUNO_MENSALIDADE_ABERTA.getValue(),
					"id_professor = " + professor.getId() + " and ativo = " + Boolean.TRUE, posicao, padraoPaginacao));

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				listaRetorno.add(fetch(resultSet, true));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return listaRetorno;
	}

	public int findCountMensalidadeCobrada(Professor professor, Aluno aluno) throws SQLException {
		int totalRegistros = 0;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(
					"COUNT(id_aluno) as totalRegistros", FuncoesViewsTables.VIEW_ALUNO_MENSALIDADES_COBRADAS.getValue(),
					new String[] { "id_professor", "id_aluno", "ativo" }));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setInt(parametro++, aluno.getId(), preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				totalRegistros = resultSet.getInt("totalRegistros");
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return totalRegistros;
	}

	public List<Mensalidade> paginationMensalidadeCobrada(Professor professor, Aluno aluno, int posicao,
			int padraoPaginacao) throws SQLException {
		List<Mensalidade> listaRetorno = new ArrayList<Mensalidade>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(
					SqlUtils.montarPaginacao(null, FuncoesViewsTables.VIEW_ALUNO_MENSALIDADES_COBRADAS.getValue(),
							"id_professor = " + professor.getId() + " and id_aluno = " + aluno.getId(), posicao,
							padraoPaginacao));

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				listaRetorno.add(fetch(resultSet, false));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return listaRetorno;
	}

	public Mensalidade fetch(ResultSet res, boolean b) throws SQLException {
		Mensalidade mensalidade = new Mensalidade();

		Aluno aluno = new Aluno();
		aluno.setId(res.getInt("id_aluno"));
		aluno.setNome(res.getString("aluno_nome"));

		mensalidade.setAluno(aluno);

		mensalidade.setValor(res.getBigDecimal(b ? "valor_cobrar" : "valor_cobrado"));
		if (b)
			mensalidade.setStatus(Mensalidade.parse(res.getString("status")));
		mensalidade.setVencimento(SqlUtils.dateToLocalDate(res.getDate(b ? "proximo_vencimento" : "data_vencimento")));
		mensalidade.setDiaVencimento(mensalidade.getVencimento().getDayOfMonth());

		mensalidade.setMomentoPagamento(SqlUtils
				.timestampToZonedDateTime(res.getTimestamp(b ? "momento_ultimo_pagamento" : "momento_pagamento")));

		return mensalidade;
	}

	public void atualizarMensalidades() throws SQLException {
		try (Connection connection = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = connection
					.prepareStatement(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_ATUALIZAR_STATUS.getValue(), 0));
			preparedStatement.execute();
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}

	}

	public void confirmPay(Aluno aluno, FormaPagamento formaPagameto) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_GERAR_CONFIRMAR_PAGAMENTO.getValue(), 3));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, aluno.getId(), preparedStatement);
			FuncoesUtils.setTimestamp(parametro++, LocalDateTime.now(), preparedStatement);
			FuncoesUtils.setInt(parametro++, formaPagameto.getId(), preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

}
