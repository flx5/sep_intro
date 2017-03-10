package de.unipassau.prassefe.sepintro.migration;

import java.sql.Connection;
import java.sql.SQLException;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.config.Backend;
import de.unipassau.prassefe.sepintro.model.repository.RepositoryException;

public abstract class SqlMigration implements Migration {
	@Override
	public Backend[] getBackends() {
		return new Backend[] { Backend.SQL };
	}
	
	@Override
	public final void up(AbstractConfig config) {
		try(Connection conn = config.getDataSource().getConnection()) {
			up(conn);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}
	
	@Override
	public final void down(AbstractConfig config) {
		try(Connection conn = config.getDataSource().getConnection()) {
			down(conn);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}
	
	protected abstract void up(Connection conn);
	protected abstract void down(Connection conn);
}
