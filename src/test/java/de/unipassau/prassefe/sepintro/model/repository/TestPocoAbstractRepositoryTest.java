package de.unipassau.prassefe.sepintro.model.repository;

import de.unipassau.prassefe.sepintro.model.TestPoco;

public abstract class TestPocoAbstractRepositoryTest extends IntAbstractRepositoryTest<TestPoco> {
	public TestPocoAbstractRepositoryTest(CreateableRepository<TestPoco, Integer> repository) {
		super(repository);
	}

	@Override
	protected TestPoco newPoco(Integer id) {
		return new TestPoco(id, 0);
	}

	@Override
	protected void changePoco(TestPoco poco) {
		poco.setValue(poco.getValue() + 42);
	}
}
