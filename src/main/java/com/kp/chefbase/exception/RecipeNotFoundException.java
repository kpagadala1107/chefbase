package com.kp.chefbase.exception;

public class RecipeNotFoundException extends RuntimeException {

	public RecipeNotFoundException() {
		
	}
	
	public RecipeNotFoundException(String exception) {
		super(exception);
 	}
}
