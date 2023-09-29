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
import com.devs.gama.stu.entities.Mensalidade.Status;
import com.devs.gama.stu.enums.ProceduresViewsTables;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.ProcessamentoProcedure;
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

			callableStatement.setInt(param++, mensalidade.getAluno().getId());
			callableStatement.setDouble(param++, param);
			callableStatement.setDate(param++, SqlUtils.localDateToDateUTC(mensalidade.getMensalidade()));

			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
			ProcessamentoProcedure.closeCallableStatement(callableStatement);
		}
	}

	public void edit(Mensalidade mensalidade) throws SQLException {
		try (Connection conn = application.getDataSource().getConnection()) {
			CallableStatement callableStatement = conn.prepareCall(SqlUtils
					.montarProcedure(ProceduresViewsTables.PROCEDURE_EDITAR_MENSALIDADE_ABERTA.getValue(), 3, 1));

			int parametro = 1;
			callableStatement.registerOutParameter(parametro++, Types.INTEGER);
			callableStatement.setInt(parametro++, mensalidade.getAluno().getId());
			callableStatement.setDouble(parametro++, mensalidade.getValor().doubleValue());
			callableStatement.setDate(parametro++, SqlUtils.localDateToDateUTC(mensalidade.getMensalidade()));

			callableStatement.execute();

			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
			ProcessamentoProcedure.closeCallableStatement(callableStatement);
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
				mensalidade = fetchAtiva(resultSet);
			}

			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);

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
				returnList.add(fetchAtiva(resultSet));
			}

			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);

			return returnList;
		}
	}

	public Mensalidade fetchAtiva(ResultSet res) throws SQLException {
		Mensalidade mensalidade = new Mensalidade();
		
		Aluno aluno = new Aluno();
		aluno.setId(res.getInt("id_aluno"));
		aluno.setNome(res.getString("aluno_nome"));

		mensalidade.setAluno(aluno);

		mensalidade.setValor(res.getBigDecimal("valor_cobrar"));
		mensalidade.setStatus(Status.parse(res.getString("status")));
		mensalidade.setProximoVencimento(SqlUtils.dateToLocalDate(res.getDate("proximo_vencimento")));

		return mensalidade;
	}

}
