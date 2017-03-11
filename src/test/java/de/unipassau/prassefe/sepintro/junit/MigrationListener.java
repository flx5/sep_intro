package de.unipassau.prassefe.sepintro.junit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import de.unipassau.prassefe.sepintro.migration.MigrationRunner;

public class MigrationListener extends RunListener {

	private MigrationRunner runner;
	
	public MigrationListener() {
		this.runner = new MigrationRunner(UnitConfig.getInstance());
	}
	
	@Override
	public void testRunStarted(Description description) throws Exception {
		runner.migrateToLatest();
		super.testRunStarted(description);
	}
	
	@Override
	public void testRunFinished(Result result) throws Exception {
		runner.migrateTo(0);
		super.testRunFinished(result);
	}

}
