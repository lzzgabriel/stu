package com.devs.gama.stu.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Mensalidade;
import com.devs.gama.stu.entities.Mensalidade.Status;
import com.devs.gama.stu.enums.ProceduresViewsTables;
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
	
	//TODO edit, findallfiltered, findbyid

	public void save(Mensalidade mensalidade) throws SQLException {
		try (Connection connection = application.getDataSource().getConnection()) {
			
			CallableStatement callableStatement = connection.prepareCall(SqlUtils.montarProcedure(ProceduresViewsTables.PROCEDURE_GERAR_MENSALIDADE_ABERTA.getValue(), 3, 1));
			
			int param = 1;
			
			callableStatement.registerOutParameter(param++, Types.INTEGER);
			
			callableStatement.setInt(param++, mensalidade.getAluno().getId());
			callableStatement.setDouble(param++, param);
			callableStatement.setDate(param++, SqlUtils.transformarDataUTC(mensalidade.getMensalidade()));
			
			callableStatement.execute();
			
			ProcessamentoProcedure.finalizarProcedure(callableStatement, 1);
			ProcessamentoProcedure.closeCallableStatement(callableStatement);
		}
	}
	
	public List<Mensalidade> findAll() throws SQLException {
		List<Mensalidade> returnList = new ArrayList<>();
		
		String sql = "SELECT * FROM " + ProceduresViewsTables.VIEW_ALUNO_MENSALIDADE_ABERTA.getValue();
		
		try (Connection connection = application.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				returnList.add(fetch(resultSet));
			}
			
			ProcessamentoProcedure.closeResultSet(resultSet);
			ProcessamentoProcedure.closePreparedStatement(preparedStatement);
			
			return returnList;
		}
	}
	
	public Mensalidade fetch(ResultSet res) throws SQLException {
		Mensalidade mensalidade = new Mensalidade();
		
		Aluno aluno = new Aluno();
		aluno.setId(res.getInt("id_aluno"));
		aluno.setNome(res.getString("aluno_nome"));
		
		mensalidade.setAluno(aluno);
		
		mensalidade.setValor(res.getBigDecimal("valor_cobrar"));
		mensalidade.setStatus(Status.parse(res.getString("status")));
		//TODO change esse jeito feio de conversão
		mensalidade.setProximoVencimento(LocalDate.ofInstant(res.getDate("proximo_vencimento").toInstant(), ZoneId.systemDefault()));
		
		return mensalidade;
	}

}
