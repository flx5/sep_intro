package sep_intro.model.repository;

public interface Repository<T, K> extends AutoCloseable {
	T getById(K id);
	T getByIdOrDefault(K id);
	void update(T value);
	void insert(T value);
	
	void close() throws RuntimeException; 
}
