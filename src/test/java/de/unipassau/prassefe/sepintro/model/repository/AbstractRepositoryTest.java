package de.unipassau.prassefe.sepintro.model.repository;

import org.junit.After;
import org.junit.Before;

import de.unipassau.prassefe.sepintro.model.repository.sql.UnitConfig;

public abstract class AbstractRepositoryTest {

	private CreateableRepository<?,?> repository;
	
	public AbstractRepositoryTest(CreateableRepository<?, ?> repository) {
		this.repository = repository;
	}

	@Before
	public void setUp() throws Exception {
		this.repository.setConfig(new UnitConfig());
		this.repository.create();
	}

	@After
	public void tearDown() throws Exception {
		this.repository.destroy();
		this.repository.close();
	}

}
