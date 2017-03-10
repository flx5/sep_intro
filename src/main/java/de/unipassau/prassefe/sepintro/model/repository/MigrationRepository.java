package de.unipassau.prassefe.sepintro.model.repository;

import java.util.Optional;

import de.unipassau.prassefe.sepintro.model.MigrationEntry;

public interface MigrationRepository extends Repository<MigrationEntry, Integer> {
	Optional<MigrationEntry> getCurrentVersion();
}
