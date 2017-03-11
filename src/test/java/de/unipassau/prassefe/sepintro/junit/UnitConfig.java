package de.unipassau.prassefe.sepintro.junit;

import com.mysql.cj.jdbc.MysqlDataSource;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.config.Backend;

public class UnitConfig extends AbstractConfig {

	private static UnitConfig instance;
	
	public static synchronized UnitConfig getInstance() {
		if(instance == null) {
			instance = new UnitConfig();
		}
		
		return instance;
	}
	
	private UnitConfig() {
		// should be used as singleton
	}
	
	@Override
	public void reload() {
		MysqlDataSource db = new MysqlDataSource();
		// TODO Read this from environment variable!
		db.setUser("sep");
		db.setPassword("changeme");
		db.setDatabaseName("junit");
		setBackend(Backend.SQL);
		setDataSource(db);
	}
	
	@Override
	public Backend getBackend() {
		throw new IllegalAccessError();
	}

}
