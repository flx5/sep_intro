package de.unipassau.prassefe.sepintro.model.repository.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import de.unipassau.prassefe.sepintro.model.MigrationEntry;
import de.unipassau.prassefe.sepintro.model.repository.MigrationRepository;

/**
 * SQL Migration repository.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class SqlMigrationRepository extends AbstractRepository<MigrationEntry, Integer> implements MigrationRepository {

    /**
     * Create new sql migration repository.
     */
    public SqlMigrationRepository() {
        super("migrations");
    }

    @Override
    public Optional<MigrationEntry> getCurrentVersion() {
        return queryFirst("SELECT * FROM migrations ORDER BY run_at DESC,id DESC LIMIT 1");
    }

    @Override
    public Optional<MigrationEntry> getById(Integer id) {
        return queryFirst("SELECT * FROM migrations WHERE id = :id", stmt -> {
            stmt.setInt("id", id);
        });
    }

    @Override
    public void update(MigrationEntry value) {
        nonQuery("UPDATE migrations SET version = :version, run_at = :runAt WHERE id = :id", stmt -> {
            stmt.setLong("version", value.getVersion());
            stmt.setTimestamp("runAt", Timestamp.valueOf(value.getRunAt()));
            stmt.setInt("id", value.getId());
        });
    }

    @Override
    public void insert(MigrationEntry value) {
        nonQuerySingle("INSERT INTO migrations (version, run_at) VALUES (:version, :runAt)", stmt -> {
            stmt.setLong("version", value.getVersion());
            stmt.setTimestamp("runAt", Timestamp.valueOf(value.getRunAt()));
        }, rs -> rs.getInt(1)).ifPresent(value::setId);
    }

    @Override
    public void deleteById(Integer id) {
        nonQuery("DELETE FROM migrations WHERE id = :id", stmt -> {
            stmt.setInt("id", id);
        });
    }

    @Override
    public void delete(MigrationEntry value) {
        deleteById(value.getId());
    }

    @Override
    protected MigrationEntry toItem(ResultSet result) throws SQLException {
        MigrationEntry entry = new MigrationEntry(result.getLong("version"));
        entry.setId(result.getInt("id"));
        entry.setRunAt(result.getTimestamp("run_at").toLocalDateTime());

        return entry;
    }
}
