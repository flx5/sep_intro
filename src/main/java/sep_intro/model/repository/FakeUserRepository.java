package sep_intro.model.repository;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import sep_intro.model.User;

public class FakeUserRepository implements UserRepository {
private static final User[] USERS = { new User("user", "leet"), new User("admin", "1337") };
	
	private static Map<String, User> users = Arrays
			.stream(USERS)
			.collect(Collectors.toMap(x -> x.getUserName(), x -> x));


	@Override
	public User getByUserName(String username) {
		return users.getOrDefault(username, null);
	}
	
}
