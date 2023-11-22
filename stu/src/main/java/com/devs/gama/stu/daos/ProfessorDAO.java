package com.devs.gama.stu.daos;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.devs.gama.stu.app.Application;
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
public class ProfessorDAO {

	@Inject
	private Application application;

	/**
	 * <h1>Método para registrar um novo professor</h1>
	 * <p>
	 * - Método para salvar um novo professor no banco de dados
	 * </p>
	 * 
	 * @param professor Informações do professor que será registrado
	 * @throws SQLException
	 */
	public void save(Professor professor) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {

			PreparedStatement preparedStatement = conn
					.prepareCall(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_CADASTRAR_PROFESSOR.getValue(), 3));

			int parametro = 1;

			FuncoesUtils.setString(parametro++, professor.getNome(), preparedStatement);
			FuncoesUtils.setString(parametro++, professor.getEmail(), preparedStatement);
			FuncoesUtils.setString(parametro++, hashSenha(professor.getSenha()), preparedStatement);
			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Método para edição de professor</h1>
	 * 
	 * <p>
	 * - Método utilizado para edição de um professor
	 * </p>
	 * 
	 * @param professor Professor logado no sistema
	 * @throws SQLException
	 */
	public void edit(Professor professor) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareCall(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_EDITAR_PROFESSOR.getValue(), 3));

			int parametro = 1;

			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setString(parametro++, professor.getNome(), preparedStatement);
			FuncoesUtils.setString(parametro++, professor.getEmail(), preparedStatement);
			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Método para mudar senha deum professor</h1>
	 * <p>
	 * - Método deve ser utilizado para fazer a troca de senha de um professor,
	 * lembrando que não temos acesso a senha em texto puro, somente em hash
	 * </p>
	 * 
	 * @param professor Professor que terá a senha alterada
	 * @param novaSenha Nova senha que será salva para este professor
	 * @throws SQLException
	 */
	public void changePassword(Professor professor, String novaSenha) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareCall(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_ALTERAR_SENHA_PROFESSOR.getValue(), 3));

			int parametro = 1;

			FuncoesUtils.setInt(parametro++, professor.getId(), preparedStatement);
			FuncoesUtils.setString(parametro++, hashSenha(professor.getSenha()), preparedStatement);
			FuncoesUtils.setString(parametro++, hashSenha(novaSenha), preparedStatement);
			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Método para retornar todos os professores</h1>
	 * <p>
	 * - Método deve ser utilizado para fazer uma listagem de todos os professor,
	 * levando em conta se ele está ativo.
	 * </p>
	 * <p>
	 * - No caso do aluno estar inativo e ter a necessidade de mostra-lo, deve-se
	 * usar o método {@link #ativarProfessor(int) ativarProfessor}
	 * </p>
	 * 
	 * @return Retorna um {@code List<Professor>} com base nos parâmetros passados
	 * @throws SQLException
	 */
	public List<Professor> findAll() throws SQLException {
		List<Professor> returnList = new ArrayList<>();
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_PROFESSOR.getValue(), new String[] { "ativo" }));

			int parametro = 1;
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				returnList.add(fetch(resultSet));
			}

			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
		return null;
	}

	/**
	 * <h1>Metódo para buscar um professor por id</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um id, para ser feita a busca do
	 * professor
	 * </p>
	 * 
	 * @param id Id do professor que será feita a busca
	 * @return Novo objeto {@code Professor} ou um objeto {@code Professor} nulo no
	 *         caso de não encontrar no banco
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 */
	public Professor findById(int id) throws SQLException, EntityNotFoundException {
		Professor professor = null;
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_PROFESSOR.getValue(), new String[] { "id", "ativo" }));
			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				professor = fetch(resultSet);
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
			if (Objects.isNull(professor)) {
				throw new EntityNotFoundException("Professor não encontrado");
			}

		}
		return professor;
	}

	/**
	 * <h1>Metódo de parse automático</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um ResultSet, populando um objeto
	 * {@code Professor} e retornando o mesmo
	 * </p>
	 * 
	 * @param res {@code ResultSet} que será usado fazer o parse automatico
	 * @return Novo objeto {@code Professor}
	 * @throws SQLException
	 */
	public Professor fetch(ResultSet res) throws SQLException {
		Professor professor = new Professor();

		professor.setId(res.getInt("id"));
		professor.setNome(res.getString("nome"));
		professor.setEmail(res.getString("email"));

		return professor;
	}

	/**
	 * <h1>Método para realizar a validação de login do professor</h1>
	 * <p>
	 * - Este método deve ser utilizado passando um email e senha, que será
	 * comparado com as informações que temos no banco de dados, procurando uma
	 * correspondencia no mesmo.
	 * </p>
	 * 
	 * @param email Email do professor
	 * @param senha Senha do professor
	 * @return Retorna um objeto {@code Professor} caso tenha achado uma
	 *         correspondencia no banco ou um objeto {@code Professor} nulo se tiver
	 *         falhado
	 * @throws SQLException
	 * @throws EntityNotFoundException
	 */
	public Professor validateLogin(String email, String senha) throws SQLException, EntityNotFoundException {
		Professor professor = null;
		try (Connection connection = application.getDataSource().getConnection()) {

			PreparedStatement preparedStatement = connection.prepareStatement(SqlUtils.montarViewTable(null,
					FuncoesViewsTables.VIEW_PROFESSOR.getValue(), new String[] { "email", "senha", "ativo" }));

			int parametro = 1;
			FuncoesUtils.setString(parametro++, email, preparedStatement);
			FuncoesUtils.setString(parametro++, hashSenha(senha), preparedStatement);
			FuncoesUtils.setBoolean(parametro++, Boolean.TRUE, preparedStatement);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				professor = new Professor();
				professor.setId(resultSet.getInt("id"));
				professor.setNome(resultSet.getString("nome"));
				professor.setEmail(resultSet.getString("email"));
			}
			ProcessamentoFuncoes.closeResultSet(resultSet);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
			if (Objects.isNull(professor)) {
				throw new EntityNotFoundException("Professor não encontrado");
			}

		}
		return professor;
	}

	/**
	 * <h1>Metódo para chamada da função Inativar_professor</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um id, para ser feita a inativação deste
	 * professor.
	 * </p>
	 * - Pode-se realizar a operação de ativação através do método
	 * {@link #ativarProfessor(int) ativarProfessor}
	 * 
	 * @param id Id do aluno que será feita a operação de inativação
	 * @throws SQLException
	 */
	public void inativarProfessor(int id) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(
					SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_INATIVAR_PROFESSOR.getValue(), 1));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Metódo para chamada da função Ativar_professor</h1>
	 * <p>
	 * - Metódo deve ser utilizado passando um id, para ser feita a ativação deste
	 * professor.
	 * </p>
	 * - Pode-se realizar a operação de inativação através do método
	 * {@link #inativarProfessor(int) inativarProfessor}
	 * 
	 * @param id Id do professor que será feita a operação de ativação
	 * @throws SQLException
	 */
	public void ativarProfessor(int id) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement(SqlUtils.montarFuncao(FuncoesViewsTables.FUNCAO_ATIVAR_PROFESSOR.getValue(), 1));

			int parametro = 1;
			FuncoesUtils.setInt(parametro++, id, preparedStatement);

			preparedStatement.execute();

			ProcessamentoFuncoes.finalizarFuncao(preparedStatement);
			ProcessamentoFuncoes.closePreparedStatement(preparedStatement);
		}
	}

	/**
	 * <h1>Método para criptografar a senha do usuário</h1>
	 * <p>
	 * - Este método serve para encodar a senha do usuário em base 64 juntamente com
	 * a criptografia SHA-256
	 * </p>
	 * 
	 * @param senha Senha do usuário que será criptografada
	 * @return Senha criptografada
	 */
	private String hashSenha(String senha) {
		try {
			MessageDigest md = MessageDigest.getInstance("sha-256");
			md.update(senha.getBytes(StandardCharsets.UTF_8));
			byte[] digest = md.digest();

			return String.format("%064x", new BigInteger(1, digest));
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
