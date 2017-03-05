package de.unipassau.prassefe.sepintro.model.repository;

import de.unipassau.prassefe.sepintro.model.migrations.MigrationEntry;

public interface MigrationRepository extends CreateableRepository<MigrationEntry, Integer> {
	MigrationEntry getCurrentVersion();
}
