package sep_intro.model.repository.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import sep_intro.model.User;
import sep_intro.model.repository.Repository;

public abstract class AbstractRepository<T, K> implements Repository<T, K> {
// TODO Move to config
	private static final String dbDriver = "com.mysql.jdbc.Driver";
	private static final String dbHost = "localhost";
	private static final String dbName = "sep";
	private static final String dbUser = "sep";
	private static final String dbPassword = "changeme";

	private Connection connection;

	@FunctionalInterface
	interface ThrowingConsumer<T> extends Consumer<T> {
		@Override
		default void accept(final T elem) {
			try {
				acceptThrows(elem);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}

		void acceptThrows(T elem) throws Exception;
	}

	public AbstractRepository() {
		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/" + dbName, dbUser,
					dbPassword);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected int nonQuery(String sql, ThrowingConsumer<PreparedStatement> setValues) {
		try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
			setValues.accept(stmt);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract T toItem(ResultSet result);

	protected List<T> queryAll(String sql, ThrowingConsumer<PreparedStatement> setValues) {
		try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
			ResultSet result = query(stmt, setValues);
			
			List<T> values = new ArrayList<>();
			
			while(result.next()) {
				values.add(toItem(result));
			}
			
			return values;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private ResultSet query(PreparedStatement stmt, ThrowingConsumer<PreparedStatement> setValues) throws SQLException {
		setValues.accept(stmt);
		return stmt.executeQuery();
	}
	
	protected T queryFirst(String sql) {
		return queryFirstOrDefault(sql, x -> {}, null);
	}

	protected T queryFirst(String sql, ThrowingConsumer<PreparedStatement> setValues) {
		return queryFirstOrDefault(sql, setValues, null);
	}
	
	protected T queryFirstOrDefault(String sql, ThrowingConsumer<PreparedStatement> setValues, T defaultValue) {
		try(PreparedStatement stmt = this.connection.prepareStatement(sql)) {
			ResultSet result = query(stmt, setValues);
			if (result.first()) {
				return toItem(result);
			} else {
				return defaultValue;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws RuntimeException {
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			this.connection = null;
		}
	}
}
