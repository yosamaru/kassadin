package com.yosamaru.kassadin;

public class KassadinException extends Exception {
	private static final long serialVersionUID = -1751212777552261218L;

	public KassadinException() {

	}

	public KassadinException(final String message) {
		super(message);
	}

	public KassadinException(final Throwable cause) {
		super(cause);
	}

	public KassadinException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
