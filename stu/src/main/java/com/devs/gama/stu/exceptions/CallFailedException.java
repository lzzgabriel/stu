package com.devs.gama.stu.exceptions;

public class CallFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CallFailedException() {
		super();
	}

	public CallFailedException(String msg) {
		super(msg);
	}

}
