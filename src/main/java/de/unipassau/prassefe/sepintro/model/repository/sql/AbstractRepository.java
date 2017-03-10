package de.unipassau.prassefe.sepintro.model.repository.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.unipassau.prassefe.sepintro.model.config.AbstractConfig;
import de.unipassau.prassefe.sepintro.model.repository.Repository;
import de.unipassau.prassefe.sepintro.model.repository.RepositoryException;
import de.unipassau.prassefe.sepintro.util.NamedPreparedStatement;
import de.unipassau.prassefe.sepintro.util.SQLUtil;
import de.unipassau.prassefe.sepintro.util.functional.ThrowingConsumer;

public abstract class AbstractRepository<T, K> implements Repository<T, K> {
	private Connection connection;
	private final String table;
	private SQLUtil sqlUtil;

	public AbstractRepository(String table) {
		this.table = table;
	}

	@Override
	public Collection<T> all() {
		return queryAll("SELECT * FROM " + table, null);
	}

	protected boolean nonQuery(String sql) {
		return nonQuery(sql, null);
	}

	protected boolean nonQuery(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues) {
		try {
			return sqlUtil.nonQuery(sql, setValues);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	protected abstract T toItem(ResultSet result) throws SQLException;

	protected List<T> queryAll(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues) {
		try {
			return sqlUtil.queryAll(sql, setValues, this::toItem);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	protected Optional<T> queryFirst(String sql) {
		return queryFirst(sql, null);
	}

	protected Optional<T> queryFirst(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues) {
		try {
			return sqlUtil.queryFirst(sql, setValues, this::toItem);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	public void close() {
		try {
			if (!this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	protected boolean tableExists() {
		/*
		 * This is somewhat ugly, but the only reliable variant.
		 * Using metadata produced cached results.
		 */
		try {
			sqlUtil.nonQuery("SELECT 1 from " + table + " LIMIT 1");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public void deleteAll() {
		nonQuery("DELETE FROM " + table);
	}

	@Override
	public void setConfig(AbstractConfig config) {
		try {
			this.connection = config.getDataSource().getConnection();
			this.sqlUtil = new SQLUtil(this.connection);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}
}
