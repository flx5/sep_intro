package de.unipassau.prassefe.sepintro.model.migrations;

import de.unipassau.prassefe.sepintro.model.User;
import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.repository.UserRepository;

public class UserTestMigration implements Migration {

	@Override
	public long getId() {
		return 20170216;
	}

	@Override
	public void up(AbstractConfig config) {
		try(UserRepository repo = config.getRepository(UserRepository.class)) {
			repo.insert(new User("admin", "1337"));
			repo.insert(new User("user", "leet"));
		}
	}

	@Override
	public void down(AbstractConfig config) {
		try(UserRepository repo = config.getRepository(UserRepository.class)) {
			repo.deleteByUsername("admin");
			repo.deleteByUsername("user");
		}
	}
	
}
