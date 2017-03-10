package de.unipassau.prassefe.sepintro.migration;

import java.sql.Connection;
import java.sql.SQLException;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.config.Backend;
import de.unipassau.prassefe.sepintro.util.SQLUtil;

public abstract class SqlMigration extends AbstractMigration {
	@Override
	public Backend[] getBackends() {
		return new Backend[] { Backend.SQL };
	}
	
	@Override
	public final void up(AbstractConfig config) {
		try(Connection conn = config.getDataSource().getConnection()) {
			up(new SQLUtil(conn));
		} catch (SQLException e) {
			throw new MigrationException(e);
		}
	}
	
	@Override
	public final void down(AbstractConfig config) {
		try(Connection conn = config.getDataSource().getConnection()) {
			down(new SQLUtil(conn));
		} catch (SQLException e) {
			throw new MigrationException(e);
		}
	}
	
	protected abstract void up(SQLUtil sqlUtil) throws SQLException;
	protected abstract void down(SQLUtil sqlUtil) throws SQLException;
}
