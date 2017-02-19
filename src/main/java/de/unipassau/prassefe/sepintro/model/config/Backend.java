package de.unipassau.prassefe.sepintro.model.config;

import de.unipassau.prassefe.sepintro.model.repository.Repository;

public enum Backend {
	Fake,
	Sql;
	
	private static final String REPOSITORY_PACKAGE_PREFIX = Repository.class.getPackage().getName();
	
	public String getRepositoryPrefix() {
		return REPOSITORY_PACKAGE_PREFIX + '.' + name().toLowerCase() + '.' + name();
	}
}
