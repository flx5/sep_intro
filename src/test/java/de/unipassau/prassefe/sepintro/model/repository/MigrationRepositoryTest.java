package de.unipassau.prassefe.sepintro.model.repository;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.unipassau.prassefe.sepintro.model.config.Backend;
import de.unipassau.prassefe.sepintro.model.migrations.MigrationEntry;

@RunWith(Parameterized.class)
public class MigrationRepositoryTest extends IntAbstractRepositoryTest<MigrationEntry> {

	private MigrationRepository repository;

	public MigrationRepositoryTest(MigrationRepository repository) {
		super(repository, false);
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
	
	@Test
	public final void testMultiple() {
		MigrationEntry entry1 = new MigrationEntry(1);
		entry1.setId(100);
		entry1.setRunAt(LocalDateTime.of(2000, 1, 1, 0, 0));
		
		MigrationEntry entry2 = new MigrationEntry(2);
		entry2.setId(101);
		
		repository.insert(entry1);
		repository.insert(entry2);
		
		MigrationEntry current = repository.getCurrentVersion();
		assertEquals(entry2, current);
	}
	
	@Test
	public final void testSameTime() {
		MigrationEntry entry1 = new MigrationEntry(1);
		entry1.setId(200);

		MigrationEntry entry2 = new MigrationEntry(2);
		entry2.setId(201);
		entry2.setRunAt(entry1.getRunAt());

		repository.insert(entry1);
		repository.insert(entry2);
		
		MigrationEntry current = repository.getCurrentVersion();
		assertEquals(entry2, current);
	}

	@Override
	protected MigrationEntry newPoco(Integer id) {
		MigrationEntry entry = new MigrationEntry(id);
		entry.setId(id);
		return entry;
	}

	@Override
	protected void changePoco(MigrationEntry poco) {
		poco.setRunAt(LocalDateTime.now());
	}
}
