package de.unipassau.prassefe.sepintro.model.repository;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.unipassau.prassefe.sepintro.junit.UnitConfig;

public abstract class BaseAbstractRepositoryTest<T, K> {

	private final Repository<T, K> repository;
	private final boolean testDuplicates;

	public BaseAbstractRepositoryTest(Repository<T, K> repository, boolean testDuplicates) {
		this.repository = repository;
		this.testDuplicates = testDuplicates;
		this.repository.setConfig(UnitConfig.getInstance());
	}
	
	public BaseAbstractRepositoryTest(Repository<T, K> repository) {
		this(repository, false);
	}
	
	@Before
	public void setUp() throws SQLException {
		this.repository.deleteAll();
	}
	
	@After
	public void tearDown() throws SQLException {
		this.repository.deleteAll();
		this.repository.close();
	}
	
	@Test
	public final void testNotExisting() {
		assertFalse(repository.getById(newKey()).isPresent());
	}
	
	protected abstract T newPoco(K id);
	protected abstract K newKey();
	protected abstract void changePoco(T poco);
	
	@Test
	public final void testInsertAndDelete() {
		K id = newKey();
		T poco = newPoco(id);

		repository.insert(poco);
		Optional<T> fromDb = repository.getById(id);

		assertTrue(fromDb.isPresent());
		assertEquals(poco, fromDb.get());
		
		repository.delete(poco);
		
		assertTrue(!repository.getById(id).isPresent());
	}

	@Test(expected = RepositoryException.class)
	public final void testInsertDuplicate() {
		if(!testDuplicates) {
			throw new RepositoryException("no duplicate test required");
		}
		
		T poco = newPoco(newKey());
		
		repository.insert(poco);
		repository.insert(poco);
	}
	
	@Test
	public final void testQueryAll() {
		@SuppressWarnings("unchecked")
		T[] expected = (T[])new Object[] {
				newPoco(newKey()),
				newPoco(newKey())
		};
		
		for(T poco : expected) {
			repository.insert(poco);
		}
		
		@SuppressWarnings("unchecked")
		T[] values = repository.all().toArray((T[])new Object[0]);
		assertArrayEquals(expected, values);
	}
	
	@Test
	public final void testUpdate() {
		K id = newKey();
		
		T poco = newPoco(id);
		
		repository.insert(poco);
		
		changePoco(poco);
		
		repository.update(poco);
		
		Optional<T> fromDb = repository.getById(id);
		
		assertTrue(fromDb.isPresent());
		assertEquals(poco, fromDb.get());
	}
}
