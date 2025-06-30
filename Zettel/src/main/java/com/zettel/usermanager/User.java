package com.zettel.usermanager;

public class User {
	public String username;
	public String password;
	public User(String username, String password) {
		this.username = username;
		if (!UserValidator.isValidUsername(username)) {
			throw new IllegalArgumentException("Username is not valid");
		}
		this.password = password;
	}
	
	public boolean authenticate(String pass) {
		return pass.equals(password);
	}
	
	public boolean changePassword(String oldPass, String newPass) {
		if (!authenticate(oldPass)) 
			return false;
		
		if (newPass.equals(oldPass))
			return false;
		
		if (!UserValidator.isStrongPassword(newPass))
			return false;
		
		password = newPass;
		return true;
	}
	
}
