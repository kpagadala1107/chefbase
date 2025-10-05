package com.kp.chefbase.exceptionHandler;

import com.kp.chefbase.exception.RecipeNotFoundException;
import com.kp.chefbase.exception.UserNotFoundException;
import com.kp.chefbase.model.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BookMyActivityHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value = RecipeNotFoundException.class)
	public ResponseEntity<ErrorDetails> activityNotFoundHandler(RecipeNotFoundException e) {
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setCode("400");
		errorDetails.setMessage(e.getMessage());
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<ErrorDetails> userNotFoundHandler(UserNotFoundException userNotFoundException){
		ErrorDetails errorDetails = new ErrorDetails();
		errorDetails.setCode("400");
		errorDetails.setMessage(userNotFoundException.getMessage());
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
	}

}
