package sep_intro.model.repository;

public interface Repository<T, K> {
	T getById(K id);
	T getByIdOrDefault(K id);
	void update(T value);
	void insert(T value);
}
