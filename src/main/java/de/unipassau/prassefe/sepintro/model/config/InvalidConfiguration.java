package de.unipassau.prassefe.sepintro.model.config;

public class InvalidConfiguration extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidConfiguration(String message) {
		super(message);
	}

	public InvalidConfiguration(Throwable cause) {
		super(cause);
	}
}
