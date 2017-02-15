package sep_intro.model.repository.fake;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import sep_intro.model.repository.Repository;

public abstract class AbstractFakeRepository<T, K> implements Repository<T, K> {

	private Map<K, T> data;

	@SafeVarargs
	protected AbstractFakeRepository(T... values) {
		this.data = Arrays.stream(values).collect(Collectors.toMap(x -> {
			setKey(x);
			return getKey(x);
		}, x -> x));
	}
	
	protected abstract K getKey(T item);
	protected abstract void setKey(T item);

	@Override
	public void update(T value) {
		this.data.put(getKey(value), value);
	}

	@Override
	public void insert(T value) {
		if (this.data.containsKey(getKey(value))) {
			throw new IllegalStateException("Value with key exists already!");
		}

		setKey(value);
		
		this.update(value);
	}

	@Override
	public T getById(K id) {
		return data.get(id);
	}

	@Override
	public T getByIdOrDefault(K id) {
		return data.getOrDefault(id, null);
	}

	protected T getByCondition(Predicate<T> predicate) {
		return data.values().stream().filter(predicate).findAny().orElse(null);
	}
	
	@Override
	public void close() {
		// do nothing
	}
}
