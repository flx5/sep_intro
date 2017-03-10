package de.unipassau.prassefe.sepintro.model.repository;

import java.util.Optional;

import de.unipassau.prassefe.sepintro.model.User;

public interface UserRepository extends CreateableRepository<User, Integer> {
	Optional<User> getByUserName(String username);
	void deleteByUsername(String username);
}
