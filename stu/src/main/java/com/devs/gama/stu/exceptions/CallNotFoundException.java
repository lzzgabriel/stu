package com.devs.gama.stu.exceptions;

public class CallNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CallNotFoundException() {
		super();
	}
	
	public CallNotFoundException(String message) {
		super(message);
	}

}
