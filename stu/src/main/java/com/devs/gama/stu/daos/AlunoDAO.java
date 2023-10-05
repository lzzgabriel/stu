package com.devs.gama.stu.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.FormaPagamento;
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
public class AlunoDAO {

	@Inject
	private Application application;

	public void save(Professor professor, Aluno aluno) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_CADASTRAR_ALUNO.getValue(), 4, 1));
			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			ProcedureUtils.setString(parametro++, aluno.getNome(), callableStatement);
			ProcedureUtils.setString(parametro++, aluno.getEmail(), callableStatement);
			ProcedureUtils.setString(parametro++, aluno.getCelularUnmasked(), callableStatement);
			ProcedureUtils.setInt(parametro++, professor.getId(), callableStatement);

			callableStatement.execute();

			ProcessamentoFuncoes.finalizarProcedure(callableStatement, 1);
			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
		}
	}

	public void edit(Professor professor, Aluno aluno) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_EDITAR_ALUNO.getValue(), 4, 1));

			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			ProcedureUtils.setInt(parametro++, aluno.getId(), callableStatement);
			ProcedureUtils.setString(parametro++, aluno.getNome(), callableStatement);
			ProcedureUtils.setString(parametro++, aluno.getEmail(), callableStatement);
			ProcedureUtils.setString(parametro++, aluno.getCelularUnmasked(), callableStatement);

			callableStatement.execute();

			ProcessamentoFuncoes.finalizarProcedure(callableStatement, 1);
			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
		}
	}

	public void generateMensalidadeAberta(Aluno aluno, Double valorCobrado, LocalDate mensalidadeVigente)
			throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(SqlUtils
					.montarProcedure(ProceduresViewsTables.PROCEDURE_GERAR_MENSALIDADE_ABERTA.getValue(), 3, 1));

			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			ProcedureUtils.setInt(parametro++, aluno.getId(), callableStatement);
			ProcedureUtils.setDouble(parametro++, valorCobrado, callableStatement);
			ProcedureUtils.setDate(parametro++, mensalidadeVigente, callableStatement);

			callableStatement.execute();

			ProcessamentoFuncoes.finalizarProcedure(callableStatement, 1);
			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
		}
	}

	public void confirmPay(Aluno aluno, FormaPagamento formaPagameto) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(SqlUtils
					.montarProcedure(ProceduresViewsTables.PROCEDURE_GERAR_CONFIRMAR_PAGAMENTO.getValue(), 3, 0));

			int parametro = 1;
			ProcedureUtils.setInt(parametro++, aluno.getId(), callableStatement);
			ProcedureUtils.setDate(parametro++, LocalDate.now(), callableStatement);
			ProcedureUtils.setInt(parametro++, formaPagameto.getId(), callableStatement);

			callableStatement.execute();

			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
		}
	}

	public void delete(Aluno aluno) throws SQLException {

		// -> Retirado até o momento, posteriormente será feito o controle através da
		// coluna "ativo"

	}

	public List<Aluno> findAll() throws SQLException {
		List<Aluno> returnList = new ArrayList<>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(
					SqlUtils.montarViewTable(null, ProceduresViewsTables.VIEW_ALUNO_DE_PROFESSOR.getValue(), null));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return returnList;
	}

	public List<Aluno> findAllFiltered(Aluno aluno) throws SQLException, EntityNotFoundException {
		List<Aluno> returnList = new ArrayList<>();
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(
					SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_FILTRAR_ALUNOS.getValue(), 4, 0));
			int parametro = 1;
			ProcedureUtils.setInt(parametro++, aluno.getId(), callableStatement);
			ProcedureUtils.setString(parametro++, aluno.getNome(), callableStatement);
			ProcedureUtils.setBoolean(parametro++, aluno.getAtivo(), callableStatement);
			ProcedureUtils.setTimestamp(parametro++, aluno.getMomentoCadastro(), callableStatement);

			ResultSet resultSet = callableStatement.executeQuery();

			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closeCallableStatement(callableStatement);
			if (returnList.isEmpty()) {
				throw new EntityNotFoundException("Aluno(s) não encontrado(s)");
			}
		}

		return returnList;
	}

	public int findCount(Professor professor) throws SQLException {
		int totalRegistros = 0;
		String sql = "SELECT COUNT(id_aluno) as totalRegistros FROM "
				+ ProceduresViewsTables.VIEW_ALUNO_DE_PROFESSOR.getValue();
		if (Objects.nonNull(professor)) {
			sql += " where id_professor = ? ";
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

	public List<Aluno> pagination(Professor professor, int posicao, int padraoPaginacao) throws SQLException {
		List<Aluno> listaRetorno = new ArrayList<Aluno>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarPaginacao(
					"adp.id_professor, adp.id_aluno , adp.nome , adp.email , adp.celular , adp.momento_cadastro",
					"view_aluno_de_professor adp", "adp.id_professor = " + professor.getId(), posicao,
					padraoPaginacao));

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				listaRetorno.add(fetch(resultSet));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return listaRetorno;
	}

	public Aluno findById(int id) throws SQLException, EntityNotFoundException {
		Aluno aluno = null;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(null,
					ProceduresViewsTables.VIEW_ALUNO_DE_PROFESSOR.getValue(), new String[] { "id" }));
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				aluno = fetch(resultSet);
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
			if (Objects.isNull(aluno)) {
				throw new EntityNotFoundException("Nenhum aluno encontrado");
			}
		}
		return aluno;
	}

	public Aluno fetch(ResultSet res) throws SQLException {
		Aluno aluno = new Aluno();

		aluno.setId(res.getInt("id_aluno"));
		aluno.setNome(res.getString("nome"));
		aluno.setEmail(res.getString("email"));
		aluno.setCelular(res.getString("celular"));
		aluno.setMomentoCadastro(SqlUtils.timestampToLocalDateTime(res.getTimestamp("momento_cadastro")));

		return aluno;
	}

}
