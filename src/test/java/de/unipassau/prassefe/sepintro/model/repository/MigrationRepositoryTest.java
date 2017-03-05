package de.unipassau.prassefe.sepintro.model.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.unipassau.prassefe.sepintro.model.config.Backend;
import de.unipassau.prassefe.sepintro.model.migrations.MigrationEntry;

@RunWith(Parameterized.class)
public class MigrationRepositoryTest extends AbstractRepositoryTest {

	private MigrationRepository repository;

	public MigrationRepositoryTest(MigrationRepository repository) {
		super(repository);
		this.repository = repository;
	}
	
	@Parameters
	public static MigrationRepository[] instancesToTest() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Backend[] backends = Backend.values();
		MigrationRepository[] repositories = new MigrationRepository[backends.length];
		for(int i = 0; i < backends.length; ++i) {
			String className = backends[i].getRepositoryPrefix() + "MigrationRepository";
			repositories[i] = (MigrationRepository) Class.forName(className).newInstance();
		}
		
		return repositories;
	}
	
	@Test
	public final void test() {
		MigrationEntry entry = new MigrationEntry(1);
		entry.setId(1);
		repository.insert(entry);
		MigrationEntry current = repository.getCurrentVersion();
		assertEquals(entry, current);
	}
}
