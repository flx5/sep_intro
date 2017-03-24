package de.unipassau.prassefe.sepintro.model.config;

import de.unipassau.prassefe.sepintro.model.repository.Repository;

/**
 * The data backends.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public enum Backend {
    /**
     * In memory backend.
     */
    FAKE("Fake"),
    
    /**
     * Sql based backend.
     */
    SQL("Sql");

    private final String repositoryPrefix;

    private Backend(String classPrefix) {
        this(classPrefix.toLowerCase(), classPrefix);
    }

    private Backend(String packageName, String classPrefix) {
        this.repositoryPrefix = Repository.class.getPackage().getName() + '.' + packageName + '.' + classPrefix;
    }

    /**
     * Get the repository class prefix.
     * @return The prefix.
     */
    public String getRepositoryPrefix() {
        return repositoryPrefix;
    }
}
