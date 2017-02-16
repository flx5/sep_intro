package sep_intro.model.migrations;

import sep_intro.model.User;
import sep_intro.model.repository.MigrationRepository;
import sep_intro.model.repository.RepositoryFactory;
import sep_intro.model.repository.UserRepository;

public class InitialMigration implements Migration {

	@Override
	public long getId() {
		return 0;
	}

	@Override
	public void up() {
		try(MigrationRepository repo = RepositoryFactory.resolve(MigrationRepository.class)) {
			repo.create();
		}
		
		try(UserRepository repo = RepositoryFactory.resolve(UserRepository.class)) {
			repo.create();
		}
	}

	@Override
	public void down() {
		try(MigrationRepository repo = RepositoryFactory.resolve(MigrationRepository.class)) {
			repo.destroy();
		}
		
		try(UserRepository repo = RepositoryFactory.resolve(UserRepository.class)) {
			repo.destroy();
		}
	}

}
