package com.sjsu5.FlightTicketingSystemAssignment2.response.components;

import org.springframework.stereotype.Component;

@Component
public class Response {
	SuccessResponse response;

	public SuccessResponse getResponse() {
		return response;
	}

	public void setResponse(SuccessResponse response) {
		this.response = response;
	}
	
}
