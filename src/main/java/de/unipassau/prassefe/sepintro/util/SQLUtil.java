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

	public SQLUtil(Connection connection) {
		this.connection = connection;
	}

	public int nonQuery(String sql) throws SQLException {
		return nonQuery(sql, null);
	}

	public int nonQuery(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues) throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, sql)) {
			if (setValues != null) {
				setValues.accept(stmt);
			}
			return stmt.executeUpdate();
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
	
	public <T> Optional<T> queryFirst(String sql, ThrowingConsumer<NamedPreparedStatement, SQLException> setValues, ThrowingFunction<ResultSet, T, SQLException> toItem) throws SQLException {
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
