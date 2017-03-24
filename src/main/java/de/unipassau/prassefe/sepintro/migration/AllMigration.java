package de.unipassau.prassefe.sepintro.migration;

import de.unipassau.prassefe.sepintro.model.config.Backend;

/**
 * Base migration to be run by every backend.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public abstract class AllMigration extends AbstractMigration {

	@Override
	public Backend[] getBackends() {
		return Backend.values();
	}

}
