package de.unipassau.prassefe.sepintro.model.migrations;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

import de.unipassau.prassefe.sepintro.model.config.Config;
import de.unipassau.prassefe.sepintro.model.repository.MigrationRepository;

public class MigrationIndex {
	private NavigableMap<Long, Migration> migrations;
	private Config config;

	public MigrationIndex(Config config) {
		this.config = config;
		this.migrations = new TreeMap<>();

		add(new InitialMigration());
		add(new UserTestMigration());
	}

	private void add(Migration migration) {
		this.migrations.put(migration.getId(), migration);
	}

	private long getCurrentVersion() {
		try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
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

		try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
			for (Migration migration : toRun) {
				migration.up(config);

				repo.insert(new MigrationEntry(migration.getId()));
			}
		}
	}

	private void down(long from, long to) {
		Collection<Migration> toRun = migrations.descendingMap().subMap(from, true, to, false).values();

		try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
			for (Migration migration : toRun) {
				migration.down(config);
				repo.insert(new MigrationEntry(migration.getId()));
			}
		}
	}
}
