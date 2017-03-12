package de.unipassau.prassefe.sepintro.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import de.unipassau.prassefe.sepintro.util.functional.ThrowingConsumer;
import de.unipassau.prassefe.sepintro.util.functional.ThrowingFunction;

public class SQLUtil {
	
	/**
	 * Known Database types
	 * @author Felix Prasse
	 * @see <a href="http://stackoverflow.com/a/254220">List of common values.</a>
	 */
	public enum DatabaseType {
		MySQL("MySQL"),
		PostgreSQL("PostgreSQL")
		;
		
		
		private final Pattern namePattern;
		
		private DatabaseType(String namePattern) {
			this.namePattern = Pattern.compile(namePattern);
		}
		
		private boolean matches(String name) {
			return namePattern.matcher(name).matches();
		}
		
		public static Optional<DatabaseType> getByName(String name) {
			return Arrays.stream(values()).filter(x -> x.matches(name)).findAny();
		}
	}
	
	private Connection connection;

	public SQLUtil(Connection connection) {
		this.connection = connection;
	}

	private Optional<DatabaseType> getDatabaseType() throws SQLException {
		String name = this.connection.getMetaData().getDatabaseProductName();
		return DatabaseType.getByName(name);
	}
	
	public void createPrimaryKey(String table, String column) throws SQLException {
		Optional<DatabaseType> type = getDatabaseType();
		
		if(!type.isPresent()) {
			throw new UnsupportedOperationException("Unknown database type");
		}
		
		switch(type.get()) {
		case MySQL:
			nonQuery("ALTER TABLE " + table + " MODIFY COLUMN " + column + " INTEGER auto_increment");
			break;
		case PostgreSQL:
			String sequenceName = table + "_" + column + "_seq";
			nonQuery("CREATE SEQUENCE " + sequenceName);
			nonQuery("ALTER TABLE " + table + " ALTER COLUMN " + column + " SET DEFAULT nextval('" + sequenceName + "')");
			nonQuery("ALTER SEQUENCE " + sequenceName + " OWNED BY " + table + "." + column);
			break;
		}
	}
	
	public void nonQuery(String sql) throws SQLException {
		nonQuery(sql, null);
	}

	public void nonQuery(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues)
			throws SQLException {
		nonQuery(sql, setValues, null);
	}

	public <T> List<T> nonQuery(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues,
			ThrowingFunction<ResultSet, T, SQLException> parseGeneratedKey) throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, sql)) {
			if (setValues != null) {
				setValues.accept(stmt);
			}

			stmt.executeUpdate();

			return readGeneratedIds(stmt.getStatement(), parseGeneratedKey);
		}
	}

	private <T> List<T> readGeneratedIds(Statement stmt, ThrowingFunction<ResultSet, T, SQLException> parseGeneratedKey) throws SQLException {
		List<T> generatedIds = new ArrayList<>();

		if(parseGeneratedKey == null) {
			// return empty
			return generatedIds;
		}
		
		try (ResultSet rs = stmt.getGeneratedKeys()) {
			while (rs.next()) {
				generatedIds.add(parseGeneratedKey.apply(rs));
			}
		}

		return generatedIds;
	}

	private ResultSet query(NamedPreparedStatement stmt,
			ThrowingConsumer<NamedPreparedStatement, SQLException> setValues) throws SQLException {

		if (setValues != null) {
			setValues.accept(stmt);
		}

		return stmt.executeQuery();
	}

	public <T> List<T> queryAll(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues,
			ThrowingFunction<ResultSet, T, SQLException> toItem) throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(this.connection, sql)) {
			ResultSet result = query(stmt, setValues);

			List<T> values = new ArrayList<>();

			while (result.next()) {
				values.add(toItem.apply(result));
			}

			return values;

		}
	}

	public <T> Optional<T> queryFirst(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues,
			ThrowingFunction<ResultSet, T, SQLException> toItem) throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(this.connection, sql)) {
			ResultSet result = query(stmt, setValues);
			if (result.first()) {
				return Optional.of(toItem.apply(result));
			} else {
				return Optional.empty();
			}
		}
	}
}
