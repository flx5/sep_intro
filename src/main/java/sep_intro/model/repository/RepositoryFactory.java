package sep_intro.model.repository;

public class RepositoryFactory {
	private static final String PACKAGE = "sep_intro.model.repository.fake";
	private static final String PREFIX = "Fake";
	
	@SuppressWarnings("unchecked")
	public static <T extends Repository<?, ?>> T resolve(Class<T> interfaceClass) {
		try {
			return (T) Class.forName(PACKAGE + "." + PREFIX + interfaceClass.getSimpleName()).newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
