package de.unipassau.prassefe.sepintro.model.repository;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.unipassau.prassefe.sepintro.model.MigrationEntry;
import de.unipassau.prassefe.sepintro.model.config.Backend;
import de.unipassau.prassefe.sepintro.model.repository.fake.IntIdGenerator;

@RunWith(Parameterized.class)
public class MigrationRepositoryTest extends IntAbstractRepositoryTest<MigrationEntry> {

	private final MigrationRepository repository;
	private final IntIdGenerator generator;

	public MigrationRepositoryTest(MigrationRepository repository) {
		super(repository);
		this.repository = repository;
		this.generator = new IntIdGenerator();
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
		Optional<MigrationEntry> current = repository.getCurrentVersion();
		
		assertTrue(current.isPresent());
		assertEquals(entry, current.get());
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
		
		Optional<MigrationEntry> current = repository.getCurrentVersion();
		assertTrue(current.isPresent());
		assertEquals(entry2, current.get());
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
		
		Optional<MigrationEntry> current = repository.getCurrentVersion();
		assertTrue(current.isPresent());
		assertEquals(entry2, current.get());
	}

	@Override
	protected MigrationEntry newPoco() {
		return new MigrationEntry(generator.next());
	}

	@Override
	protected void changePoco(MigrationEntry poco) {
		poco.setRunAt(LocalDateTime.now());
	}
	
	@Override
	protected Integer getKey(MigrationEntry poco) {
		return poco.getId();
	}
}
