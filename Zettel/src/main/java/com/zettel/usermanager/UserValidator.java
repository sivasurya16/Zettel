package com.zettel.usermanager;

public class UserValidator {
	public static boolean isValidUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			return false;
		}

		return true;
	}

	public static boolean isStrongPassword(String password) {
		if (password == null || password.length() < 8) {
			return false;
		}
		return true;
	}

	public static String formatUsername(String username) {
		if (username == null) {
			throw new IllegalArgumentException("Username cannot be null");
		}

		return username.toUpperCase();
	}
}
