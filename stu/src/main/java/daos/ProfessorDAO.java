package daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.devs.gama.stu.app.App;
import com.devs.gama.stu.entities.Professor;

public class ProfessorDAO implements DAO<Professor> {

	private String tableName = "professor";

	@Override
	public void save(Professor t) throws SQLException {
		if (findById(t.getId()) == null) {
			try (Connection conn = App.getDataSource().getConnection()) {
//				PreparedStatement ps = conn.prepareStatement(SqlUtils.montarProcedure("cadastrar_professor", 3, 0)); // Procedure
//				int parametro = 1;
//				ps.setString(parametro++, t.getNome());
//				ps.setString(parametro++, t.getEmail());
//				ps.setString(parametro++, t.getSenha());
//				ps.execute();
				String sql = "INSERT INTO " + tableName + " (id, nome, email, senha) VALUES (?, ?, ?, ?)";
				PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int parametro = 1;
				preparedStatement.setNull(parametro++, Types.INTEGER);
				preparedStatement.setString(parametro++, t.getNome());
				preparedStatement.setString(parametro++, t.getEmail());
				preparedStatement.setString(parametro++, t.getSenha());
				int linhasAfetadas = preparedStatement.executeUpdate();
				if (linhasAfetadas > 0) {
					ResultSet chavesGeradas = preparedStatement.getGeneratedKeys();
					if (chavesGeradas.next()) {
						int novoId = chavesGeradas.getInt(1);
						System.out.println(novoId);
						// registro inserido
					} else {
						// nenhum registro inserido
					}
				}
			}
		} else {
			edit(t);
		}
	}

	@Override
	public void edit(Professor t) throws SQLException {
		try (Connection conn = App.getDataSource().getConnection()) {
			String sql = "UPDATE " + tableName + " SET nome = ?, email = ?, senha = ? WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setString(parametro++, t.getNome());
			preparedStatement.setString(parametro++, t.getEmail());
			preparedStatement.setString(parametro++, t.getSenha());
			preparedStatement.setInt(parametro++, t.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// atualizou
			} else {
				// nenhum registro atualizado
			}
		}
	}

	@Override
	public void delete(Professor t) throws SQLException {

		try (Connection conn = App.getDataSource().getConnection()) {
			String sql = "DELETE FROM " + tableName + " WHERE id = ?";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, t.getId());
			int linhasAfetadas = preparedStatement.executeUpdate();
			if (linhasAfetadas > 0) {
				// registro excluido
			} else {
				// nenhum registro deletado
			}
		}

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
		Professor professor = null;
		String sql = "select * from " + tableName + " where id = ?";
		try (Connection conn = App.getDataSource().getConnection()) {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			int parametro = 1;
			preparedStatement.setInt(parametro++, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				professor = fetch(rs);
			}
		}
		return professor;
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
