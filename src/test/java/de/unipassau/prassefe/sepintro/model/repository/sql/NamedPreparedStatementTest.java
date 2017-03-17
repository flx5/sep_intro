/**
 * 
 */
package de.unipassau.prassefe.sepintro.model.repository.sql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.unipassau.prassefe.sepintro.junit.UnitConfig;
import de.unipassau.prassefe.sepintro.util.NamedPreparedStatement;

public class NamedPreparedStatementTest {
	private Connection connection;

	@Before
	public void setUp() throws SQLException {
		this.connection = UnitConfig.defaultInstance().getDataSource().getConnection();
	}

	@After
	public void tearDown() throws SQLException {
		this.connection.close();
	}

	@Test
	public final void simpleSelect() throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, "SELECT 'OK'")) {
			try (ResultSet result = stmt.executeQuery()) {
				assertTrue(result.next());
				assertEquals("OK", result.getString(1));
			}
		}
	}

	@Test
	public final void parameterSelect() throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, "SELECT :param")) {
			stmt.setString("param", "OK");
			try (ResultSet result = stmt.executeQuery()) {
				assertTrue(result.next());
				assertEquals("OK", result.getString(1));
			}
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void quotedParameterSelect() throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, "SELECT ':param'")) {
			stmt.setString("param", "FAIL");
		}
	}
	
	@Test
	public final void quotedParameterSelect2() throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, "SELECT ':param', :actual")) {
			stmt.setString("actual", "YAY");
			try (ResultSet result = stmt.executeQuery()) {
				assertTrue(result.next());
				assertEquals(":param", result.getString(1));
				assertEquals("YAY", result.getString(2));
			}
		}
	}
	
	@Test(expected=SQLException.class)
	public final void invalidParamNameStart() throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, "SELECT :#na&me")) {
			stmt.execute();
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void invalidParamName() throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, "SELECT :na#e")) {
			stmt.setString("na", "OK");
			
			// should throw IllegalArgumentException
			stmt.setString("na#e", "FAIL");
		}
	}
	
	@Test(expected=SQLException.class)
	public final void atEnd() throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, "SELECT :")) {
			stmt.execute();
		}
	}
	
	@Test
	public final void duplicateParam() throws SQLException {
		try (NamedPreparedStatement stmt = new NamedPreparedStatement(connection, "SELECT :param, :param")) {
			stmt.setString("param", "OK");
			try (ResultSet result = stmt.executeQuery()) {
				assertTrue(result.next());
				assertEquals("OK", result.getString(1));
				assertEquals("OK", result.getString(2));
			}
		}
	}
}
