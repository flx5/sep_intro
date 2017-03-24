package de.unipassau.prassefe.sepintro.model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import de.unipassau.prassefe.sepintro.util.StringUtil;

/**
 * User bean.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public class User {

    /**
     * Salt size.
     */
    public static final int SALT_SIZE = 32;
    /**
     * Hash size.
     */
    public static final int HASH_SIZE = 256;

    private static final int PBKDF2_ITERATIONS = 64000;

    private int id;
    private String userName;
    private byte[] passwordHash;
    private String realName;
    private LocalDate birthday;
    private Address address;

    private byte[] salt;

    /**
     * Create new user.
     *
     * @param userName Username.
     * @param plainPassword Password.
     */
    public User(String userName, String plainPassword) {
        this.userName = userName;

        this.salt = new byte[SALT_SIZE];
        new SecureRandom().nextBytes(this.salt);

        this.setPassword(plainPassword);

        this.realName = "";
        this.setBirthday(LocalDate.now());
        this.address = new Address();
    }

    /**
     * Create new user.
     *
     * @param id The id.
     * @param userName The username.
     * @param passwordHash The hashed password.
     * @param realName The real username.
     * @param birthday The birthday.
     * @param address The address.
     * @param salt The salt for hashing the password.
     */
    public User(int id, String userName, byte[] passwordHash, String realName, LocalDate birthday, Address address,
            byte[] salt) {
        this.id = id;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.realName = realName;
        this.birthday = birthday;
        this.address = address;
        this.salt = salt;
    }

    /**
     * Get the username.
     *
     * @return The username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Set the username.
     *
     * @param userName The username.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Set the password.
     *
     * @param password The password.
     */
    public void setPassword(String password) {
        this.passwordHash = hashPassword(password);
    }

    /**
     * Getter for satisfying jsf.
     *
     * @return {@link StringUtil.EMPTY}
     */
    public String getPassword() {
        // required by JSF...
        return StringUtil.EMPTY;
    }

    /**
     * Get the hashed password.
     *
     * @return Password hash.
     */
    public byte[] getPasswordHash() {
        return this.passwordHash;
    }

    /**
     * Get the real name.
     *
     * @return realname.
     */
    public String getRealName() {
        return realName;
    }

    /**
     * Set the real name.
     *
     * @param realName Real name.
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * Get the address.
     *
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Get the birthday.
     *
     * @return the birthday
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * Get the salt.
     *
     * @return The salt.
     */
    public byte[] getSalt() {
        return this.salt;
    }

    /**
     * Validate the password against stored password.
     *
     * @param givenPassword Passwort to test.
     * @return True on success.
     */
    public boolean verifyPassword(String givenPassword) {
        return Arrays.equals(hashPassword(givenPassword), this.passwordHash);
    }

    private byte[] hashPassword(final String password) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), this.salt, PBKDF2_ITERATIONS, HASH_SIZE);
            SecretKey key = skf.generateSecret(spec);
            return key.getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SecurityException(e);
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return false;
        }

        User other = (User) obj;
        return this.userName.equals(other.userName);
    }
}
