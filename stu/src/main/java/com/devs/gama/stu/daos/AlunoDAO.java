package com.devs.gama.stu.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.PGPooledConnection;
import org.postgresql.jdbc.PgStatement;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.FormaPagamento;
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
public class AlunoDAO {

	@Inject
	private Application application;

	public void save(Professor professor, Aluno aluno, Double valor, LocalDate mensalidade) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareCall(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_CADASTRAR_ALUNO.getValue(), 6));
			int parametro = 1;
			FuncoesUtils.setString(parametro++, aluno.getNome(), preparedStatement);
			FuncoesUtils.setString(parametro++, aluno.getEmail(), preparedStatement);
			FuncoesUtils.setString(parametro++, aluno.getCelularUnmasked(), preparedStatement);
			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setDouble(parametro++, valor, preparedStatement);
			FuncoesUtils.setDate(parametro++, mensalidade, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public void edit(Professor professor, Aluno aluno) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareCall(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_EDITAR_ALUNO.getValue(), 4));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, aluno.getId(), preparedStatement);
			FuncoesUtils.setString(parametro++, aluno.getNome(), preparedStatement);
			FuncoesUtils.setString(parametro++, aluno.getEmail(), preparedStatement);
			FuncoesUtils.setString(parametro++, aluno.getCelularUnmasked(), preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public void generateMensalidadeAberta(Aluno aluno, Double valorCobrado, LocalDate mensalidadeVigente)
			throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_GERAR_MENSALIDADE_ABERTA.getValue(), 3));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, aluno.getId(), preparedStatement);
			FuncoesUtils.setDouble(parametro++, valorCobrado, preparedStatement);
			FuncoesUtils.setDate(parametro++, mensalidadeVigente, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public void confirmPay(Aluno aluno, FormaPagamento formaPagameto) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_GERAR_CONFIRMAR_PAGAMENTO.getValue(), 3));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, aluno.getId(), preparedStatement);
			FuncoesUtils.setDate(parametro++, LocalDate.now(), preparedStatement);
			FuncoesUtils.setInt(parametro++, formaPagameto.getId(), preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public List<Aluno> findAll(Professor professor) throws SQLException {
		List<Aluno> returnList = new ArrayList<>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_ALUNO_DE_PROFESSOR.getValue(), new String[] { "id_professor", "ativo" }));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return returnList;
	}

	public int findCount(Professor professor) throws SQLException {
		int totalRegistros = 0;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement(SqlUtils.montarViewTable("COUNT(id_aluno) as totalRegistros",
							FuncoesViewsTables.VIEW_ALUNO_DE_PROFESSOR.getValue(), new String[] { "id_professor", "ativo" }));

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

	public List<Aluno> pagination(Professor professor, int posicao, int padraoPaginacao) throws SQLException {
		List<Aluno> listaRetorno = new ArrayList<Aluno>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarPaginacao(
					"id_professor, id_aluno , nome , email , celular , momento_cadastro",
					FuncoesViewsTables.VIEW_ALUNO_DE_PROFESSOR.getValue(),
					"id_professor = " + professor.getId() + " and ativo = " + Boolean.TRUE, posicao, padraoPaginacao));

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
					FuncoesViewsTables.VIEW_ALUNO_DE_PROFESSOR.getValue(), new String[] { "id", "ativo" }));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

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

	public void inativarAluno(int id) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_INATIVAR_ALUNO.getValue(), 1));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	public void ativarAluno(int id) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_ATIVAR_ALUNO.getValue(), 1));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
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
