package sep_intro.model.repository;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractFakeRepository<T, K> implements Repository<T, K> {

	private Map<K, T> data;
	private Function<T, K> keySelector;
	private Consumer<T> setInsertKey;

	@SafeVarargs
	protected AbstractFakeRepository(Function<T, K> keySelector, Consumer<T> setInsertKey, T... values) {
		this.data = Arrays.stream(values).collect(Collectors.toMap(x -> {
			setInsertKey.accept(x);
			return keySelector.apply(x);
		}, x -> x));

		this.keySelector = keySelector;
		this.setInsertKey = setInsertKey;
	}

	@Override
	public void update(T value) {
		this.data.put(keySelector.apply(value), value);
	}

	@Override
	public void insert(T value) {
		if (this.data.containsKey(keySelector.apply(value))) {
			throw new IllegalStateException("Value with key exists already!");
		}

		this.setInsertKey.accept(value);
		
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
}
