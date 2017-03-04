package de.unipassau.prassefe.sepintro.model.repository.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.unipassau.prassefe.sepintro.model.config.Config;
import de.unipassau.prassefe.sepintro.model.repository.Repository;
import de.unipassau.prassefe.sepintro.model.repository.RepositoryException;

public abstract class AbstractRepository<T, K> implements Repository<T, K> {
	private Connection connection;
	private final String table;

	@FunctionalInterface
	interface ThrowingConsumer<T> extends Consumer<T> {
		@Override
		default void accept(final T elem) {
			try {
				acceptThrows(elem);
			} catch (final Exception e) {
				throw new RepositoryException(e);
			}
		}

		void acceptThrows(T elem) throws SQLException;
	}

	public AbstractRepository(String table) {
		this.table = table;
	}

	protected int nonQuery(String sql) {
		return nonQuery(sql, null);
	}

	protected int nonQuery(String sql, ThrowingConsumer<NamedPreparedStatement> setValues) {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(this.connection, sql)) {
			if (setValues != null) {
				setValues.accept(stmt);
			}
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	protected abstract T toItem(ResultSet result) throws SQLException;

	protected List<T> queryAll(String sql, ThrowingConsumer<NamedPreparedStatement> setValues) {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(this.connection, sql)) {
			ResultSet result = query(stmt, setValues);

			List<T> values = new ArrayList<>();

			while (result.next()) {
				values.add(toItem(result));
			}

			return values;

		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	private ResultSet query(NamedPreparedStatement stmt, ThrowingConsumer<NamedPreparedStatement> setValues)
			throws SQLException {

		if (setValues != null) {
			setValues.accept(stmt);
		}
		
		return stmt.executeQuery();
	}

	protected T queryFirst(String sql) {
		return queryFirstOrDefault(sql, null, null);
	}

	protected T queryFirst(String sql, ThrowingConsumer<NamedPreparedStatement> setValues) {
		return queryFirstOrDefault(sql, setValues, null);
	}

	protected T queryFirstOrDefault(String sql, ThrowingConsumer<NamedPreparedStatement> setValues, T defaultValue) {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(this.connection, sql)) {
			ResultSet result = query(stmt, setValues);
			if (result.first()) {
				return toItem(result);
			} else {
				return defaultValue;
			}
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

	protected boolean tableExists(String table) {
		try {
			DatabaseMetaData meta = connection.getMetaData();

			try (ResultSet res = meta.getTables(null, null, table, new String[] { "TABLE" })) {
				return res.next();
			}
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}

	@Override
	public void destroy() {
		nonQuery("DROP TABLE " + table);
	}

	@Override
	public void setConfig(Config config) {
		try {
			this.connection = config.getDataSource().getConnection();
		} catch (SQLException e) {
			throw new RepositoryException(e);
		}
	}
}
