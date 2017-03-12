package de.unipassau.prassefe.sepintro.model.repository.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.BeforeClass;

import de.unipassau.prassefe.sepintro.junit.UnitConfig;
import de.unipassau.prassefe.sepintro.model.TestPoco;
import de.unipassau.prassefe.sepintro.model.repository.RepositoryException;
import de.unipassau.prassefe.sepintro.model.repository.TestPocoAbstractRepositoryTest;

public class AbstractSqlRepositoryTest extends TestPocoAbstractRepositoryTest {

	public AbstractSqlRepositoryTest() {
		super(new TestRepository());
	}

	@BeforeClass
	public static void setUpClass() throws SQLException {
		try(TestRepository repo = new TestRepository())
		{
			repo.setConfig(UnitConfig.defaultInstance());
			repo.create();
		}
	}

	private static class TestRepository extends AbstractRepository<TestPoco, Integer> {

		public TestRepository() {
			super("test");
		}

		public void create() {
			nonQuery(
					"CREATE TABLE IF NOT EXISTS test (id INTEGER NOT NULL PRIMARY KEY, value BIGINT NOT NULL)");
			
			try {
				getSqlUtil().createPrimaryKey("test", "id");
			} catch (SQLException e) {
				throw new RepositoryException(e);
			}
		}

		@Override
		public Optional<TestPoco> getById(Integer id) {
			return queryFirst("SELECT * FROM test WHERE id = :id", stmt -> {
				stmt.setInt("id", id);
			});
		}

		@Override
		public void update(TestPoco value) {
			nonQuery("UPDATE test SET value = :value WHERE id = :id", stmt -> {
				stmt.setInt("id", value.getId());
				stmt.setLong("value", value.getValue());
			});
		}

		@Override
		public void insert(TestPoco value) {
			nonQuerySingle("INSERT INTO test (id, value) VALUES (:id, :value)", stmt -> {
				stmt.setInt("id", value.getId());
				stmt.setLong("value", value.getValue());
			}, rs -> rs.getInt(1)).ifPresent(value::setId);
		}

		@Override
		public void deleteById(Integer id) {
			nonQuery("DELETE FROM test WHERE id = :id", stmt -> {
				stmt.setInt("id", id);
			});
		}

		@Override
		public void delete(TestPoco value) {
			deleteById(value.getId());
		}

		@Override
		protected TestPoco toItem(ResultSet result) throws SQLException {
			return new TestPoco(result.getInt("id"), result.getLong("value"));
		}
	}
}
