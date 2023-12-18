package com.example.techverse.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler{
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) ->{
	 		String fieldName = ((FieldError) error).getField();
	  		String message = error.getDefaultMessage();
			 if(fieldName.equals("password"))
		 	{
		 		 message="Password must contain ";
		 		 		//"1 Capital Letter,1 Small Letter, 1 Special Symbol and maximun 12 character and min 8";
		 		 String pwd=ex.getFieldValue(fieldName)+"";
		 		
		 		 if(!pwd.matches(".*[A-Z].*"))
		 			message=message + ",1 Capital Letter";
		 		  if(!pwd.matches(".*[a-z].*"))
		 			message=message + ", 1 small Letter";
		 		  if(!pwd.matches(".*[0-9].*"))
		 			message=message + ", 1 numeric";
		 		  if(!pwd.matches(".*[@#$%^&+=].*"))
				 	message=message + ", 1 Special character";
		 		  if(pwd.length()<8 || pwd.length()>12)
		 			message=message + " Minimum 8 character and maximum 12 character size";
		 			  
		 	
		 	}
		 	 
			errors.put(fieldName, message);
		});
		Map<String, String> responseBody = new HashMap<String, String>();
		responseBody.put("success", "false");
		responseBody.put("message", "Username Allready Exist");


		
		return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
	}
	 
}