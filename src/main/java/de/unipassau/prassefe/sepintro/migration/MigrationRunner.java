package de.unipassau.prassefe.sepintro.migration;

import java.util.Arrays;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Consumer;

import de.unipassau.prassefe.sepintro.model.MigrationEntry;
import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.repository.MigrationRepository;

public class MigrationRunner {
	private NavigableMap<Long, Migration> migrations;
	private AbstractConfig config;

	public MigrationRunner(AbstractConfig config) {
		this.config = config;
		this.migrations = new TreeMap<>();
		// TODO Implement some kind of finder
		add(new _2017031000_InitialMigration());
		add(new _2017031001_UserMigration());
	}

	private void add(Migration migration) {
		this.migrations.put(migration.getId(), migration);
	}

	private long getCurrentVersion() {
		try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
			return repo.getCurrentVersion().map(x -> x.getVersion()).orElse(0l);
		}
	}

	public void migrateTo(long to) {
		long current = getCurrentVersion();

		if (current < to) {
			up(to);
		} else if(current > to) {
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
		migrate(toRun, x -> x.up(config));
	}

	private void migrate(Collection<Migration> orderedMigrations, Consumer<Migration> runMigration) {
		try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
			for (Migration migration : orderedMigrations) {

				if (Arrays.stream(migration.getBackends()).anyMatch(b -> b == config.getBackend())) {
					runMigration.accept(migration);
					repo.insert(new MigrationEntry(migration.getId()));
				}
			}
		}
	}

	private void down(long from, long to) {
		Collection<Migration> toRun = migrations.descendingMap().subMap(from, true, to, false).values();
		migrate(toRun, x -> x.down(config));
		
		try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
			repo.insert(new MigrationEntry(to));
		}
	}
}
