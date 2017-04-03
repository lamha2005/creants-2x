package com.creants.creants_2x.socket.exception;

/**
 * @author LamHM
 *
 */
public class CASCodecException extends CASException {
	private static final long serialVersionUID = 1L;

	public CASCodecException() {
	}

	public CASCodecException(final String message) {
		super(message);
	}

	public CASCodecException(final Throwable t) {
		super(t);
	}
}
