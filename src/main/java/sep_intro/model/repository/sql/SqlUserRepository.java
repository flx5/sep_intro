package sep_intro.model.repository.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import sep_intro.model.Address;
import sep_intro.model.User;
import sep_intro.model.repository.UserRepository;

public class SqlUserRepository extends AbstractRepository<User, Integer> implements UserRepository {

	@Override
	public User getById(Integer id) {
		User user = getByIdOrDefault(id);

		if (user == null) {
			throw new IllegalStateException("No such user");
		}

		return user;
	}

	@Override
	public User getByIdOrDefault(Integer id) {
		return queryFirstOrDefault("SELECT * FROM users WHERE id = ?", stmt -> {
			stmt.setInt(1, id);
		}, null);
	}

	@Override
	public void update(User value) {
		// TODO Try passing array
		nonQuery("UPDATE users SET username = ?, passwordhash = ?, realname = ?, "
				+ "birthday = ?, salt = ?, street = ?, street_nr = ?, zipcode = ?, "
				+ "city = ?, country = ?",
				stmt -> populateStatement(stmt, value));
	}
	
	private void populateStatement(PreparedStatement stmt, User value) throws SQLException {
		stmt.setString(1, value.getUserName());
		stmt.setBytes(2, value.getPasswordHash());
		stmt.setString(3, value.getRealName());
		stmt.setObject(4, value.getBirthday());
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
				+ "birthday, salt, street, street_nr, zipcode, "
				+ "city, country) VALUES (?, ?, ?,?,?,?,?,?,?,?)",
				stmt -> populateStatement(stmt, value));
	}

	@Override
	public User getByUserName(String username) {
		return queryFirstOrDefault("SELECT * FROM users WHERE username = ?", stmt -> {
			stmt.setString(1, username);
		}, null);
	}

	@Override
	protected User toItem(ResultSet result) {
		Address address;
		try {
			address = new Address(result.getString("street"), result.getInt("street_nr"), result.getString("zipcode"),
					result.getString("city"), result.getString("country"));
			return new User(result.getInt("id"), result.getString("username"), result.getBytes("passwordhash"),
					result.getString("realname"), (LocalDate)result.getObject("birthday"), address, result.getBytes("salt"));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
