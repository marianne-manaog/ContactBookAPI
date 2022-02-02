package com.qa.contactbookapi.exceptions;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateContactException extends DuplicateKeyException {

	private static final long serialVersionUID = 1L;

	public DuplicateContactException(String message) {
		super(message);
	}

}
