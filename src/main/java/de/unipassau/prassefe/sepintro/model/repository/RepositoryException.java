package de.unipassau.prassefe.sepintro.model.repository;

public class RepositoryException extends RuntimeException {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 1L;

	public RepositoryException(Throwable inner) {
		super(inner);
	}

}
