package de.unipassau.prassefe.sepintro.model.repository.fake;

import static org.junit.Assert.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Test;

import de.unipassau.prassefe.sepintro.model.TestPoco;
import de.unipassau.prassefe.sepintro.model.repository.BaseAbstractRepositoryTest;
import de.unipassau.prassefe.sepintro.model.repository.UnitTestRepository;

public class AbstractFakeRepositoryTest extends BaseAbstractRepositoryTest {

	private TestRepository repository;

	public AbstractFakeRepositoryTest() {
		this(new TestRepository());
	}

	private AbstractFakeRepositoryTest(TestRepository repository) {
		super(repository);
		this.repository = repository;
	}

	private static class TestRepository extends AbstractFakeRepository<TestPoco, Integer>
			implements UnitTestRepository<TestPoco, Integer> {
		private ConcurrentMap<Integer, TestPoco> storage = new ConcurrentHashMap<>();
		private IdGenerator<Integer> idGenerator = new IntIdGenerator();

		@Override
		protected Integer getKey(TestPoco item) {
			return item.getId();
		}

		@Override
		protected void setKey(TestPoco item) {
			item.setId(idGenerator.next());
		}

		@Override
		protected ConcurrentMap<Integer, TestPoco> getStorage() {
			return storage;
		}

		@Override
		public void deleteAll() {
			getStorage().clear();
		}
	}
	
	@Test
	public final void testPredicate() {
		TestPoco item = new TestPoco(100, 42);
		repository.insert(item);
		TestPoco fromDb = repository.getByCondition(x -> x.getValue() == 42);
		
		assertEquals(item, fromDb);
	}
}
