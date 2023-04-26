package com.pms.exception;

public class AppointmentServiceException extends Exception {
	private static final long serialVersionUID = 526706541554094375L;

	public AppointmentServiceException(String message) {
		super(message);
	}

	public AppointmentServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppointmentServiceException(Throwable cause) {
		super(cause);
	}

}
