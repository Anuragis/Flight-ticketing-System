package com.sjsu5.FlightTicketingSystemAssignment2.services;

import org.springframework.http.HttpStatus;

import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.GenericException;

public class ExceptionThrower {
	
	/**
	 * 
	 * Central method to collect all error messages
	 * @param httpStatus
	 * @param message
	 * @throws GenericException
	 */
	public void throwGenericException(HttpStatus httpStatus,String message) throws GenericException{
		GenericException ex=new GenericException();
		ex.setHttpStatus(httpStatus);
		ex.setMessage(message);
		throw ex;
	}

}
