package com.sjsu5.FlightTicketingSystemAssignment2.controllers;


import java.text.ParseException;

import java.util.List;
import java.util.Map;


import javax.management.relation.RelationNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Flight;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Plane;
import com.sjsu5.FlightTicketingSystemAssignment2.response.components.Response;
import com.sjsu5.FlightTicketingSystemAssignment2.response.components.SuccessResponse;
import com.sjsu5.FlightTicketingSystemAssignment2.services.ExceptionThrower;
import com.sjsu5.FlightTicketingSystemAssignment2.services.FlightService;
import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.GenericException;
import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.ResException;




@RestController
public class FlightController {
	public static boolean fromFlight=false;
	@Autowired
	private FlightService flightService;
	
	
	/**
	 * This is get all flights service
	 * @return List of all flights
	 * @throws GenericException
	 */
	@RequestMapping(value="/flights", method = RequestMethod.GET,
			produces = { "application/json", "application/xml" })
	public ResponseEntity<List<Flight>> getAllFights() throws GenericException{
		fromFlight=true;
		try {
			return new ResponseEntity<List<Flight>>( flightService.getAllFlights(),HttpStatus.OK );
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null;
	}
	
	/**
	 * This is get details of a flight based on particular id
	 * @param number, Flight number to fetch flight details
	 * @return
	 * @throws GenericException
	 */
	@RequestMapping(value="/flight/{number}", method = RequestMethod.GET,
			produces = { "application/json", "application/xml" })
	public ResponseEntity<Flight> getFlight(@PathVariable String number) throws GenericException {
		try {
			return new ResponseEntity<Flight>(flightService.getFlight(number),HttpStatus.OK);
		}catch (RelationNotFoundException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.NOT_FOUND, "Sorry, the requested flight with number: "+ number +" does not exist");
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		
		return null;
	}
	
	
	/**
	 * This is a post request to create a new flight
	 * @param flightNumber
	 * @param requestParams
	 * @return Details of a particular flight by id
	 * @throws GenericException
	 * @throws ParseException
	 */
	@RequestMapping(value="/flight/{flightNumber}", method=RequestMethod.POST,produces = { "application/json", "application/xml" })
	public ResponseEntity<Flight> addFlight(@PathVariable String flightNumber,@RequestParam Map<String,String> requestParams) throws GenericException, ParseException
	{
		try {
				
		Plane plane=new Plane();
			plane.setCapacity(Integer.parseInt(requestParams.get("capacity")))
							 .setManufacturer(requestParams.get("manufacturer"))
							 .setModel(requestParams.get("model"))
							 .setYear(Integer.parseInt(requestParams.get("year")));	
			Flight flight=new Flight();
			
			flight.setFlightnumber(flightNumber)
					.setPrice(Double.parseDouble(requestParams.get("price")))
					.setOrigin(requestParams.get("origin"))
					.setDestination(requestParams.get("to"))
					.setArrivalTime(requestParams.get("arrivalTime"))
					.setDepartureTime(requestParams.get("departureTime"))
					.setDescription(requestParams.get("description"))
					.setSeatsLeft(Integer.parseInt(requestParams.get("capacity")))
					.setPlane(plane);
					
		flightService.addNewFlight(flight);
		return new ResponseEntity<Flight>(flight,HttpStatus.CREATED);
		}catch (DataIntegrityViolationException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, "another passenger with the same number '"+ requestParams.get("phone")+ "' already exists");
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		System.out.print(e.getStackTrace());
		}
		return null; 
		
	}
	
	
	/**
	 * This is a PUT call to fully update an existing flight object
	 * @param flightNumber
	 * @param requestParams
	 * @return Newly created flight object
	 * @throws GenericException
	 * @throws ParseException
	 */
	@RequestMapping(value="/flight/{flightNumber}", method=RequestMethod.PUT,produces = { "application/json", "application/xml" })
	public ResponseEntity<Flight> updateFlight(@PathVariable String flightNumber,@RequestParam Map<String,String> requestParams) throws GenericException, ParseException
	{
		try {	
		Plane plane=new Plane();
			plane.setCapacity(Integer.parseInt(requestParams.get("capacity")))
							 .setManufacturer(requestParams.get("manufacturer"))
							 .setModel(requestParams.get("model"))
							 .setYear(Integer.parseInt(requestParams.get("year")));	
			Flight flight=new Flight();
			
			flight.setFlightnumber(flightNumber)
					.setPrice(Double.parseDouble(requestParams.get("price")))
					.setOrigin(requestParams.get("origin"))
					.setDestination(requestParams.get("to"))
					.setArrivalTime(requestParams.get("arrivalTime"))
					.setDepartureTime(requestParams.get("departureTime"))
					.setDescription(requestParams.get("description"))
					.setPlane(plane);
					
		flightService.updateFlight(flight);
		return new ResponseEntity<Flight>(flightService.getFlight(flightNumber),HttpStatus.CREATED);
		}catch (DataIntegrityViolationException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, "another passenger with the same number '"+ requestParams.get("phone")+ "' already exists");
		}catch (ResException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		System.out.print(e.getStackTrace());
		}
		return null; 
		
	}
	
	/**
	 * Delete call to delete an existing flight 
	 * @param flightNumber
	 * @return Success or Error response
	 * @throws GenericException
	 */
	@RequestMapping(value="/airline/{flightNumber}", method=RequestMethod.DELETE,produces = { "application/json", "application/xml" })
	public ResponseEntity<Response> deleteFlight(@PathVariable String flightNumber) throws GenericException {
		try {
			flightService.deleteFlight(flightNumber);
			SuccessResponse succeessResponse=new SuccessResponse(HttpStatus.OK.value(),"Flight with id "+ flightNumber +" is deleted successfully");
			Response Response=new Response();
			Response.setResponse(succeessResponse);
			return new ResponseEntity<Response>(Response, HttpStatus.OK);
		}catch (RelationNotFoundException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.NOT_FOUND, "Sorry, the requested flight with id: "+ flightNumber +" does not exist");
		}catch (ResException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, e.getMessage());
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null;
	}
}