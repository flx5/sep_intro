package sep_intro.model.repository.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import sep_intro.model.Address;
import sep_intro.model.User;
import sep_intro.model.repository.UserRepository;

public class SqlUserRepository extends AbstractRepository<User, Integer> implements UserRepository {
	public SqlUserRepository() {
		super("users");
	}

	@Override
	public User getById(Integer id) {
		return queryFirstOrDefault("SELECT * FROM users WHERE id = :id", stmt -> {
			stmt.setInt("id", id);
		}, null);
	}

	@Override
	public void update(User value) {
		nonQuery("UPDATE users SET username = :username, passwordhash = :password" + ", realname = :realname, "
				+ "birthday = :birthday, salt = :salt, street = :street, street_nr = :streetnr, zipcode = :zipcode, "
				+ "city = :city, country = :country WHERE id = :id", stmt -> {
					stmt.setInt("id", value.getId());
					populateStatement(stmt, value);
				});
	}

	private void populateStatement(NamedPreparedStatement stmt, User value) throws SQLException {
		stmt.setString("username", value.getUserName());
		stmt.setBytes("password", value.getPasswordHash());
		stmt.setString("realname", value.getRealName());
		stmt.setDate("birthday", value.getBirthday());
		stmt.setBytes("salt", value.getSalt());
		stmt.setString("street", value.getAddress().getStreet());
		stmt.setInt("streetnr", value.getAddress().getStreetNumber());
		stmt.setString("zipcode", value.getAddress().getZipcode());
		stmt.setString("city", value.getAddress().getCity());
		stmt.setString("country", value.getAddress().getCountry());
	}

	@Override
	public void insert(User value) {
		nonQuery("INSERT INTO users (username, passwordhash, realname, birthday, salt, street, street_nr, zipcode, city, country)"
				+ " VALUES (:username, :password, :realname, :birthday, :salt, :street, :streetnr, :zipcode, :city, :country)",
				stmt -> populateStatement(stmt, value));
	}

	@Override
	public User getByUserName(String username) {
		return queryFirst("SELECT * FROM users WHERE username = :username", stmt -> {
			stmt.setString("username", username);
		});
	}

	@Override
	protected User toItem(ResultSet result) {
		Address address;
		try {
			address = new Address(result.getString("street"), result.getInt("street_nr"), result.getString("zipcode"),
					result.getString("city"), result.getString("country"));
			return new User(result.getInt("id"), result.getString("username"), result.getBytes("passwordhash"),
					result.getString("realname"), result.getDate("birthday").toLocalDate(), address,
					result.getBytes("salt"));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteById(Integer id) {
		nonQuery("DELETE FROM users WHERE id = :id", x -> x.setInt("id", id));
	}

	@Override
	public void deleteByUsername(String username) {
		nonQuery("DELETE FROM users WHERE username = :username", x -> x.setString("username", username));
	}

	@Override
	public void delete(User value) {
		deleteById(value.getId());
	}

	@Override
	public void create() {
		nonQuery("CREATE TABLE users (" + "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"
				+ "username VARCHAR(20) NOT NULL UNIQUE," + "passwordhash VARBINARY(" + User.HASH_SIZE + ") NOT NULL,"
				+ "realname VARCHAR(20) NOT NULL," + "birthday DATE NOT NULL," + "salt BINARY(" + User.SALT_SIZE
				+ ") NOT NULL," + "street VARCHAR(20)," + "street_nr INTEGER," + "zipcode VARCHAR(10),"
				+ "city VARCHAR(20)," + "country VARCHAR(20)" + ")");
	}
}
