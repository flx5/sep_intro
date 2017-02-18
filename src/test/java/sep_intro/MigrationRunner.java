package sep_intro;

import java.io.IOException;

import org.junit.Test;

import sep_intro.model.config.Config;
import sep_intro.model.migrations.MigrationIndex;

public class MigrationRunner {

	@Test
	public void test() throws IOException {
		new MigrationIndex(new Config()).migrateToLatest();
	}

}
