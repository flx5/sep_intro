package de.unipassau.prassefe.sepintro.model.repository;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.unipassau.prassefe.sepintro.model.TestPoco;
import de.unipassau.prassefe.sepintro.model.repository.sql.UnitConfig;

public abstract class AbstractRepositoryTest {

	private UnitTestRepository<TestPoco, Integer> repository;
	
	public AbstractRepositoryTest(UnitTestRepository<TestPoco, Integer> repository) {
		this.repository = repository;
	}
	
	@Before
	public void setUp() throws SQLException {
		this.repository.setConfig(new UnitConfig());
		this.repository.create();
	}
	
	@After
	public void tearDown() throws SQLException {
		this.repository.destroy();
		this.repository.close();
	}
	
	@Test
	public final void testNotExisting() {
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
