package com.zettel.parameterized;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.zettel.usermanager.User;

class TestUser {
	static User u;

	@BeforeEach
	public void setUpBeforeClass() {
		System.out.println("Initializing User Object");
		u = new User("Ram", "12345678");
	}

	@ParameterizedTest
	@ValueSource(strings = { "", " " })
	void testInvalidUsername(String username) {
		assertThrows(IllegalArgumentException.class, () -> {
			new User(username, "12345678");
		});

	}

	@ParameterizedTest
	@ValueSource(strings = { "", " ", "123" })
	void testInvalidPassword(String password) {
		assertThrows(IllegalArgumentException.class, () -> {
			new User("ram", password);
		});

	}

	@ParameterizedTest
	@CsvSource({ "akjdsfhlkjawher,false", "12345678,true" })
	void testAuthenticate(String password, boolean expected) {
		assertEquals(expected, u.authenticate(password));
	}

	@ParameterizedTest
	@CsvSource({ "incorrectpass,asdfasdf183287,false", "12345678,123,false", "12345678,12345678,false",
			"12345678,newpassword,true" })
	void testChangePassword(String oldPass, String newPass, boolean expected) {
		assertEquals(expected, u.changePassword(oldPass, newPass));
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
