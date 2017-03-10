package de.unipassau.prassefe.sepintro.migration;

import static org.junit.Assert.*;

import org.junit.Test;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;

public class AbstractMigrationTest {

	private static class EmptyMigration extends AllMigration {
		@Override
		public void up(AbstractConfig config) {
			// do nothing
		}

		@Override
		public void down(AbstractConfig config) {
			// do nothing
		}
	}
	
	private static class _2017031001_ValidMigration extends EmptyMigration {}
	private static class _Invalid2017031001_ValidMigration extends EmptyMigration {}
	
	@Test
	public final void testValid() {
		assertEquals(2017031001, new _2017031001_ValidMigration().getId());
	}
	
	@Test(expected=MigrationException.class)
	public final void testInvalid() {
		new _Invalid2017031001_ValidMigration().getId();
	}

}
