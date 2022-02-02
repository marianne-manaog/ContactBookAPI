package com.qa.contactbookapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerDuplicateContactExceptionHandler {

	@ExceptionHandler(value = { DuplicateContactException.class })
	public ResponseEntity<String> userNotFoundExceptions(DuplicateContactException unfe) {
		return new ResponseEntity<String>(unfe.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
