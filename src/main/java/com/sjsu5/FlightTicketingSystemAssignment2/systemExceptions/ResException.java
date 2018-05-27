package com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResException extends NullPointerException {
	/**
	 * Collection class for all custom messages
	 */
	private static final long serialVersionUID = -841009383514264961L;
	private String message;
	public ResException() {
		
	}
	public ResException(String message) {
		super();
		this.message = message;
	}
	private HttpStatus httpStatus;
	
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
