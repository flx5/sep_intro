package sep_intro.model.repository;

import sep_intro.model.migrations.MigrationEntry;

public interface MigrationRepository extends Repository<MigrationEntry, Integer> {
	MigrationEntry getCurrentVersion();
}
