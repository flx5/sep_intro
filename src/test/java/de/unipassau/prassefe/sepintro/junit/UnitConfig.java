package de.unipassau.prassefe.sepintro.junit;

import java.util.Optional;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

import com.mysql.cj.jdbc.MysqlDataSource;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.config.Backend;
import de.unipassau.prassefe.sepintro.model.config.InvalidConfiguration;
import de.unipassau.prassefe.sepintro.util.EnumUtil;

public class UnitConfig extends AbstractConfig {

	private enum SqlBackend {
		POSTGRESQL(SqlBackend::pgsqlSupplier), MYSQL(SqlBackend::mySqlSupplier);

		private final Supplier<DataSource> dsSupplier;

		private SqlBackend(Supplier<DataSource> dsSupplier) {
			this.dsSupplier = dsSupplier;
		}

		public DataSource getDataSource() {
			return this.dsSupplier.get();
		}

		private static DataSource pgsqlSupplier() {
			PGSimpleDataSource ds = new PGSimpleDataSource();
			ds.setUser(System.getProperty("junit.db.user", ""));
			ds.setPassword(System.getProperty("junit.db.password", ""));
			ds.setDatabaseName(System.getProperty("junit.db.schema", ""));
			return ds;
		}

		private static DataSource mySqlSupplier() {
			MysqlDataSource ds = new MysqlDataSource();
			ds.setUser(System.getProperty("junit.db.user", ""));
			ds.setPassword(System.getProperty("junit.db.password", ""));
			ds.setDatabaseName(System.getProperty("junit.db.schema", ""));
			return ds;
		}
	}

	private static UnitConfig defaultInstance;

	public static synchronized UnitConfig defaultInstance() {
		if (defaultInstance == null) {
			defaultInstance = new UnitConfig();
		}

		return defaultInstance;
	}

	public UnitConfig() {
		this(null);
	}

	public UnitConfig(Backend backend) {
		setBackend(backend);
	}

	@Override
	public void reload() {
		Optional<SqlBackend> dbType = EnumUtil.lookup(SqlBackend.class,
				System.getProperty("junit.db.type", "PostgreSQL"));
		DataSource ds = dbType.orElseThrow(() -> new InvalidConfiguration("Unknown sql backend")).getDataSource();
		setDataSource(ds);
	}

	@Override
	public Backend getBackend() {
		Backend backend = super.getBackend();

		if (backend == null) {
			throw new IllegalAccessError();
		}

		return backend;
	}

}
