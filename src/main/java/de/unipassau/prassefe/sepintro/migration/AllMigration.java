package de.unipassau.prassefe.sepintro.migration;

import de.unipassau.prassefe.sepintro.model.config.Backend;

public abstract class AllMigration implements Migration {

	@Override
	public Backend[] getBackends() {
		return Backend.values();
	}

}
