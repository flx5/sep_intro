package sep_intro.model.migrations;

import sep_intro.model.config.Config;
import sep_intro.model.repository.MigrationRepository;
import sep_intro.model.repository.UserRepository;

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
