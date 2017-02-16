package sep_intro.model.repository.fake;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import sep_intro.model.migrations.MigrationEntry;
import sep_intro.model.repository.MigrationRepository;

public class FakeMigrationRepository extends AbstractFakeRepository<MigrationEntry, Integer> implements MigrationRepository {

	private static ConcurrentMap<Integer, MigrationEntry> storage = new ConcurrentHashMap<>();
	
	private static int idGenerator = 0;
	
	@Override
	public MigrationEntry getCurrentVersion() {
		return storage.values().stream().max((a,b) -> {
			int result = a.getRunAt().compareTo(b.getRunAt());
			
			if(result == 0) {
				result = Integer.compare(a.getId(), b.getId());
			}
			
			return result;
			
		}).orElse(null);
	}

	@Override
	protected Integer getKey(MigrationEntry item) {
		return item.getId();
	}

	@Override
	protected void setKey(MigrationEntry item) {
		item.setId(++idGenerator);
	}

	@Override
	protected ConcurrentMap<Integer, MigrationEntry> getStorage() {
		return storage;
	}
}
