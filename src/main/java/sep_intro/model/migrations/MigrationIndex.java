package sep_intro.model.migrations;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

import sep_intro.model.repository.MigrationRepository;
import sep_intro.model.repository.RepositoryFactory;

public class MigrationIndex {
	private NavigableMap<Long, Migration> migrations;

	public MigrationIndex() {
		migrations = new TreeMap<Long, Migration>();

		add(new InitialMigration());
		add(new UserTestMigration());
	}

	private void add(Migration migration) {
		this.migrations.put(migration.getId(), migration);
	}

	private long getCurrentVersion() {
		try (MigrationRepository repo = RepositoryFactory.resolve(MigrationRepository.class)) {
			MigrationEntry current = repo.getCurrentVersion();

			if (current == null) {
				return 0;
			}

			return current.getVersion();
		}
	}

	public void migrateTo(long to) {
		long current = getCurrentVersion();
		
		if(current <= to) {
			up(to);
		} else {
			down(to);
		}
	}
	
	private void up(long to) {
		up(getCurrentVersion(), to);
	}
	
	public void migrateToLatest() {
		up(Long.MAX_VALUE);
	}
	
	private void down(long to) {
		down(getCurrentVersion(), to);
	}

	private void up(long from, long to) {

		Collection<Migration> toRun = migrations.subMap(from, false, to, true).values();

		try (MigrationRepository repo = RepositoryFactory.resolve(MigrationRepository.class)) {
			for (Migration migration : toRun) {
				migration.up();

				repo.insert(new MigrationEntry(migration.getId()));
			}
		}
	}

	private void down(long from, long to) {
		Collection<Migration> toRun = migrations.descendingMap().subMap(from, false, to, true).values();

		try (MigrationRepository repo = RepositoryFactory.resolve(MigrationRepository.class)) {
			for (Migration migration : toRun) {
				migration.down();
				repo.insert(new MigrationEntry(migration.getId()));
			}
		}
	}
}
