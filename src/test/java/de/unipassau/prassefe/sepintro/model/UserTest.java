package de.unipassau.prassefe.sepintro.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.unipassau.prassefe.sepintro.model.User;

public class UserTest {

	private User user;
	
	@Before
	public void setUp() throws Exception {
		this.user = new User("name", "Password");
	}

	@Test
	public void testVerifyPassword() {
		assertTrue(this.user.verifyPassword("Password"));
		assertFalse(this.user.verifyPassword("notPassword"));
		assertFalse(this.user.verifyPassword("password"));
	}
}