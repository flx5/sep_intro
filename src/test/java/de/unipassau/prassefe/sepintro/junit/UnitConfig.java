package de.unipassau.prassefe.sepintro.junit;

import com.mysql.cj.jdbc.MysqlDataSource;

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
		MysqlDataSource db = new MysqlDataSource();
		// TODO Read this from environment variable!
		db.setUser("sep");
		db.setPassword("changeme");
		db.setDatabaseName("junit");
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
