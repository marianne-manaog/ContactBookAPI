package com.qa.contactbookapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerInvalidContactExceptionHandler {

	@ExceptionHandler(value = { InvalidContactException.class })
	public ResponseEntity<String> invalidContactExceptions(InvalidContactException ice) {
		return new ResponseEntity<String>(ice.getMessage(), HttpStatus.NOT_FOUND);
	}
}
