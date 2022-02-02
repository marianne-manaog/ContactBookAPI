package com.qa.contactbookapi.exceptions;

import javax.persistence.EntityNotFoundException;

public class InvalidContactException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public InvalidContactException(String message) {
		super(message);
	}

}
