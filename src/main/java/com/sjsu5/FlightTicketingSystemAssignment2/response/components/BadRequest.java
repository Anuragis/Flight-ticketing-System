package com.sjsu5.FlightTicketingSystemAssignment2.response.components;

import org.springframework.stereotype.Component;

@Component
public class BadRequest {
	private ExceptionResponse BadRequest;

	public ExceptionResponse getBadRequest() {
		return BadRequest;
	}

	public void setBadRequest(ExceptionResponse BadRequest) {
		this.BadRequest = BadRequest;
	}
	
}
