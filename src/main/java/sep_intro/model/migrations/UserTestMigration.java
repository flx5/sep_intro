package sep_intro.model.migrations;

import sep_intro.model.User;
import sep_intro.model.repository.RepositoryFactory;
import sep_intro.model.repository.UserRepository;

public class UserTestMigration implements Migration {

	@Override
	public long getId() {
		return 20170216;
	}

	@Override
	public void up() {
		try(UserRepository repo = RepositoryFactory.resolve(UserRepository.class)) {
			repo.insert(new User("admin", "1337"));
			repo.insert(new User("user", "leet"));
		}
	}

	@Override
	public void down() {
		try(UserRepository repo = RepositoryFactory.resolve(UserRepository.class)) {
			repo.deleteByUsername("admin");
			repo.deleteByUsername("user");
		}
	}
	
}
