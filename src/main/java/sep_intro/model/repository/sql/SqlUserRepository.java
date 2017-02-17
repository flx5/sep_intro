package sep_intro.model.repository.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
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
		return queryFirstOrDefault("SELECT * FROM users WHERE id = ?", stmt -> {
			stmt.setInt(1, id);
		}, null);
	}

	@Override
	public void update(User value) {
		// TODO Should add param names
		nonQuery("UPDATE users SET username = ?, passwordhash = ?, realname = ?, "
				+ "birthday = ?, salt = ?, street = ?, street_nr = ?, zipcode = ?, "
				+ "city = ?, country = ? WHERE id = ?", stmt -> {
					stmt.setInt(11, value.getId());
					populateStatement(stmt, value);
				});
	}

	private void populateStatement(PreparedStatement stmt, User value) throws SQLException {
		stmt.setString(1, value.getUserName());
		stmt.setBytes(2, value.getPasswordHash());
		stmt.setString(3, value.getRealName());
		stmt.setDate(4, Date.valueOf(value.getBirthday()));
		stmt.setBytes(5, value.getSalt());
		stmt.setString(6, value.getAddress().getStreet());
		stmt.setInt(7, value.getAddress().getStreetNumber());
		stmt.setString(8, value.getAddress().getZipcode());
		stmt.setString(9, value.getAddress().getCity());
		stmt.setString(10, value.getAddress().getCountry());
	}

	@Override
	public void insert(User value) {
		nonQuery("INSERT INTO users (username, passwordhash, realname, "
				+ "birthday, salt, street, street_nr, zipcode, " + "city, country) VALUES (?, ?, ?,?,?,?,?,?,?,?)",
				stmt -> populateStatement(stmt, value));
	}

	@Override
	public User getByUserName(String username) {
		return queryFirst("SELECT * FROM users WHERE username = ?", stmt -> {
			stmt.setString(1, username);
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
		nonQuery("DELETE FROM users WHERE id = ?", x -> x.setInt(1, id));
	}

	@Override
	public void deleteByUsername(String username) {
		nonQuery("DELETE FROM users WHERE username = ?", x -> x.setString(1, username));
	}

	@Override
	public void delete(User value) {
		deleteById(value.getId());
	}

	// TODO Validate input lengths!
	@Override
	public void create() {
		nonQuery("CREATE TABLE users (" + "id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,"
				+ "username VARCHAR(20) NOT NULL UNIQUE," + "passwordhash VARBINARY(" + User.HASH_SIZE + ") NOT NULL,"
				+ "realname VARCHAR(20) NOT NULL," + "birthday DATE NOT NULL," + "salt BINARY(" + User.SALT_SIZE
				+ ") NOT NULL," + "street VARCHAR(20)," + "street_nr INTEGER," + "zipcode VARCHAR(10),"
				+ "city VARCHAR(20)," + "country VARCHAR(20)" + ")");
	}
}
