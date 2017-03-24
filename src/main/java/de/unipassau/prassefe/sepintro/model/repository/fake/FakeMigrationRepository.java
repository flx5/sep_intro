package de.unipassau.prassefe.sepintro.model.repository.fake;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.unipassau.prassefe.sepintro.model.MigrationEntry;
import de.unipassau.prassefe.sepintro.model.repository.MigrationRepository;

/**
 * In memory migration repository.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class FakeMigrationRepository extends AbstractFakeRepository<MigrationEntry, Integer>
		implements MigrationRepository {

	private static ConcurrentMap<Integer, MigrationEntry> storage = new ConcurrentHashMap<>();

	private static IdGenerator<Integer> idGenerator = new IntIdGenerator();

	@Override
	public Optional<MigrationEntry> getCurrentVersion() {
		return storage.values().stream().max((a, b) -> {
			int result = a.getRunAt().compareTo(b.getRunAt());

			if (result == 0) {
				result = Integer.compare(a.getId(), b.getId());
			}

			return result;

		});
	}

	@Override
	protected Integer getKey(MigrationEntry item) {
		return item.getId();
	}

	@Override
	protected void setKey(MigrationEntry item) {
		if (item.getId() == 0) {
			item.setId(idGenerator.next());
		}
	}

	@Override
	protected ConcurrentMap<Integer, MigrationEntry> getStorage() {
		return storage;
	}
}
