package com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GenericException extends Exception{
	
	/**
	 * Central class for collecting all exceptions
	 */
	private static final long serialVersionUID = -759490189402461704L;
	private HttpStatus httpStatus;
	private String message;
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
