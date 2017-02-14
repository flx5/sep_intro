package sep_intro;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User {
	private static final int SALT_SIZE = 24;
	private static final int HASH_SIZE = 18;
	private static final int PBKDF2_ITERATIONS = 64000;

	private String userName;
	private byte[] passwordHash;
	private String realName;
	private Date dateOfBirth;

	private byte[] salt;

	// TODO Address

	public User(String userName, String plainPassword) {
		this.userName = userName;
		
		this.salt = new byte[SALT_SIZE];
		new SecureRandom().nextBytes(this.salt);
		
		this.setPassword(plainPassword);
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public boolean verifyPassword(String givenPassword) {
		return Arrays.equals(hashPassword(givenPassword), this.passwordHash);
	}

	private byte[] hashPassword(final String password) {

		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), this.salt, PBKDF2_ITERATIONS, HASH_SIZE);
			SecretKey key = skf.generateSecret(spec);
			byte[] res = key.getEncoded();
			return res;

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}
}
