package sep_intro.model.repository.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import sep_intro.model.migrations.MigrationEntry;
import sep_intro.model.repository.MigrationRepository;

public class SqlMigrationRepository extends AbstractRepository<MigrationEntry, Integer> implements MigrationRepository {

	@Override
	public MigrationEntry getCurrentVersion() {
		if(!tableExists("migrations")) {
			return null;
		}
		
		return queryFirst("SELECT * FROM migrations ORDER BY run_at,id DESC LIMIT 1");
	}

	@Override
	public MigrationEntry getById(Integer id) {
		return queryFirst("SELECT * FROM migrations WHERE id = ?", stmt -> {
			stmt.setInt(1, id);
		});
	}

	@Override
	public void update(MigrationEntry value) {
		nonQuery("UPDATE migrations SET version = ?, run_at = ? WHERE id = ?", stmt -> {
			stmt.setLong(1, value.getVersion());
			stmt.setTimestamp(2, Timestamp.valueOf(value.getRunAt()));
			stmt.setInt(3, value.getId());
		});
	}

	@Override
	public void insert(MigrationEntry value) {
		nonQuery("INSERT INTO migrations (version, run_at) VALUES (?, ?)", stmt -> {
			stmt.setLong(1, value.getVersion());
			stmt.setTimestamp(2, Timestamp.valueOf(value.getRunAt()));
		});
	}

	@Override
	public void deleteById(Integer id) {
		nonQuery("DELETE FROM migrations WHERE id = ?", stmt -> {
			stmt.setInt(1, id);
		});
	}

	@Override
	public void delete(MigrationEntry value) {
		deleteById(value.getId());
	}

	@Override
	protected MigrationEntry toItem(ResultSet result) {
		try {
			MigrationEntry entry = new MigrationEntry();
			entry.setId(result.getInt("id"));
			entry.setVersion(result.getLong("version"));
			entry.setRunAt(result.getTimestamp("run_at").toLocalDateTime());
			
			return entry;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void create() {
		nonQuery("CREATE TABLE migrations ("
				+ "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"
				+ "version BIGINT NOT NULL,"
				+ "run_at TIMESTAMP NOT NULL"
				+ ")");
	}

	@Override
	public void destroy() {
		nonQuery("DROP TABLE migrations");
	}

}
