package de.unipassau.prassefe.sepintro.model.repository.sql;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.unipassau.prassefe.sepintro.model.repository.RepositoryException;

public class AbstractRepositoryTest {
	private TestRepository repository;

	private static class TestPoco {
		private int id;
		private long value;

		public TestPoco(int id, long value) {
			this.id = id;
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public long getValue() {
			return value;
		}
		
		public void setValue(long value) {
			this.value = value;
		}

		public boolean equals(TestPoco other) {
			if (this == other) {
				return true;
			}

			if (other == null) {
				return false;
			}

			return id == other.id && value == other.value;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof TestPoco && equals((TestPoco)obj);
		}
	}

	private static class TestRepository extends AbstractRepository<TestPoco, Integer> {

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

	@Before
	public void setUp() throws SQLException {
		this.repository = new TestRepository();
		this.repository.setConfig(new UnitConfig());
		this.repository.create();
	}

	@After
	public void tearDown() throws SQLException {
		this.repository.destroy();
		this.repository.close();
	}

	@Test
	public final void testInvalidId() {
		assertNull(repository.getById(0));
	}

	@Test
	public final void testInsert() {
		TestPoco poco = new TestPoco(1, 2);

		repository.insert(poco);
		TestPoco fromDb = repository.getById(1);

		assertEquals(poco, fromDb);
		
		repository.delete(poco);
		
		assertNull(repository.getById(1));
	}

	@Test(expected = RepositoryException.class)
	public final void testInsertDuplicate() {
		repository.insert(new TestPoco(1, 2));
		repository.insert(new TestPoco(1, 2));
	}
	
	@Test
	public final void testQueryAll() {
		TestPoco[] expected = new TestPoco[] {
				new TestPoco(1, 2),
				new TestPoco(3, 4),
		};
		
		repository.deleteAll();
		
		for(TestPoco poco : expected) {
			repository.insert(poco);
		}
		
		TestPoco[] values = repository.all().toArray(new TestPoco[0]);
		assertArrayEquals(expected, values);
	}
	
	@Test
	public final void testUpdate() {
		TestPoco poco = new TestPoco(2, 2);
		
		repository.insert(poco);
		
		poco.setValue(4);
		
		repository.update(poco);
		
		TestPoco fromDb = repository.getById(poco.getId());
		
		assertEquals(4, fromDb.getValue());
	}
}
