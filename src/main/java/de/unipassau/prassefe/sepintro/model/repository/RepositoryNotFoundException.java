package de.unipassau.prassefe.sepintro.model.repository;

/**
 * Exception to be thrown if a repository implementation is missing.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class RepositoryNotFoundException extends RepositoryException {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create new exception.
     * @param inner The reason.
     */
    public RepositoryNotFoundException(final Throwable inner) {
        super(inner);
    }
}
