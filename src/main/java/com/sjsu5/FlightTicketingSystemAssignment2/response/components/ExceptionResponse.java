package com.sjsu5.FlightTicketingSystemAssignment2.response.components;

import org.springframework.stereotype.Component;

@Component
public class ExceptionResponse {
	
	
	private int code;
	private String msg;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
