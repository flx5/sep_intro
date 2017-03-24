package de.unipassau.prassefe.sepintro.model.config;

/**
 * Exception for invalid configurations.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class InvalidConfiguration extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new invalid configuration exception.
     *
     * @param message The reason.
     */
    public InvalidConfiguration(String message) {
        super(message);
    }

    /**
     * Create a new invalid configuration exception.
     *
     * @param cause The reason.
     */
    public InvalidConfiguration(Throwable cause) {
        super(cause);
    }
}
