package de.unipassau.prassefe.sepintro.model.repository;

public abstract class IntAbstractRepositoryTest<T> extends BaseAbstractRepositoryTest<T, Integer> {
	private int id = 0;
	
	public IntAbstractRepositoryTest(Repository<T, Integer> repository, boolean testDuplicates) {
		super(repository, testDuplicates);
	}

	public IntAbstractRepositoryTest(Repository<T, Integer> repository) {
		this(repository, true);
	}
	
	@Override
	protected Integer newKey() {
		return ++id;
	}
}
