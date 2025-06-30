package com.zettel.regression;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import com.zettel.usermanager.User;


class TestUser {
	static User u;
	@BeforeEach
	public void setUpBeforeClass() {
		System.out.println("Initializing User Object");
		u = new User("Ram","12345678");
	}
	
	@RepeatedTest(5)
	void testInvalidUser() {
		assertThrows(IllegalArgumentException.class , ()-> {new User("","");});
		
	}
	
	@Test
	void testAuthenticate() {
		assertFalse(u.authenticate("akjdsfhlkjawher"));
		assertTrue(u.authenticate("12345678"));
	}

	@Test
	void testChangePassword() {
		
		assertFalse(u.changePassword("incorrectpass", "asdfasdf183287"));
		assertFalse(u.changePassword("12345678", "123"));
		assertFalse(u.changePassword("12345678", "12345678"));
		assertTrue(u.changePassword("12345678", "asdfasdf183287"));
	}
	
	@Test
	void testFail() {
		fail("This is failed test case");
	}
	
	@AfterAll
	public static void setUpAfterClass() {
		System.out.println("Test Completed");
	}

}
