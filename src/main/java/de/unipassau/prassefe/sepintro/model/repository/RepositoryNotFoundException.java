package de.unipassau.prassefe.sepintro.model.repository;

public class RepositoryNotFoundException extends RuntimeException {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 1L;


	public RepositoryNotFoundException(Throwable inner) {
		super(inner);
	}
}
