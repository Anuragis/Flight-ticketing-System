package com.sjsu5.FlightTicketingSystemAssignment2.services;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.RelationNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.FlightPassengerMapping;
import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.PassengerFlightMapping;
import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.PassengerReservationMapping;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Flight;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Passenger;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Reservation;
import com.sjsu5.FlightTicketingSystemAssignment2.repositories.FlightRepository;
import com.sjsu5.FlightTicketingSystemAssignment2.repositories.PassengerRepository;
import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.ResException;

@Service
public class FlightService {
	
	
	@Autowired
	private FlightRepository flightRepository;
	
	
	@Autowired
	private PassengerRepository passengerRepository;
	
	@PersistenceContext
	EntityManager em;
	
	/**
	 * Method returns list of all passengers
	 * @return List of all passengers
	 */
	public List<Flight> getAllFlights() {
		List<Flight>flights=new ArrayList<>();
		flightRepository.findAll().
				forEach(flights::add);
		  return flights;
	}
	
	/**
	 * Method returns details of a single flight based on input id
	 * @param id
	 * @return Single flight details
	 * @throws RelationNotFoundException
	 */
	public Flight getFlight(String id) throws RelationNotFoundException {
		Flight flightObj=flightRepository.findById(id)
	            .orElseThrow(() -> new RelationNotFoundException(id));
	return flightObj;
	}
	
	
	/**
	 * Method creates a new flight entity
	 * @param flight
	 */
	public void addNewFlight(Flight flight) {
		flightRepository.save(flight);
	}
	
	@Transactional
	public void updateFlight(Flight flight) throws ParseException {
		Flight fObj = flightRepository.findById(flight.getFlightnumber())
				.orElseThrow(() -> new ResException("Flight with Id: " + flight.getFlightnumber() + " not found"));
		
		if(null != fObj.getPassengers() && flight.getPlane().getCapacity()<fObj.getPassengers().size()) {
			throw new ResException("Capacity not be lowered than active number of bookings");
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");
		java.util.Date arrival = sdf.parse(flight.getArrivalTime());
		java.util.Date dep = sdf.parse(flight.getDepartureTime());
		
		//check for overlap with existing flight bookings
		if(null !=fObj.getPassengers()) {
			for(FlightPassengerMapping fpm:fObj.getPassengers()) {
				Passenger pObjt=passengerRepository.findById(fpm.getId())
								.orElseThrow(() -> new ResException("Passenger with Id: " + fpm.getId() + " not found"));
				for(PassengerReservationMapping rpm:pObjt.getReservations()) {
					for(PassengerFlightMapping rfm: rpm.getFlights()) {
						
						java.util.Date farrival = sdf.parse(rfm.getArrivalTime());
						java.util.Date fdep = sdf.parse(rfm.getDepartureTime());
						if(!(flight.getFlightnumber().equals(rfm.getFlightnumber()) )&& ! (arrival.before(fdep) || dep.after(farrival)) ){
							throw new ResException("Overlap occured!! for flight number: "+rfm.getFlightnumber());
						}
					}
				}
			}
		}
		
		if(null != fObj.getPassengers()) {
			flight.setSeatsLeft(flight.getPlane().getCapacity()-fObj.getPassengers().size());
		}else {
			flight.setSeatsLeft(flight.getPlane().getCapacity());
		}
		
		//flightRepository.save(flight);
		em.createNativeQuery("UPDATE flight set price=:pr, origin=:or, destination=:de, "
				+ "departuretime=:dt, arrivaltime=:at, description=:des, capacity=:cap, "
				+ "model=:mod, manufacturer=:manu, year=:yr where flightnumber=:fn")
		.setParameter("pr", flight.getPrice())
		.setParameter("or", flight.getOrigin())
		.setParameter("de", flight.getDestination())
		.setParameter("dt", flight.getDepartureTime())
		.setParameter("at", flight.getArrivalTime())
		.setParameter("des", flight.getDescription())
		.setParameter("cap", flight.getPlane().getCapacity())
		.setParameter("mod", flight.getPlane().getModel())
		.setParameter("manu", flight.getPlane().getManufacturer())
		.setParameter("yr", flight.getPlane().getYear())
		.setParameter("fn", flight.getFlightnumber())
		.executeUpdate();
		
	}
	
	/**
	 * Delete an existing flight entity based on input id
	 * @param id
	 * @throws RelationNotFoundException
	 */
	public void deleteFlight(String id) throws RelationNotFoundException {
		
		Flight fObj = flightRepository.findById(id)
				.orElseThrow(() -> new ResException("Flight with Id: " + id + " not found"));
		
		if(null == fObj.getPassengers() || fObj.getPassengers().size()==0)
			{
				flightRepository.deleteById(id);
			}else {
				throw new ResException("Sorry, request flight cannot be deleted as it has one or more reservations!!");
			}
	}
}