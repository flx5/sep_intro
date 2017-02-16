package sep_intro;

import org.junit.Test;

import sep_intro.model.migrations.MigrationIndex;

public class MigrationRunner {

	@Test
	public void test() {
		new MigrationIndex().migrateToLatest();
	}

}
