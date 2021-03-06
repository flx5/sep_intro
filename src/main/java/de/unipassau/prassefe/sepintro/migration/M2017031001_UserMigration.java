package de.unipassau.prassefe.sepintro.migration;

import de.unipassau.prassefe.sepintro.model.User;
import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.repository.UserRepository;

/**
 * Migration to initialize user data.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class M2017031001_UserMigration extends AllMigration {
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
