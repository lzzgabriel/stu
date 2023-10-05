package com.devs.gama.stu.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Mensalidade;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.enums.ProceduresViewsTables;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.ProcedureUtils;
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

	public void save(Mensalidade mensalidade) throws SQLException {
		try (Connection connection = application.getDataSource().getConnection()) {

			CallableStatement callableStatement = connection.prepareCall(SqlUtils
					.montarProcedure(ProceduresViewsTables.PROCEDURE_GERAR_MENSALIDADE_ABERTA.getValue(), 3, 1));

			int param = 1;

			callableStatement.registerOutParameter(param++, Types.INTEGER);

			ProcedureUtils.setInt(param++, mensalidade.getAluno().getId(), callableStatement);
			ProcedureUtils.setDouble(param++, mensalidade.getValor().doubleValue(), callableStatement);
			ProcedureUtils.setDate(param++, mensalidade.getMensalidade(), callableStatement);

			callableStatement.execute();

			ProcessamentoFuncoes.finalizarProcedure(callableStatement, 1);
			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
		}
	}

	public void edit(Mensalidade mensalidade) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(SqlUtils
					.montarProcedure(ProceduresViewsTables.PROCEDURE_EDITAR_MENSALIDADE_ABERTA.getValue(), 3, 1));

			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			ProcedureUtils.setInt(parametro++, mensalidade.getAluno().getId(), callableStatement);
			ProcedureUtils.setDouble(parametro++, mensalidade.getValor().doubleValue(), callableStatement);
			ProcedureUtils.setDate(parametro++, mensalidade.getMensalidade(), callableStatement);

			callableStatement.execute();

			ProcessamentoFuncoes.finalizarProcedure(callableStatement, 1);
			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
		}
	}

	public List<Mensalidade> findAllFiltered(Mensalidade mensalidade) throws SQLException {
		// -> procedure de filtragem
		return null;
	}

	public Mensalidade findById(int id) throws SQLException, EntityNotFoundException {

		Mensalidade mensalidade = null;

		try (Connection connection = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(SqlUtils.montarViewTable(null,
					ProceduresViewsTables.VIEW_ALUNO_MENSALIDADE_ABERTA.getValue(), new String[] { "id" }));

			int parametro = 1;
			preparedStatement.setInt(parametro++, id);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				mensalidade = fetch(resultSet);
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);

			if (Objects.isNull(mensalidade)) {
				throw new EntityNotFoundException("Nenhuma mensalidade encontrada");
			}
		}
		return mensalidade;
	}

	public List<Mensalidade> findAll() throws SQLException {
		List<Mensalidade> returnList = new ArrayList<>();

		try (Connection connection = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(SqlUtils.montarViewTable(null,
					ProceduresViewsTables.VIEW_ALUNO_MENSALIDADE_ABERTA.getValue(), null));

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);

			return returnList;
		}
	}

	public int findCountMensalidadeAberta(Professor professor) throws SQLException {
		int totalRegistros = 0;
		String sql = "SELECT COUNT(ama.id_aluno) as totalRegistros FROM view_aluno_mensalidade_aberta ama JOIN view_aluno_de_professor vap ";
		if (Objects.nonNull(professor)) {
			sql += " where vap.id_professor = ? ";
		}
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			int parametro = 1;
			if (Objects.nonNull(professor)) {
				preparedStatement.setInt(parametro, professor.getId());
			}

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
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarPaginacao(
					"ama.id_aluno, ama.aluno_nome, ama.valor_cobrar, ama.status, ama.proximo_vencimento, vap.id_professor",
					"stu.view_aluno_mensalidade_aberta ama JOIN view_aluno_de_professor vap",
					"vap.id_professor = " + professor.getId(), posicao, padraoPaginacao));

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				listaRetorno.add(fetch(resultSet));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return listaRetorno;
	}

	public int findCountMensalidadeCobrada(Professor professor) throws SQLException {
		int totalRegistros = 0;
		String sql = "SELECT COUNT(ama.id_aluno) as totalRegistros FROM view_aluno_mensalidade_cobrada ama JOIN view_aluno_de_professor vap ";
		if (Objects.nonNull(professor)) {
			sql += " where vap.id_professor = ? ";
		}
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			int parametro = 1;
			if (Objects.nonNull(professor)) {
				preparedStatement.setInt(parametro, professor.getId());
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				totalRegistros = resultSet.getInt("totalRegistros");
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return totalRegistros;
	}

	public List<Mensalidade> paginationMensalidadeCobrada(Professor professor, int posicao, int padraoPaginacao)
			throws SQLException {
		List<Mensalidade> listaRetorno = new ArrayList<Mensalidade>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarPaginacao(
					"amc.id_aluno, amc.aluno_nome, amc.valor_cobrar, amc.status, amc.proximo_vencimento, vap.id_professor",
					"view_aluno_mensalidade_cobra amc JOIN view_aluno_de_professor vap",
					"vap.id_professor = " + professor.getId(), posicao, padraoPaginacao));

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				listaRetorno.add(fetch(resultSet));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return listaRetorno;
	}

	public Mensalidade fetch(ResultSet res) throws SQLException {
		Mensalidade mensalidade = new Mensalidade();

		Aluno aluno = new Aluno();
		aluno.setId(res.getInt("id_aluno"));
		aluno.setNome(res.getString("aluno_nome"));

		mensalidade.setAluno(aluno);

		mensalidade.setValor(res.getBigDecimal("valor_cobrar"));
		mensalidade.setStatus(Mensalidade.parse(res.getString("status")));
		mensalidade.setProximoVencimento(SqlUtils.dateToLocalDate(res.getDate("proximo_vencimento")));

		return mensalidade;
	}

}
