package sep_intro.model.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import sep_intro.util.EnumUtil;

public class DatabaseConfig {
	enum DbType {
		MySql;
		
		@Override
		public String toString() {
			return name().toLowerCase();
		}
	}
	
	private final DbType type;
	private final String host;
	private final String name;
	private final String user;
	private final String password;
	
	public DatabaseConfig(Properties properties) {
		this.host = properties.getProperty("dbHost");
		this.name = properties.getProperty("dbName");
		this.user = properties.getProperty("dbUser");
		this.password = properties.getProperty("dbPassword");
		this.type = EnumUtil.lookup(DbType.class, properties.getProperty("dbType"));
	}
	
	private String getUrl() {
		return "jdbc:"+ type + "://" + host + "/" + name;
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(getUrl(), user, password);
	}
}
