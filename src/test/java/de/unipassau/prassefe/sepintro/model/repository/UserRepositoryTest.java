package de.unipassau.prassefe.sepintro.model.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.unipassau.prassefe.sepintro.model.User;
import de.unipassau.prassefe.sepintro.model.repository.fake.FakeUserRepository;
import de.unipassau.prassefe.sepintro.model.repository.sql.SqlUserRepository;

@RunWith(Parameterized.class)
public class UserRepositoryTest extends AbstractRepositoryTest {

	private UserRepository repository;

	public UserRepositoryTest(UserRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Parameters
	public static UserRepository[] instancesToTest() {
		return new UserRepository[] { new SqlUserRepository(), new FakeUserRepository() };
	}

	@Test
	public final void test() {
		User user = new User("name", "Password");
		
		this.repository.insert(user);
		
		User fromDb = this.repository.getByUserName("name");

		assertTrue(fromDb.verifyPassword("Password"));
		assertFalse(fromDb.verifyPassword("notPassword"));
		assertFalse(fromDb.verifyPassword("password"));
		
		repository.deleteByUsername("name");
		
		this.repository.insert(fromDb);
		repository.delete(user);
	}

}
