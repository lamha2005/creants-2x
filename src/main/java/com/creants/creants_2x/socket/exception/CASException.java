package com.creants.creants_2x.socket.exception;

public class CASException extends Exception {
	private static final long serialVersionUID = 6052949605652105170L;

	public CASException() {
	}

	public CASException(final String message) {
		super(message);
	}

	public CASException(final Throwable t) {
		super(t);
	}

}
