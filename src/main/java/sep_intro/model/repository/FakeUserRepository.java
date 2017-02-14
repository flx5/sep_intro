package sep_intro.model.repository;

import sep_intro.model.User;

public class FakeUserRepository extends AbstractFakeRepository<User, Integer> implements UserRepository {
	private static final User[] USERS = { 
			new User("user", "leet"), 
			new User("admin", "1337") 
	};

	private static FakeUserRepository instance;
	
	public static FakeUserRepository getInstance() {
		if(instance == null) {
			instance = new FakeUserRepository();
		}
		
		return instance;
	}
	
	private static int idGenerator;
	
	private FakeUserRepository() {
		super(x -> x.getId(), x -> x.setId(++idGenerator), USERS);
	}
	
	@Override
	public User getByUserName(String username) {
		return getByCondition(x -> x.getUserName().equals(username));
	}
}
