package de.unipassau.prassefe.sepintro.model.repository;

import de.unipassau.prassefe.sepintro.model.repository.fake.IntIdGenerator;

public abstract class IntAbstractRepositoryTest<T> extends BaseAbstractRepositoryTest<T, Integer> {
	protected IntIdGenerator generator = new IntIdGenerator();
	
	public IntAbstractRepositoryTest(Repository<T, Integer> repository) {
		super(repository);
	}
}
