package de.unipassau.prassefe.sepintro.model.config;

import de.unipassau.prassefe.sepintro.model.repository.Repository;

public enum Backend {
	FAKE("Fake"),
	SQL("Sql");
	
	private static final String REPOSITORY_PACKAGE_PREFIX = Repository.class.getPackage().getName();
	
	private String packageName;
	private String classPrefix;
	
	private Backend(String classPrefix) {
		this(classPrefix.toLowerCase(), classPrefix);
	}
	
	private Backend(String packageName, String classPrefix) {
		this.packageName = packageName;
		this.classPrefix = classPrefix;
	}

	public String getRepositoryPrefix() {
		return REPOSITORY_PACKAGE_PREFIX + '.' + packageName + '.' + classPrefix;
	}
}
