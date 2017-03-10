package de.unipassau.prassefe.sepintro.migration;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.repository.MigrationRepository;

public class MigrationIndex {
	private NavigableMap<Long, Migration> migrations;
	private AbstractConfig config;

	public MigrationIndex(AbstractConfig config) {
		this.config = config;
		this.migrations = new TreeMap<>();
		// TODO Implement
		// add(new InitialMigration());
		add(new UserTestMigration());
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

		if (current <= to) {
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
