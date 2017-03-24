package de.unipassau.prassefe.sepintro.model.repository.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import de.unipassau.prassefe.sepintro.model.Address;
import de.unipassau.prassefe.sepintro.model.User;
import de.unipassau.prassefe.sepintro.model.repository.UserRepository;
import de.unipassau.prassefe.sepintro.util.NamedPreparedStatement;

/**
 * Sql user repository.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class SqlUserRepository extends AbstractRepository<User, Integer> implements UserRepository {

    /**
     * Create new sql user repository.
     */
    public SqlUserRepository() {
        super("users");
    }

    @Override
    public Optional<User> getById(Integer id) {
        return queryFirst("SELECT * FROM users WHERE id = :id", stmt -> {
            stmt.setInt("id", id);
        });
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
        nonQuerySingle(
                "INSERT INTO users (username, passwordhash, realname, birthday, salt, street, street_nr, zipcode, city, country)"
                + " VALUES (:username, :password, :realname, :birthday, :salt, :street, :streetnr, :zipcode, :city, :country)",
                stmt -> populateStatement(stmt, value), rs -> rs.getInt(1)).ifPresent(value::setId);
    }

    @Override
    public Optional<User> getByUserName(String username) {
        return queryFirst("SELECT * FROM users WHERE username = :username", stmt -> {
            stmt.setString("username", username);
        });
    }

    @Override
    protected User toItem(ResultSet result) throws SQLException {
        Address address = new Address(result.getString("street"), result.getInt("street_nr"),
                result.getString("zipcode"), result.getString("city"), result.getString("country"));
        return new User(result.getInt("id"), result.getString("username"), result.getBytes("passwordhash"),
                result.getString("realname"), result.getDate("birthday").toLocalDate(), address,
                result.getBytes("salt"));
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
}
