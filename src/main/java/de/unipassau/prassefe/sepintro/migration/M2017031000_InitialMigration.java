package de.unipassau.prassefe.sepintro.migration;

import java.sql.SQLException;

import de.unipassau.prassefe.sepintro.model.User;
import de.unipassau.prassefe.sepintro.util.SQLUtil;

public class M2017031000_InitialMigration extends SqlMigration {

	@Override
	public void up(SQLUtil sqlUtil) throws SQLException {	
		sqlUtil.nonQuery("CREATE TABLE users (" + "id INTEGER NOT NULL PRIMARY KEY,"
				+ "username VARCHAR(20) NOT NULL UNIQUE," + "passwordhash VARBINARY(" + User.HASH_SIZE + ") NOT NULL,"
				+ "realname VARCHAR(20) NOT NULL," + "birthday DATE NOT NULL," + "salt BINARY(" + User.SALT_SIZE
				+ ") NOT NULL," + "street VARCHAR(20)," + "street_nr INTEGER," + "zipcode VARCHAR(10),"
				+ "city VARCHAR(20)," + "country VARCHAR(20)" + ")");
		
		sqlUtil.createPrimaryKey("users", "id");
	}

	@Override
	public void down(SQLUtil sqlUtil) throws SQLException {
		sqlUtil.nonQuery("DROP TABLE users");
	}

}
