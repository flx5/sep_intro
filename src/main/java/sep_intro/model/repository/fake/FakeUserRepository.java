package sep_intro.model.repository.fake;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import sep_intro.model.User;
import sep_intro.model.repository.UserRepository;

public class FakeUserRepository extends AbstractFakeRepository<User, Integer> implements UserRepository {
	private static ConcurrentMap<Integer, User> storage = new ConcurrentHashMap<>();
	
	private static int idGenerator;
	// TODO Username must be unique
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
		if(item.getId() == 0) {
			item.setId(++idGenerator);
		}
	}

	@Override
	protected ConcurrentMap<Integer, User> getStorage() {
		return storage;
	}

	@Override
	public void deleteByUsername(String username) {
		storage.values().removeIf(x -> x.getUserName().equals(username));
	}
}
