package de.unipassau.prassefe.sepintro.model.repository;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.unipassau.prassefe.sepintro.model.User;
import de.unipassau.prassefe.sepintro.model.repository.fake.FakeUserRepository;
import de.unipassau.prassefe.sepintro.model.repository.sql.SqlUserRepository;

@RunWith(Parameterized.class)
public class UserRepositoryTest extends IntAbstractRepositoryTest<User> {

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

		Optional<User> fromDb = this.repository.getByUserName("name");

		assertTrue(fromDb.isPresent());

		fromDb.ifPresent(x -> {
			assertTrue(x.verifyPassword("Password"));
			assertFalse(x.verifyPassword("notPassword"));
			assertFalse(x.verifyPassword("password"));
		});

		repository.deleteByUsername("name");

		fromDb = this.repository.getByUserName("name");
		assertFalse(fromDb.isPresent());
	}

	@Test(expected = RepositoryException.class)
	public final void testUpdateDuplicate() {
		User user1 = newPoco(newKey());
		User user2 = newPoco(newKey());

		this.repository.insert(user1);
		this.repository.insert(user2);

		user2.setUserName(user1.getUserName());
		this.repository.update(user2);

		System.out.println("HALT");
	}

	@Override
	protected User newPoco(Integer id) {
		return new User("name" + id, "Password");
	}

	@Override
	protected void changePoco(User poco) {
		poco.setBirthday(poco.getBirthday().plusDays(1));
	}

}
