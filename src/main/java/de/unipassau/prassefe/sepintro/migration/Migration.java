package de.unipassau.prassefe.sepintro.migration;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.config.Backend;

/**
 * Migration contract.
 * @author Felix Prasse
 *
 */
public interface Migration {
	
	/**
	 * The id of the migration.
	 * It is recommended to use the following format:
	 * YYYYmmdd<Digit><Digit>
	 * @return The id.
	 */
	long getId();
	
	/**
	 * Get the supported data providers.
	 * @return Array of supported providers
	 */
	Backend[] getBackends();
	
	/**
	 * Migrate up from previous
	 * @param config The active configuration
	 */
	void up(AbstractConfig config);
	
	/**
	 * Migrate down to previous
	 * @param config The active configuration
	 */
	void down(AbstractConfig config);
}
