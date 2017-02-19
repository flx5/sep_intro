package de.unipassau.prassefe.sepintro.model.repository;

import de.unipassau.prassefe.sepintro.model.User;

public interface UserRepository extends Repository<User, Integer> {
	User getByUserName(String username);
	void deleteByUsername(String username);
}
