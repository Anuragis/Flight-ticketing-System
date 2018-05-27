package com.sjsu5.FlightTicketingSystemAssignment2.controllers;

import java.util.List;
import java.util.Map;

import javax.management.relation.RelationNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sjsu5.FlightTicketingSystemAssignment2.models.Passenger;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Reservation;
import com.sjsu5.FlightTicketingSystemAssignment2.response.components.Response;
import com.sjsu5.FlightTicketingSystemAssignment2.response.components.SuccessResponse;
import com.sjsu5.FlightTicketingSystemAssignment2.services.ExceptionThrower;
import com.sjsu5.FlightTicketingSystemAssignment2.services.ReservationService;
import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.GenericException;
import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.ResException;

@RestController
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
	
	/**
	 * GET all reserservations
	 * @return List of all active reseravtions
	 * @throws GenericException
	 */
	@RequestMapping(value="/reservations", method = RequestMethod.GET,
			produces = { "application/json", "application/xml"})
	public ResponseEntity<List<Reservation>> getAllReservations() throws GenericException {
		try {

			return new ResponseEntity<List<Reservation>>(reservationService.findAllReservation(), HttpStatus.OK ) ;
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null;
	}
	
	/**
	 * GET reservation details for a particular id
	 * @param id
	 * @return Reservation details for particular object
	 * @throws GenericException
	 */
	@RequestMapping(value="/reservation/{id}", method = RequestMethod.GET,
			produces = { "application/json", "application/xml"})
	public ResponseEntity<Reservation> getOneReservation(@PathVariable String id) throws GenericException {
		try {
			return new ResponseEntity<Reservation>(reservationService.findOneReservation(Integer.parseInt(id)), HttpStatus.OK ) ;
		}catch (RelationNotFoundException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.NOT_FOUND, "Sorry, the requested reservation with id "+ id +" does not exist");
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null;
	}
	
	/**
	 * Fully update an existing reservation entity
	 * @param requestParams
	 * @param id
	 * @return Updated reservation entity
	 * @throws ResException
	 * @throws GenericException
	 */
	@RequestMapping(value="/reservation/{id}", method=RequestMethod.PUT,produces = { "application/json", "application/xml" })
	public ResponseEntity<Reservation> UpdateReservation(@RequestParam Map<String,String> requestParams,@PathVariable String id) throws ResException, GenericException{
		try {
			reservationService.updateDeleteReservation(Integer.parseInt(id), requestParams.get("flightsAdded"), requestParams.get("flightsRemoved"));
			reservationService.updateAddReservation(Integer.parseInt(id), requestParams.get("flightsAdded"), requestParams.get("flightsRemoved"));
			return new ResponseEntity<Reservation>(reservationService.findOneReservation(Integer.parseInt(id)), HttpStatus.OK );
		}catch(RelationNotFoundException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, "e.getMessage()");
		}catch(ResException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, e.getMessage());
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null;
		}
	
	
	/**
	 * Create a new reservation
	 * @param requestParams
	 * @return Newly created reservation object
	 * @throws ResException
	 * @throws GenericException
	 */
	@RequestMapping(value="/reservation", method=RequestMethod.POST,produces = { "application/json", "application/xml" })
	public ResponseEntity<Reservation> MakeReservation(@RequestParam Map<String,String> requestParams) throws ResException, GenericException{
		try {
			reservationService.makeReservation(Integer.parseInt(requestParams.get("passengerId")), requestParams.get("flightLists"));
			int rID = reservationService.makeBookings(Integer.parseInt(requestParams.get("passengerId")), requestParams.get("flightLists"));
			return new ResponseEntity<Reservation>(reservationService.findOneReservation(rID), HttpStatus.OK );
		}catch(ResException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, e.getMessage());
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null;
		}
	
	
	/**
	 * DELETE an existing reservation
	 * @param id
	 * @return Success response based on successfully deletion
	 * @throws GenericException
	 */
	@RequestMapping(value="/reservation/{id}", method = RequestMethod.DELETE,
			produces = { "application/json", "application/xml"})
	public ResponseEntity<Response> cancelReservation(@PathVariable String id) throws GenericException {
		try {
			reservationService.cancelBookings(Integer.parseInt(id)) ;
			reservationService.cancelReservation(Integer.parseInt(id)) ;
			SuccessResponse succeessResponse=new SuccessResponse(HttpStatus.OK.value(),"Passenger with id "+ id +" is deleted successfully");
			Response Response=new Response();
			Response.setResponse(succeessResponse);
			return new ResponseEntity<Response>(Response, HttpStatus.OK);
		}catch (RelationNotFoundException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.NOT_FOUND, "Sorry, the requested reservation with id "+ id +" does not exist");
		}catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null;
		
	}
	
	
	/**
	 * Search reservation/reservations based on combination of passengerId, flightNumber, to and from 
	 * @param requestParams
	 * @return
	 * @throws GenericException
	 */
	@RequestMapping(value="/reservation", method = RequestMethod.GET,
			produces = { "application/json", "application/xml"})
	public ResponseEntity<List<Reservation>> searchReservation(@RequestParam Map<String,String> requestParams) throws GenericException {
		try {
			return new ResponseEntity<List<Reservation>>((reservationService.searchReservation(requestParams.get("passengerId"),
						requestParams.get("origin"), requestParams.get("to"), requestParams.get("flightNumber"))), HttpStatus.OK ) ;
		}catch (RelationNotFoundException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.NOT_FOUND, "Sorry, the requested reservation with id  does not exist");
		}catch(ResException e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		catch (Exception e) {
			ExceptionThrower thrower=new ExceptionThrower();
			thrower.throwGenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops Something went wrong. Please try again later");
		}
		return null;
	}
}