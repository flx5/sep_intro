package sep_intro;

import java.io.IOException;

import org.junit.Test;

import de.unipassau.prassefe.sepintro.model.config.Config;
import de.unipassau.prassefe.sepintro.model.migrations.MigrationIndex;

public class MigrationRunner {

	@Test
	public void test() throws IOException {
		new MigrationIndex(new Config()).migrateToLatest();
	}

}
