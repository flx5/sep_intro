package de.unipassau.prassefe.sepintro.model.repository;

import java.util.Optional;

import de.unipassau.prassefe.sepintro.model.User;

/**
 * User repository contract.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public interface UserRepository extends Repository<User, Integer> {

    /**
     * Obtain user by username.
     * @param username The username.
     * @return The user, if any.
     */
    Optional<User> getByUserName(String username);

    /**
     * Delete user by username.
     * @param username The username.
     */
    void deleteByUsername(String username);
}
