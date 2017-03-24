package de.unipassau.prassefe.sepintro.model.repository;

/**
 * Exception for any issue related to repositories.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class RepositoryException extends RuntimeException {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new repository exception.
     *
     * @param inner Cause for this exception.
     */
    public RepositoryException(final Throwable inner) {
        super(inner);
    }

    /**
     * Create a new repository exception.
     *
     * @param message Cause for this exception.
     */
    public RepositoryException(final String message) {
        super(message);
    }

}
