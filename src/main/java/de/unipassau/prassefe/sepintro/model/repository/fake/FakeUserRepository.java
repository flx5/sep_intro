package de.unipassau.prassefe.sepintro.model.repository.fake;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.unipassau.prassefe.sepintro.model.User;
import de.unipassau.prassefe.sepintro.model.repository.RepositoryException;
import de.unipassau.prassefe.sepintro.model.repository.UserRepository;

/**
 * In memory user repository.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class FakeUserRepository extends AbstractFakeRepository<User, Integer> implements UserRepository {
	private static ConcurrentMap<Integer, User> storage = new ConcurrentHashMap<>();
	
	private static IdGenerator<Integer> idGenerator = new IntIdGenerator();

	@Override
	public void update(User value) {
		
		if(getByCondition(x -> x.getUserName().equals(value.getUserName()) 
				&& x.getId() != value.getId()).isPresent()) {
			throw new RepositoryException("Username exists!");
		}
		
		super.update(value);
	}

	@Override
	public void insert(User value) {
		if(getByUserName(value.getUserName()).isPresent()) {
			throw new RepositoryException("Username exists!");
		}
		
		super.insert(value);
	}

	@Override
	public Optional<User> getByUserName(String username) {
		return getByCondition(x -> x.getUserName().equals(username));
	}

	@Override
	protected Integer getKey(User item) {
		return item.getId();
	}

	@Override
	protected void setKey(User item) {
		if(item.getId() == 0) {
			item.setId(idGenerator.next());
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
