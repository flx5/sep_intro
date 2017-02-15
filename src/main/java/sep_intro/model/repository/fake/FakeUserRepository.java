package sep_intro.model.repository.fake;

import sep_intro.model.User;
import sep_intro.model.repository.UserRepository;

public class FakeUserRepository extends AbstractFakeRepository<User, Integer> implements UserRepository {
	private static final User[] USERS = { 
			new User("user", "leet"), 
			new User("admin", "1337") 
	};
	
	private static int idGenerator;
	
	public FakeUserRepository() {
		super(USERS);
	}
	
	@Override
	public User getByUserName(String username) {
		return getByCondition(x -> x.getUserName().equals(username));
	}

	@Override
	protected Integer getKey(User item) {
		return item.getId();
	}

	@Override
	protected void setKey(User item) {
		item.setId(++idGenerator);
	}
}
