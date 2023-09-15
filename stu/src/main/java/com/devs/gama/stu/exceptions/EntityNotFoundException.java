package com.devs.gama.stu.exceptions;

public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException() {
		super();
	}
	
	public EntityNotFoundException(String message) {
		super(message);
	}
	
}
