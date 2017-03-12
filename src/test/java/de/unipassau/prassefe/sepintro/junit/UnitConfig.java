package de.unipassau.prassefe.sepintro.junit;

import org.postgresql.ds.PGSimpleDataSource;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.config.Backend;

public class UnitConfig extends AbstractConfig {

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
		// TODO Support multiple dbs
		PGSimpleDataSource db = new PGSimpleDataSource();
		db.setUser(System.getProperty("junit.db.user", ""));
		db.setPassword(System.getProperty("junit.db.password", ""));
		db.setDatabaseName(System.getProperty("junit.db.schema", ""));
		setDataSource(db);
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
