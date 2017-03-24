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

/**
 * Utility for handling sql.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public final class SQLUtil {

    /**
     * Known Database types.
     *
     * @author Felix Prasse <prassefe@fim.uni-passau.de>
     * @see <a href="http://stackoverflow.com/a/254220">List of common
     * values.</a>
     */
    public enum DatabaseType {
        /**
         * Mysql.
         */
        MYSQL("MySQL"),
        /**
         * Postgres.
         */
        POSTGRESQL("PostgreSQL");

        private final Pattern namePattern;

        private DatabaseType(String namePattern) {
            this.namePattern = Pattern.compile(namePattern);
        }

        private boolean matches(String name) {
            return namePattern.matcher(name).matches();
        }

        /**
         * Get database type by given name.
         *
         * @param name The name.
         * @return DB Type.
         */
        public static DatabaseType getByName(String name) {
            return Arrays.stream(values()).filter(x -> x.matches(name)).
                    findAny()
                    .orElseThrow(() -> new UnsupportedOperationException(
                    "Unknown database type"));
        }
    }

    private Connection connection;

    /**
     * Create new sql util.
     *
     * @param connection The active connection.
     */
    public SQLUtil(Connection connection) {
        this.connection = connection;
    }

    private DatabaseType getDatabaseType() throws SQLException {
        String name = this.connection.getMetaData().getDatabaseProductName();
        return DatabaseType.getByName(name);
    }

    /**
     * Get varbinary type based upon db type.
     *
     * @param length The length of the binary field.
     * @return The column descriptor.
     * @throws SQLException
     */
    public String getVariableBinaryType(int length) throws SQLException {
        switch (getDatabaseType()) {
            case MYSQL:
                return "VARBINARY(" + length + ")";
            case POSTGRESQL:
                return "BYTEA";
            default:
                throw new UnsupportedOperationException("Unknown database type");
        }
    }

    /**
     * Add auto increment to column.
     *
     * @param table The table.
     * @param column The column.
     * @throws SQLException
     */
    public void createAutoIncrement(String table, String column) throws
            SQLException {
        switch (getDatabaseType()) {
            case MYSQL:
                nonQuery(
                        "ALTER TABLE " + table + " MODIFY COLUMN " + column + " INTEGER auto_increment");
                break;
            case POSTGRESQL:
                createPgSqlSequence(table, column);
                break;
        }
    }

    private void createPgSqlSequence(String table, String column) throws
            SQLException {
        String sequenceName = table + "_" + column + "_seq";

        StringBuilder query = new StringBuilder();

        query.append("do $$\n");
        query.append("begin\n");
        query.append("IF NOT EXISTS (SELECT 0 FROM pg_class where relname = '").
                append(sequenceName).append("' ) THEN\n");
        query.append("CREATE SEQUENCE ").append(sequenceName).append(";");
        query.append("ALTER TABLE ").append(table).append(" ALTER COLUMN ").
                append(column).append(" SET DEFAULT nextval('").append(
                sequenceName).append("');");
        query.append("ALTER SEQUENCE ").append(sequenceName).
                append(" OWNED BY ").append(table).append(".").append(column).
                append(";");
        query.append("\nEND IF;");
        query.append("end\n");
        query.append("$$\n");
        nonQuery(query.toString());
    }

    /**
     * Execute sql query.
     *
     * @param sql The query.
     * @throws SQLException
     */
    public void nonQuery(String sql) throws SQLException {
        nonQuery(sql, null);
    }

    /**
     * Execute sql query.
     *
     * @param sql The query.
     * @param setValues Callback to set params.
     * @throws SQLException
     */
    public void nonQuery(String sql,
            ThrowingConsumer<NamedPreparedStatement, SQLException> setValues)
            throws SQLException {
        nonQuery(sql, setValues, null);
    }

    /**
     * Execute sql query.
     *
     * @param <T> Key type.
     * @param sql The query.
     * @param setValues Callback to set params.
     * @param parseGeneratedKey Parse generated keys.
     * @return The generated keys.
     * @throws SQLException
     */
    public <T> List<T> nonQuery(String sql,
            ThrowingConsumer<NamedPreparedStatement, SQLException> setValues,
            ThrowingFunction<ResultSet, T, SQLException> parseGeneratedKey) throws
            SQLException {
        try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection,
                sql)) {
            if (setValues != null) {
                setValues.accept(stmt);
            }

            stmt.executeUpdate();

            return readGeneratedIds(stmt.getStatement(), parseGeneratedKey);
        }
    }

    private <T> List<T> readGeneratedIds(Statement stmt,
            ThrowingFunction<ResultSet, T, SQLException> parseGeneratedKey)
            throws SQLException {
        List<T> generatedIds = new ArrayList<>();

        if (parseGeneratedKey == null) {
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
            ThrowingConsumer<NamedPreparedStatement, SQLException> setValues) throws
            SQLException {

        if (setValues != null) {
            setValues.accept(stmt);
        }

        return stmt.executeQuery();
    }

    /**
     * Get all results.
     *
     * @param <T> The entry type.
     * @param sql The query.
     * @param setValues Callback to set params.
     * @param toItem Converter from sql result to item.
     * @return The result.
     * @throws java.sql.SQLException
     */
    public <T> List<T> queryAll(String sql,
            ThrowingConsumer<NamedPreparedStatement, SQLException> setValues,
            ThrowingFunction<ResultSet, T, SQLException> toItem) throws
            SQLException {
        try (NamedPreparedStatement stmt = new NamedPreparedStatement(
                this.connection, sql)) {
            ResultSet result = query(stmt, setValues);

            List<T> values = new ArrayList<>();

            while (result.next()) {
                values.add(toItem.apply(result));
            }

            return values;

        }
    }

    /**
     * Get first result.
     *
     * @param <T> The entry type.
     * @param sql The query.
     * @param setValues Callback to set params.
     * @param toItem Converter from sql result to item.
     * @return The result.
     * @throws java.sql.SQLException
     */
    public <T> Optional<T> queryFirst(String sql,
            ThrowingConsumer<NamedPreparedStatement, SQLException> setValues,
            ThrowingFunction<ResultSet, T, SQLException> toItem) throws
            SQLException {
        try (NamedPreparedStatement stmt = new NamedPreparedStatement(
                this.connection, sql)) {
            ResultSet result = query(stmt, setValues);
            if (result.next()) {
                return Optional.of(toItem.apply(result));
            } else {
                return Optional.empty();
            }
        }
    }
}
