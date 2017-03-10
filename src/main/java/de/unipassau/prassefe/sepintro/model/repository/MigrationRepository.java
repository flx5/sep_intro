package de.unipassau.prassefe.sepintro.model.repository;

import java.util.Optional;

import de.unipassau.prassefe.sepintro.migration.MigrationEntry;

public interface MigrationRepository extends CreateableRepository<MigrationEntry, Integer> {
	Optional<MigrationEntry> getCurrentVersion();
}
