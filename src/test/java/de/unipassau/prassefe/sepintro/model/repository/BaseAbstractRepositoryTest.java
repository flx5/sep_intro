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

	public BaseAbstractRepositoryTest(Repository<T, K> repository) {
		this.repository = repository;
		this.repository.setConfig(UnitConfig.defaultInstance());
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
	
	protected abstract T newPoco();
	protected abstract K getKey(T poco);
	protected abstract void changePoco(T poco);
	
	@Test
	public final void testInsertAndDelete() {
		T poco = newPoco();

		repository.insert(poco);
		Optional<T> fromDb = repository.getById(getKey(poco));

		assertTrue(fromDb.isPresent());
		assertEquals(poco, fromDb.get());
		
		repository.delete(poco);
		
		assertTrue(!repository.getById(getKey(poco)).isPresent());
	}
	
	@Test
	public final void testQueryAll() {
		@SuppressWarnings("unchecked")
		T[] expected = (T[])new Object[] {
				newPoco(),
				newPoco()
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
		T poco = newPoco();
		
		repository.insert(poco);
		
		changePoco(poco);
		
		repository.update(poco);
		
		Optional<T> fromDb = repository.getById(getKey(poco));
		
		assertTrue(fromDb.isPresent());
		assertEquals(poco, fromDb.get());
	}
}
