package com.sample.webclassroom.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FieldEmptyException extends RuntimeException {
	
	public FieldEmptyException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		// TODO Auto-generated method stub
		return this;
	}
	
}
