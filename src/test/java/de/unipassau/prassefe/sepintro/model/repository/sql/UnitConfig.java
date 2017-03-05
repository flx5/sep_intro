package de.unipassau.prassefe.sepintro.model.repository.sql;

import com.mysql.cj.jdbc.MysqlDataSource;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.config.Backend;

public class UnitConfig extends AbstractConfig {

	@Override
	public void reload() {
		MysqlDataSource db = new MysqlDataSource();
		// TODO Read this from environment variable!
		db.setUser("sep");
		db.setPassword("changeme");
		db.setDatabaseName("sep");
		setBackend(Backend.SQL);
		setDataSource(db);
	}

}
