package com.example.techverse.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.techverse.DTO.ErrorResponse;

 
@ControllerAdvice
public class GlobalExceptionHandler {
	
	
	
	
	/* @ExceptionHandler(BadCredentialsException.class)
     public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
         // Create a custom error response
    	 System.out.println("bghghjgjhgjhgjhghjg"+ex);
         ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage());
         return new ResponseEntity<>(errorResponse, HttpStatus.OK);
     }
	 */
	
	 @ExceptionHandler(ProductException.class)
	    public ResponseEntity<Object> handleProductException(ProductException ex) {
	        // Create a custom error response
	    	 System.out.println(ex.getMessage());
	        ErrorResponse errorResponse = new ErrorResponse(false, "product not found");
	        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
	    }
	 
	   @ExceptionHandler(OrderException.class)
	    public ResponseEntity<Object> handleOrderException(OrderException ex) {
	        // Create a custom error response
	    	 System.out.println(ex.getMessage());
	        ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage());
	        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
	    }
     @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> handleUserException(UserException ex) {
        // Create a custom error response
    	 System.out.println(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, "user not found");
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    
     @ExceptionHandler(IOException.class)
     public ResponseEntity<Object> handleIO(IOException ex) {
         // Create a custom error response
     	 System.out.println(ex.getMessage());
         ErrorResponse errorResponse = new ErrorResponse(false, "file storage error");
         return new ResponseEntity<>(errorResponse, HttpStatus.OK);
     }
     
     
     
     
   /*  @ExceptionHandler(UsernameNotFoundException.class)
     public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
         // Create a custom error response
         ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage());
         return new ResponseEntity<>(errorResponse, HttpStatus.OK);
     }
    
     */
     
   /*  @ExceptionHandler(Exception.class)
     public ResponseEntity<Object> handleBadCredentialsException( Exception ex) {
         // Create a custom error response
    	 ex.
    	 System.out.println("hello"+ex);
         ErrorResponse errorResponse = new ErrorResponse(false, ex.getMessage());
         return new ResponseEntity<>(errorResponse, HttpStatus.OK);
     }
     
     */
    // Add more @ExceptionHandler methods for other custom exceptions

    // Generic exception handler
  /*  @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        // Create a generic error response
        ErrorResponse errorResponse = new ErrorResponse(false, "Internal Server Error123");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}