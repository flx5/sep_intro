package de.unipassau.prassefe.sepintro.model.config;

import de.unipassau.prassefe.sepintro.model.repository.Repository;

public enum Backend {
	FAKE("Fake"),
	SQL("Sql");
	
	private final String repositoryPrefix;
	
	private Backend(String classPrefix) {
		this(classPrefix.toLowerCase(), classPrefix);
	}
	
	private Backend(String packageName, String classPrefix) {
		this.repositoryPrefix = Repository.class.getPackage().getName() + '.' + packageName + '.' + classPrefix;
	}

	public String getRepositoryPrefix() {
		return repositoryPrefix;
	}
}
