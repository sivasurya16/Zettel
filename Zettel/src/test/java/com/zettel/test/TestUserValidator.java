package com.zettel.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.zettel.usermanager.UserValidator;

class TestUserValidator {
	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Initializing UserValidator Object");
	}

	@Test
	void testIsValidUsername() {
		assertFalse(UserValidator.isValidUsername(null));
		assertTrue(UserValidator.isValidUsername("user1"));
	}

	@Test
	void testIsStrongPassword() {
		assertFalse(UserValidator.isStrongPassword(null));
		assertFalse(UserValidator.isStrongPassword("123"));
		assertTrue(UserValidator.isStrongPassword("asj@12873zxkjhq98wkjhds"));
	}

	@Test
	void formatUsername() {
		assertEquals("ADMIN 123", UserValidator.formatUsername("Admin 123"));
		assertThrows(IllegalArgumentException.class, () -> {
			UserValidator.formatUsername(null);
		});
	}

	@AfterAll
	public static void setUpAfterClass() {
		System.out.println("Test Completed");
	}

}
