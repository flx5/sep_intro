package de.unipassau.prassefe.sepintro.model.repository;

import java.util.Optional;

import de.unipassau.prassefe.sepintro.model.MigrationEntry;

/**
 * Migration repository.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public interface MigrationRepository extends Repository<MigrationEntry, Integer> {
    /**
     * Get the current version.
     * @return The current version.
     */
    Optional<MigrationEntry> getCurrentVersion();
}
