package com.devs.gama.stu.daos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Aluno;
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

	/**
	 * <h1>Método para cadastrar um novo aluno com uma mensalidade aberta</h1>
	 * <p>
	 * - Método deve ser utilizado para ser feito o cadastro de um novo aluno, este
	 * método gera automaticamente a mensalidade aberta referente ao mês que está
	 * sendo feito o cadastro do aluno e ao aluno.
	 * </p>
	 * 
	 * @param professor   Professor logado no sistema
	 * @param aluno       Informações referentes ao novo aluno
	 * @param valor       Valor da mensalidade
	 * @param mensalidade Mensalidade referente ao mês pagante
	 * @throws SQLException
	 */
	public void saveComMensalidade(Professor professor, Aluno aluno, BigDecimal valor, LocalDate mensalidade)
			throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareCall(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_CADASTRAR_ALUNO.getValue(), 6));
			int parametro = 1;
			FuncoesUtils.setString(parametro++, aluno.getNome(), preparedStatement);
			FuncoesUtils.setString(parametro++, aluno.getEmail(), preparedStatement);
			FuncoesUtils.setString(parametro++, aluno.getCelularUnmasked(), preparedStatement);
			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setBigDecimal(parametro++, valor, preparedStatement);
			FuncoesUtils.setDate(parametro++, mensalidade, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Método para cadastrar um novo aluno sem mensalidade aberta</h1>
	 * <p>
	 * - Método deve ser utilizado para ser feito o cadastro de um novo aluno sem
	 * mensalidade, funciona em conjunto com o método
	 * {@link #generateMensalidadeAberta(Aluno, BigDecimal, LocalDate)
	 * generateMensalidadeAberta}, já que este não gera mensalidades abertas
	 * automaticamente
	 * </p>
	 * 
	 * @param professor Professor logado no sistema
	 * @param aluno     Informações referentes ao novo aluno
	 * @throws SQLException
	 */
	public void saveSemMensalidade(Professor professor, Aluno aluno) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_CADASTRAR_ALUNO_SEM_MENSALIDADE.getValue(), 4));
			int parametro = 1;
			FuncoesUtils.setString(parametro++, aluno.getNome(), preparedStatement);
			FuncoesUtils.setString(parametro++, aluno.getEmail(), preparedStatement);
			FuncoesUtils.setString(parametro++, aluno.getCelularUnmasked(), preparedStatement);
			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Método para edição de aluno</h1>
	 * 
	 * <p>
	 * - Método utilizado para edição de um aluno
	 * </p>
	 * 
	 * @param professor Professor logado no sistema
	 * @param aluno     Aluno que terá seus dados alterados
	 * @throws SQLException
	 */
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

	/**
	 * <h1>Método para gerar uma mensalidade aberta</h1>
	 * <p>
	 * - Método deve ser utilizado para gerar uma mensalidade aberta, naturalmente o
	 * método {@link #saveComMensalidade(Professor, Aluno, BigDecimal, LocalDate)
	 * saveComMensalidade}, já faz o cadastro automatico das mensalidades ao
	 * registrar um aluno, não tendo a necessidade de usar separadamente, no
	 * entanto, ao utilizar o método {@link #saveSemMensalidade(Professor, Aluno)},
	 * deve-se ficar atento, pois o mesmo não gera mensalidade automaticamente.
	 * </p>
	 * 
	 * @param aluno              Aluno que será feita a geração da mensalidade
	 *                           aberta
	 * @param valorCobrado       Valor que será cobrado na mensalidade
	 * @param mensalidadeVigente Mensalidade referente ao mês pagante
	 * @throws SQLException
	 */
	public void generateMensalidadeAberta(Aluno aluno, BigDecimal valorCobrado, LocalDate mensalidadeVigente)
			throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_GERAR_MENSALIDADE_ABERTA.getValue(), 3));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, aluno.getId(), preparedStatement);
			FuncoesUtils.setBigDecimal(parametro++, valorCobrado, preparedStatement);
			FuncoesUtils.setDate(parametro++, mensalidadeVigente, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Método para retornar todos os alunos referente a um professor</h1>
	 * <p>
	 * - Método deve ser utilizado para fazer uma listagem com todos os alunos,
	 * levando em conta: Professor e se o aluno está ativo.
	 * </p>
	 * <p>
	 * - No caso do aluno estar inativo e ter a necessidade de mostra-lo, deve-se
	 * usar o método {@link #ativarAluno(int) ativarAluno}
	 * </p>
	 * 
	 * @param professor Professor logado no sistema
	 * @return Retorna um {@code List<Aluno>} com base nos parâmetros passados
	 * @throws SQLException
	 */
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

	/**
	 * <h1>Método para ser feita a contagem total de alunos com base em um
	 * professor</h1>
	 * <p>
	 * - Método que deve ser utilizado para saber a contagem total de alunos
	 * referente a um professor, normalmente utilizado em conjunto com o método
	 * {@link #pagination(Professor, int, int) pagination} para ser feita a
	 * paginação
	 * </p>
	 * 
	 * @param professor Professor logado no sistema
	 * @return Retorna um {@code int totalRegistros} encontrados
	 * @throws SQLException
	 */
	public int findCount(Professor professor) throws SQLException {
		int totalRegistros = 0;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(
					"COUNT(id_aluno) as totalRegistros", FuncoesViewsTables.VIEW_ALUNO_DE_PROFESSOR.getValue(),
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

	/**
	 * <h1>Metódo para ser feita uma paginação de alunos com base em um professor
	 * </h1>
	 * <p>
	 * - Método deve ser utilizado em conjunto com o método
	 * {@link #findCount(Professor) findCount}, este sendo para fazer a contagem de
	 * registros, e este de paginação para paginar os registros totais com base em
	 * um padrão pré-estabelecido.
	 * </p>
	 * 
	 * @param professor       Professor logado no sistema
	 * @param posicao         Página em que a paginação esta
	 * @param padraoPaginacao Quantidade de registros que deverá ser retornado por
	 *                        página
	 * @return Retorna um {@code List<Aluno>} com base nos parâmetros passados
	 * @throws SQLException
	 */
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

	/**
	 * <h1>Metódo para buscar um aluno por id</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um id, para ser feita a busca do aluno
	 * </p>
	 * 
	 * @param id Id do aluno que será feita a busca
	 * @return Novo objeto {@code Aluno} ou um objeto {@code Aluno} nulo no caso de
	 *         não encontrar no banco
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 */
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

	/**
	 * <h1>Metódo para chamada da função Inativar_aluno</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um id, para ser feita a inativação deste
	 * aluno.
	 * </p>
	 * - Pode-se realizar a operação de ativação através do método
	 * {@link #ativarAluno(int) ativarAluno}
	 * 
	 * @param id Id do aluno que será feita a operação de inativação
	 * @throws SQLException
	 */
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

	/**
	 * <h1>Metódo para chamada da função Ativar_aluno</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um id, para ser feita a ativação deste
	 * aluno.
	 * </p>
	 * - Pode-se realizar a operação de inativação através do método
	 * {@link #inativarAluno(int) inativarAluno}
	 * 
	 * @param id Id do aluno que será feita a operação de ativação
	 * @throws SQLException
	 */
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

	/**
	 * <h1>Metódo de parse automático</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um ResultSet, populando um objeto
	 * {@code Aluno} e retornando o mesmo
	 * </p>
	 * 
	 * @param res {@code ResultSet} que será usado fazer o parse automatico
	 * @return Novo objeto {@code Aluno}
	 * @throws SQLException
	 */
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
