package daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.devs.gama.stu.app.App;
import com.devs.gama.stu.entities.Professor;

public class ProfessorDAO implements DAO<Professor> {

	private String tableName = "professor";

	@Override
	public void save(Professor t) {
	}

	@Override
	public void edit(Professor t) {
	}

	@Override
	public void delete(Professor t) {
	}

	@Override
	public List<Professor> findAll() throws SQLException {

		List<Professor> returnList = new ArrayList<>();

		String sql = "select * from " + tableName;

		try (Connection conn = App.getDataSource().getConnection()) {
			
			CallableStatement callableStatement = conn.prepareCall(sql);
			
			try (ResultSet resultSet = callableStatement.executeQuery()) {
				while (resultSet.next()) {
					returnList.add(fetch(resultSet));
				}
			}
		}

		return returnList;
	}

	@Override
	public List<Professor> findAllFiltered(Professor t) {
		return null;
	}

	@Override
	public Professor findById(int id) throws SQLException {
		return null;
	}

	@Override
	public Professor fetch(ResultSet res) throws SQLException {
		Professor professor = new Professor();

		professor.setId(res.getInt("id"));
		professor.setNome(res.getString("nome"));
		professor.setEmail(res.getString("email"));
		professor.setSenha(res.getString("senha"));

		return professor;
	}

}
