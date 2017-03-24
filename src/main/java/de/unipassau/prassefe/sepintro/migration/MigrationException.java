package de.unipassau.prassefe.sepintro.migration;

/**
 * Exception for problems with migrations.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class MigrationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new migration exception.
     */
    public MigrationException() {
        super();
    }

    /**
     * Create a new migration exception.
     *
     * @param msg The message.
     * @param throwable The inner exception.
     */
    public MigrationException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    /**
     * Create a new migration exception.
     *
     * @param msg The message.
     */
    public MigrationException(String msg) {
        super(msg);
    }

    /**
     * Create a new migration exception.
     *
     * @param throwable The inner exception.
     */
    public MigrationException(Throwable throwable) {
        super(throwable);
    }

}
