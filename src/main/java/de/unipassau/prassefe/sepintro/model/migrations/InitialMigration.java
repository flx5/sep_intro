package de.unipassau.prassefe.sepintro.model.migrations;

import de.unipassau.prassefe.sepintro.model.config.Config;
import de.unipassau.prassefe.sepintro.model.repository.MigrationRepository;
import de.unipassau.prassefe.sepintro.model.repository.UserRepository;

public class InitialMigration implements Migration {

	@Override
	public long getId() {
		return 1;
	}

	@Override
	public void up(Config config) {
		try(MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
			repo.create();
		}
		
		try(UserRepository repo = config.getRepository(UserRepository.class)) {
			repo.create();
		}
	}

	@Override
	public void down(Config config) {
		try(UserRepository repo = config.getRepository(UserRepository.class)) {
			repo.destroy();
		}
		
		try(MigrationRepository repo = config.getRepository(MigrationRepository.class)) {
			repo.destroy();
		}
	}

}
