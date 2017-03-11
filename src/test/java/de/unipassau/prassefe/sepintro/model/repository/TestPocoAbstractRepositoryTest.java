package de.unipassau.prassefe.sepintro.model.repository;

import de.unipassau.prassefe.sepintro.model.TestPoco;

public abstract class TestPocoAbstractRepositoryTest extends IntAbstractRepositoryTest<TestPoco> {
	public TestPocoAbstractRepositoryTest(Repository<TestPoco, Integer> repository) {
		super(repository);
	}

	@Override
	protected TestPoco newPoco() {
		return new TestPoco(0);
	}
	
	@Override
	protected Integer getKey(TestPoco poco) {
		return poco.getId();
	}

	@Override
	protected void changePoco(TestPoco poco) {
		poco.setValue(poco.getValue() + 42);
	}
}
