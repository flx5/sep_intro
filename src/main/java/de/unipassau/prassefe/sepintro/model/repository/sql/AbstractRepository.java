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
import de.unipassau.prassefe.sepintro.util.functional.ThrowingFunction;

/**
 * Base class for sql repositories.
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 * @param <T> Type of the entity to be stored.
 * @param <K> Type of the entity key.
 */
public abstract class AbstractRepository<T, K> implements Repository<T, K> {
	private Connection connection;
	private final String table;
	private SQLUtil sqlUtil;

        /**
         * Create new base repository for table.
         * @param table The table name.
         */
	public AbstractRepository(String table) {
		this.table = table;
	}

        /**
         * Get associated sql util.
         * @return the sql util.
         */
	protected SQLUtil getSqlUtil() {
		return sqlUtil;
	}
	
	@Override
	public Collection<T> all() {
		return queryAll("SELECT * FROM " + table, null);
	}

        /**
         * Execute sql query.
         * @param sql The query.
         */
	protected void nonQuery(String sql) {
		nonQuery(sql, null);
	}

        /**
         * Execute sql query.
         * @param sql The query.
         * @param setValues Callback to set params.
         */
	protected void nonQuery(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues) {
		try {
			sqlUtil.nonQuery(sql, setValues);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

        /**
         * Execute sql query.
         * @param sql The query.
         * @param setValues Callback to set params.
         * @param parseGeneratedKey Parse generated keys.
         * @return The generated key.
         */
	protected Optional<K> nonQuerySingle(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues,
			ThrowingFunction<ResultSet, K, SQLException> parseGeneratedKey) {
		List<K> generated = nonQuery(sql, setValues, parseGeneratedKey);

		if (generated.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(generated.get(0));
		}
	}

        /**
         * Execute sql query.
         * @param sql The query.
         * @param setValues Callback to set params.
         * @param parseGeneratedKey Parse generated keys.
         * @return The generated keys.
         */
	private List<K> nonQuery(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues,
			ThrowingFunction<ResultSet, K, SQLException> parseGeneratedKey) {
		try {
			return sqlUtil.nonQuery(sql, setValues, parseGeneratedKey);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

        /**
         * Convert sql result to java object.
         * @param result The sql result set.
         * @return The java object.
         * @throws SQLException Throws sql exception if column not found.
         */
	protected abstract T toItem(ResultSet result) throws SQLException;

        /**
         * Get all results.
         * @param sql The query.
         * @param setValues Callback to set params.
         * @return The result.
         */
	protected List<T> queryAll(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues) {
		try {
			return sqlUtil.queryAll(sql, setValues, this::toItem);
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

        /**
         * Get first item.
         * @param sql The query.
         * @return The found item.
         */
	protected Optional<T> queryFirst(String sql) {
		return queryFirst(sql, null);
	}

        /**
         * Get first item.
         * @param sql The query.
         * @param setValues Callback to set params.
         * @return The found item.
         */
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
