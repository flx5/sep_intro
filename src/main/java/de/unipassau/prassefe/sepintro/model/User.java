package de.unipassau.prassefe.sepintro.model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User {
	public static final int SALT_SIZE = 32;
	public static final int HASH_SIZE = 256;
	private static final int PBKDF2_ITERATIONS = 64000;

	private int id;
	private String userName;
	private byte[] passwordHash;
	private String realName;
	private LocalDate birthday;
	private Address address;

	private byte[] salt;

	public User(String userName, String plainPassword) {
		this.userName = userName;
		
		this.salt = new byte[SALT_SIZE];
		new SecureRandom().nextBytes(this.salt);
		
		this.setPassword(plainPassword);

		this.realName = "";
		this.setBirthday(LocalDate.now());
		this.address = new Address();
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.passwordHash = hashPassword(password);
	}
	
	public String getPassword() {
		// required by JSF...
		return "";
	}
	
	public byte[] getPasswordHash() {
		return this.passwordHash;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
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
	
	public byte[] getSalt() {
		return this.salt;
	}

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
			throw new RuntimeException(e);
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
}
