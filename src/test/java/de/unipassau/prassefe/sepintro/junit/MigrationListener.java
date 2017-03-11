package de.unipassau.prassefe.sepintro.junit;

import java.util.Arrays;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import de.unipassau.prassefe.sepintro.migration.MigrationRunner;
import de.unipassau.prassefe.sepintro.model.MigrationEntry;
import de.unipassau.prassefe.sepintro.model.config.Backend;
import de.unipassau.prassefe.sepintro.model.repository.MigrationRepository;

public class MigrationListener extends RunListener {

	private final UnitConfig[] configs;
	private long version;

	public MigrationListener() {
		this.configs = Arrays.stream(Backend.values()).map(b -> new UnitConfig(b)).toArray(UnitConfig[]::new);
	}

	@Override
	public void testRunStarted(Description description) throws Exception {
		for (UnitConfig config : configs) {
			version = new MigrationRunner(config).migrateToLatest();
		}

		super.testRunStarted(description);
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		for (UnitConfig config : configs) {
			try (MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
				repo.insert(new MigrationEntry(version));
				new MigrationRunner(config).migrateTo(0);
				repo.deleteAll();
			}
			
		}

		super.testRunFinished(result);
	}

}
