package de.unipassau.prassefe.sepintro.migration;

import de.unipassau.prassefe.sepintro.model.config.Backend;

public abstract class AllMigration extends AbstractMigration {

	@Override
	public Backend[] getBackends() {
		return Backend.values();
	}

}
