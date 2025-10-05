package com.kp.chefbase.exception;

public class UserNotFoundException extends RuntimeException {
	
	public UserNotFoundException() {
		
	}
	
	public UserNotFoundException(String exception) {
		super(exception);
	}

}
