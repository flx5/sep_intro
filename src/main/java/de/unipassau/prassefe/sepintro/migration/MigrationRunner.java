package de.unipassau.prassefe.sepintro.migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Consumer;

import de.unipassau.prassefe.sepintro.model.MigrationEntry;
import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.config.Backend;
import de.unipassau.prassefe.sepintro.model.repository.MigrationRepository;
import de.unipassau.prassefe.sepintro.util.SQLUtil;

/**
 * Runner for migrations.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class MigrationRunner {

    private NavigableMap<Long, Migration> migrations;
    private AbstractConfig config;

    /**
     * Create a new migration runner.
     *
     * @param config
     */
    public MigrationRunner(AbstractConfig config) {
        this.config = config;
        this.migrations = new TreeMap<>();

        add(new M2017031000_InitialMigration());
        add(new M2017031001_UserMigration());

        initializeDb();
    }

    private void add(Migration migration) {
        this.migrations.put(migration.getId(), migration);
    }

    private void initializeDb() {
        if (config.getBackend() == Backend.SQL) {
            try (Connection conn = config.getDataSource().getConnection()) {
                SQLUtil sqlUtil = new SQLUtil(conn);
                sqlUtil.nonQuery(
                        "CREATE TABLE IF NOT EXISTS migrations (id INTEGER NOT NULL PRIMARY KEY,"
                        + "version BIGINT NOT NULL, run_at TIMESTAMP NOT NULL" + ")");

                sqlUtil.createAutoIncrement("migrations", "id");
            } catch (SQLException e) {
                throw new MigrationException(e);
            }
        }
    }

    private long getCurrentVersion() {
        try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
            return repo.getCurrentVersion().map(MigrationEntry::getVersion).orElse(Long.MIN_VALUE);
        }
    }

    /**
     * Migrate to specified version. This will only migrate to the closest
     * possible migration.
     *
     * @param to The version to be migrated to.
     * @return The actually reached migration.
     */
    public long migrateTo(long to) {
        long current = getCurrentVersion();

        if (current < to) {
            return up(to);
        } else {
            return down(to);
        }
    }

    private long up(long to) {
        return up(getCurrentVersion(), to);
    }

    /**
     * Migrate to latest version.
     * @return The version that was migrated to.
     */
    public long migrateToLatest() {
        return up(Long.MAX_VALUE);
    }

    private long down(long to) {
        return down(getCurrentVersion(), to);
    }

    private long up(long from, long to) {
        NavigableMap<Long, Migration> toRun = migrations.subMap(from, false, to, true);

        if (toRun.isEmpty()) {
            return from;
        }

        return migrate(toRun, x -> x.up(config));
    }

    private long migrate(NavigableMap<Long, Migration> orderedMigrations, Consumer<Migration> runMigration) {
        if (orderedMigrations.isEmpty()) {
            throw new IllegalArgumentException("orderedMigrations");
        }

        try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
            for (Migration migration : orderedMigrations.values()) {

                if (Arrays.stream(migration.getBackends()).anyMatch(b -> b == config.getBackend())) {
                    runMigration.accept(migration);
                    repo.insert(new MigrationEntry(migration.getId()));
                }
            }
        }

        return orderedMigrations.lastEntry().getValue().getId();
    }

    private long down(long from, long to) {
        NavigableMap<Long, Migration> toRun = migrations.descendingMap().subMap(from, true, to, false);

        if (toRun.isEmpty()) {
            return from;
        }

        migrate(toRun, x -> x.down(config));

        Entry<Long, Migration> current = migrations.descendingMap().floorEntry(to);

        try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
            repo.insert(new MigrationEntry(current.getKey()));
        }

        return current.getKey();
    }
}
