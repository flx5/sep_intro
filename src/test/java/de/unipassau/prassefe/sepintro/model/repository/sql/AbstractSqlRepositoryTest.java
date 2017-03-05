package de.unipassau.prassefe.sepintro.model.repository.sql;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import de.unipassau.prassefe.sepintro.model.TestPoco;
import de.unipassau.prassefe.sepintro.model.repository.AbstractRepositoryTest;
import de.unipassau.prassefe.sepintro.model.repository.UnitTestRepository;

public class AbstractSqlRepositoryTest extends AbstractRepositoryTest {
	
	private TestRepository repository;
	
	public AbstractSqlRepositoryTest() {
		this(new TestRepository());
	}
	
	private AbstractSqlRepositoryTest(TestRepository repository) {
		super(repository);
		this.repository = repository;
	}
	
	private static class TestRepository extends AbstractRepository<TestPoco, Integer> implements UnitTestRepository<TestPoco, Integer> {

		public TestRepository() {
			super("test");
		}

		@Override
		public void create() {
			nonQuery("CREATE TABLE test (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, value BIGINT NOT NULL)");
		}

		@Override
		public TestPoco getById(Integer id) {
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
			nonQuery("INSERT INTO test (id, value) VALUES (:id, :value)", stmt -> {
				stmt.setInt("id", value.getId());
				stmt.setLong("value", value.getValue());
			});
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
		
		public void deleteAll() {
			nonQuery("DELETE FROM test");
		}

		@Override
		protected TestPoco toItem(ResultSet result) throws SQLException {
			return new TestPoco(result.getInt("id"), result.getLong("value"));
		}
	}
	
	
	@Test
	public final void testTableExists() {
		assertTrue(repository.tableExists());
		this.repository.destroy();
		assertFalse(repository.tableExists());
		this.repository.create();
		assertTrue(repository.tableExists());
	}
}
