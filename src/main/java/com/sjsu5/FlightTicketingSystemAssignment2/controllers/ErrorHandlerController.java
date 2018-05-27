package com.sjsu5.FlightTicketingSystemAssignment2.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sjsu5.FlightTicketingSystemAssignment2.response.components.BadRequest;
import com.sjsu5.FlightTicketingSystemAssignment2.response.components.ExceptionResponse;
import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.GenericException;
import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.ResException;

@ControllerAdvice
public class ErrorHandlerController {
	
	/**
	 * Generic Exception Handler that will catch Generic Exception
	 * @param e Exception
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(GenericException.class)
	public ResponseEntity<BadRequest> genericException(GenericException e) throws Exception{
		BadRequest bex=new BadRequest();
		ExceptionResponse ex=new ExceptionResponse();
		ex.setCode(e.getHttpStatus().value());;
		ex.setMsg(e.getMessage());
		bex.setBadRequest(ex);
		return new ResponseEntity<BadRequest>(bex,e.getHttpStatus());
	}
	
	/**@ExceptionHandler(ResException.class)
	public ResponseEntity<BadRequest> resException(ResException e) throws Exception {
		BadRequest bex=new BadRequest();
		ExceptionResponse ex=new ExceptionResponse();
		ex.setCode(e.getHttpStatus().value());;
		ex.setMsg(e.getMessage());
		bex.setBadRequest(ex);
		return new ResponseEntity<BadRequest>(bex,e.getHttpStatus());
	}*/
}
