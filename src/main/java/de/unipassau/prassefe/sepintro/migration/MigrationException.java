package de.unipassau.prassefe.sepintro.migration;

public class MigrationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MigrationException() {
		super();
	}

	public MigrationException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public MigrationException(String msg) {
		super(msg);
	}

	public MigrationException(Throwable throwable) {
		super(throwable);
	}

	
}
