package de.unipassau.prassefe.sepintro.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Prepared statement supporting named parameters.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class NamedPreparedStatement implements AutoCloseable {

    private final Map<String, List<Integer>> paramMap;
    private final PreparedStatement statement;

    /**
     * Create a new prepared statement.
     *
     * @param connection The connection.
     * @param query The query.
     * @throws SQLException
     */
    public NamedPreparedStatement(Connection connection, String query) throws SQLException {
        this.paramMap = new HashMap<>();
        String parsedQuery = parse(query);
        this.statement = connection.prepareStatement(parsedQuery, Statement.RETURN_GENERATED_KEYS);
    }

    private final String parse(String query) {
        int length = query.length();
        int index = 0;

        StringBuilder parsedQuery = new StringBuilder(length);

        boolean inQuotes = false;
        char quoteChar = '"';

        int pos = 0;
        while (pos < length) {
            char c = query.charAt(pos);

            if (c == '\'' || c == '"') {
                if (!inQuotes) {
                    inQuotes = true;
                    quoteChar = c;
                } else if (quoteChar == c) {
                    inQuotes = false;
                }
            } else if (!inQuotes && c == ':' && pos + 1 < length && Character.isJavaIdentifierStart(query.charAt(pos + 1))) {
                String name = getWord(pos + 1, query);
                registerName(name, ++index);

                // Replace parameter with question mark
                c = '?';

                // skip past end of parameter
                pos += name.length();
            }

            parsedQuery.append(c);
            ++pos;
        }

        return parsedQuery.toString();
    }

    private void registerName(String name, int index) {
        if (!paramMap.containsKey(name)) {
            paramMap.put(name, new ArrayList<>());
        }

        paramMap.get(name).add(index);
    }

    private String getWord(int start, String text) {
        int j = findEndOfWord(start, text);
        return text.substring(start, j);
    }

    private int findEndOfWord(int start, String text) {
        int position = start + 1;
        while (position < text.length() && Character.isJavaIdentifierPart(text.charAt(position))) {
            // walk j to end of word
            ++position;
        }
        return position;
    }

    /**
     * Get underlying statement.
     *
     * @return The underlying statement.
     */
    public PreparedStatement getStatement() {
        return statement;
    }

    private List<Integer> getIndexes(String name) {
        List<Integer> indexes = paramMap.get(name);
        if (indexes == null) {
            throw new IllegalArgumentException("Parameter not found: " + name);
        }
        return indexes;
    }

    /**
     * Set object.
     *
     * @param name The param name.
     * @param value The param value.
     * @throws SQLException if an sql exception occurs.
     */
    public void setObject(String name, Object value) throws SQLException {
        for (int index : getIndexes(name)) {
            statement.setObject(index, value);
        }
    }

    /**
     * Set string.
     *
     * @param name The param name.
     * @param value The param value.
     * @throws SQLException if an sql exception occurs.
     */
    public void setString(String name, String value) throws SQLException {
        for (int index : getIndexes(name)) {
            statement.setString(index, value);
        }
    }

    /**
     * Set int.
     *
     * @param name The param name.
     * @param value The param value.
     * @throws SQLException if an sql exception occurs.
     */
    public void setInt(String name, int value) throws SQLException {
        for (int index : getIndexes(name)) {
            statement.setInt(index, value);
        }
    }

    /**
     * Set long.
     *
     * @param name The param name.
     * @param value The param value.
     * @throws SQLException if an sql exception occurs.
     */
    public void setLong(String name, long value) throws SQLException {
        for (int index : getIndexes(name)) {
            statement.setLong(index, value);
        }
    }

    /**
     * Set date.
     *
     * @param name The param name.
     * @param value The param value.
     * @throws SQLException if an sql exception occurs.
     */
    public void setDate(String name, Date value) throws SQLException {
        for (int index : getIndexes(name)) {
            statement.setDate(index, value);
        }
    }

    /**
     * Set date.
     *
     * @param name The param name.
     * @param value The param value.
     * @throws SQLException if an sql exception occurs.
     */
    public void setDate(String name, LocalDate value) throws SQLException {
        setDate(name, Date.valueOf(value));
    }

    /**
     * Set bytes.
     *
     * @param name The param name.
     * @param value The param value.
     * @throws SQLException if an sql exception occurs.
     */
    public void setBytes(String name, byte[] value) throws SQLException {
        for (int index : getIndexes(name)) {
            statement.setBytes(index, value);
        }
    }

    /**
     * Set timestamp.
     *
     * @param name The param name.
     * @param value The param value.
     * @throws SQLException if an sql exception occurs.
     */
    public void setTimestamp(String name, Timestamp value) throws SQLException {
        for (int index : getIndexes(name)) {
            statement.setTimestamp(index, value);
        }
    }

    /**
     * Execute statement.
     * @return True on success.
     * @throws SQLException 
     */
    public boolean execute() throws SQLException {
        return statement.execute();
    }

    /**
     * Execute statement.
     * @return query result.
     * @throws SQLException 
     */
    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }

    /**
     * Execute update.
     * @return Affected row count.
     * @throws SQLException 
     */
    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws SQLException {
        statement.close();
    }
}
