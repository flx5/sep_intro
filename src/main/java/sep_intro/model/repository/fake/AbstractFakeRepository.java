package sep_intro.model.repository.fake;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

import sep_intro.model.config.Config;
import sep_intro.model.repository.Repository;

public abstract class AbstractFakeRepository<T, K> implements Repository<T, K> {

	protected abstract K getKey(T item);
	protected abstract void setKey(T item);
	protected abstract ConcurrentMap<K, T> getStorage();

	@Override
	public void update(T value) {
		this.getStorage().put(getKey(value), value);
	}

	@Override
	public void insert(T value) {
		if (this.getStorage().containsKey(getKey(value))) {
			throw new IllegalStateException("Value with key exists already!");
		}

		setKey(value);
		
		this.update(value);
	}

	@Override
	public T getById(K id) {
		return getStorage().get(id);
	}

	protected T getByCondition(Predicate<T> predicate) {
		return getStorage().values().stream().filter(predicate).findAny().orElse(null);
	}
	
	@Override
	public void close() {
		// do nothing
	}

	@Override
	public void deleteById(K id) {
		getStorage().remove(id);
	}

	@Override
	public void delete(T value) {
		deleteById(getKey(value));
	}

	@Override
	public void create() {
		// do nothing
	}

	@Override
	public void destroy() {
		// do nothing
	}
	
	@Override
	public void setConfig(Config config) {
		// nothing to do here
	}
}
