package com.sjsu5.FlightTicketingSystemAssignment2.controllers;

import java.util.List;
import java.util.Map;
import javax.management.relation.RelationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Passenger;
import com.sjsu5.FlightTicketingSystemAssignment2.repositories.PassengerRepository;
import com.sjsu5.FlightTicketingSystemAssignment2.response.components.Response;
import com.sjsu5.FlightTicketingSystemAssignment2.response.components.SuccessResponse;
import com.sjsu5.FlightTicketingSystemAssignment2.services.ExceptionThrower;
import com.sjsu5.FlightTicketingSystemAssignment2.services.PassengerService;
import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.GenericException;



@RestController
public class PassengerController {
	
	@Autowired
	private PassengerService personService;
	
	
	/**
	 * GET call to to fecth list of all passengers
	 * @return List of all passengers
	 * @throws GenericException
	 */
	@RequestMapping(value="/passengers", method = RequestMethod.GET,
			produces = { "application/json", "application/xml" })
	public ResponseEntity<List<Passenger>> getPassengers() throws GenericException{
		try {
			final HttpHeaders httpHeaders= new HttpHeaders();
		    httpHeaders.setContentType(MediaType.APPLICATION_XML);
			
				return new ResponseEntity<List<Passenger>>(personService.getAllPersons(),HttpStatus.OK );
			}catch (Exception e) {
				ExceptionThrower thrower=new ExceptionThrower();
				thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
			}
		return null;
		}
	
	/**
	 * GET passenger details based on id
	 * @param id
	 * @return Single Passenger 
	 * @throws GenericException
	 */
	@RequestMapping(value="/passenger/{id}", method = RequestMethod.GET,
			produces = { "application/json", "application/xml" })
	public ResponseEntity<Passenger> getPassenger(@PathVariable String id) throws GenericException {
		try {
			return new ResponseEntity<Passenger>(personService.getPassenger(Integer.parseInt(id)),HttpStatus.OK);
		}catch (RelationNotFoundException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.NOT_FOUND, "Sorry, the requested passenger with id "+ id +" does not exist");
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		
		return null;
	}
	
	/**
	 * UPDATE an existing passenger object fully
	 * @param requestParams
	 * @param id
	 * @return Updated passenger object
	 * @throws GenericException
	 */
	@RequestMapping(value="/passenger/{id}", method=RequestMethod.PUT,produces = { "application/json", "application/xml" })
	public ResponseEntity<Passenger> updatePassenger(@RequestParam Map<String,String> requestParams,@PathVariable String id) throws GenericException{
		try {
		Passenger passenger=new Passenger(requestParams.get("firstname"),requestParams.get("lastname")
					 			,Integer.parseInt(requestParams.get("age")), requestParams.get("gender"), requestParams.get("phone"));
		personService.updatePassenger(Integer.parseInt(id), passenger);
		return new ResponseEntity<Passenger>(personService.getPassenger(Integer.parseInt(id)),HttpStatus.OK);
		}catch (RelationNotFoundException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.NOT_FOUND, "Sorry, the requested passenger with id "+ id +" does not exist and hence cannot be updated");
		}catch (DataIntegrityViolationException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, "Duplicate entry '"+ requestParams.get("phone")+ "' for phone");
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null; 
		
	}
	
	
	/**
	 * DELETE an existing passenger
	 * @param id
	 * @return Success message on deletion
	 * @throws GenericException
	 */
	@RequestMapping(value="/passenger/{id}", method=RequestMethod.DELETE,produces = { "application/json", "application/xml" })
	public ResponseEntity<Response> deletePassenger(@PathVariable String id) throws GenericException {
		try {
			personService.deletePassenger(Integer.parseInt(id));
			SuccessResponse succeessResponse=new SuccessResponse(HttpStatus.OK.value(),"Passenger with id "+ id +" is deleted successfully");
			Response Response=new Response();
			Response.setResponse(succeessResponse);
			return new ResponseEntity<Response>(Response, HttpStatus.OK);
		}catch (RelationNotFoundException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.NOT_FOUND, "Sorry, the requested passenger with id: "+ id +" does not exist");
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		
		return null;
	}
	
	
	/**
	 * POST call to create a new Passenger
	 * @param requestParams
	 * @return Newly created passenger
	 * @throws GenericException
	 */
	@RequestMapping(value="/passenger", method=RequestMethod.POST,produces = { "application/json", "application/xml" })
	public ResponseEntity<Passenger> addPassenger(@RequestParam Map<String,String> requestParams) throws GenericException
	{
		try {
		Passenger passenger=new Passenger(requestParams.get("firstname"),requestParams.get("lastname")
					 			,Integer.parseInt(requestParams.get("age")), requestParams.get("gender"), requestParams.get("phone"));
		int id=personService.addNewPassenger(passenger);
		return new ResponseEntity<Passenger>(personService.getPassenger(id),HttpStatus.CREATED);
		}catch (DataIntegrityViolationException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, "another passenger with the same number '"+ requestParams.get("phone")+ "' already exists");
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null; 
		
	}
	
	
	


}
