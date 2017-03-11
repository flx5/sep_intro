package de.unipassau.prassefe.sepintro.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.unipassau.prassefe.sepintro.util.functional.ThrowingConsumer;
import de.unipassau.prassefe.sepintro.util.functional.ThrowingFunction;

public class SQLUtil {
	private Connection connection;
// TODO Not really a utility class anymore
	public SQLUtil(Connection connection) {
		this.connection = connection;
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

			List<T> generatedIds = new ArrayList<>();

			if (parseGeneratedKey != null) {
				try (ResultSet rs = stmt.getStatement().getGeneratedKeys()) {
					while (rs.next()) {
						generatedIds.add(parseGeneratedKey.apply(rs));
					}
				}
			}

			return generatedIds;
		}
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
