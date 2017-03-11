package de.unipassau.prassefe.sepintro.junit;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.config.Backend;
import de.unipassau.prassefe.sepintro.model.config.InvalidConfiguration;

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
		try {
			InitialContext ctx = new InitialContext(System.getProperties());
			setDataSource((DataSource) ctx.lookup("junit/db"));
		} catch (NamingException e) {
			throw new InvalidConfiguration(e);
		}
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
