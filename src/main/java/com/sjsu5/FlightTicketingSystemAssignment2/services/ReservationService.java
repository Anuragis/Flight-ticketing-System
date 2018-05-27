package com.sjsu5.FlightTicketingSystemAssignment2.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.relation.RelationNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.FlightReservationMapping;
import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.PassengerFlightMapping;
import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.PassengerReservationMapping;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Flight;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Passenger;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Reservation;
import com.sjsu5.FlightTicketingSystemAssignment2.repositories.FlightRepository;
import com.sjsu5.FlightTicketingSystemAssignment2.repositories.PassengerRepository;
import com.sjsu5.FlightTicketingSystemAssignment2.repositories.ReservationRepository;
import com.sjsu5.FlightTicketingSystemAssignment2.systemExceptions.ResException;


@Service
public class ReservationService {
	
	@Autowired
	private ReservationRepository reservationRepo;
	@Autowired
	private PassengerRepository passengerRepo;

	@Autowired
	private FlightRepository flightRepository; 
	
	/**
	 * Method returns list of all reservations
	 * @return list of all reservations
	 */
	public List<Reservation> findAllReservation() {
		List<Reservation> reservationList = new ArrayList<>();
		reservationRepo.findAll().forEach(reservationList::add);
		return reservationList;
	}
	
	/**
	 * Method returns an existing passenger enity
	 * @param id
	 * @return Reservation entity
	 * @throws RelationNotFoundException
	 */
	public Reservation findOneReservation(int id) throws RelationNotFoundException {
		Reservation rObj = reservationRepo.findById(id)
				.orElseThrow(()-> new RelationNotFoundException());
		return rObj;
	}
	
	@PersistenceContext
	EntityManager em;
	
	
	/**
	 * Method creates new reservation entity
	 * @param id
	 * @param added
	 * @param removed
	 * @throws ResException
	 * @throws RelationNotFoundException
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateDeleteReservation(int id, String added, String removed) throws ResException, RelationNotFoundException {
		
		if(removed==null) return;
		em.clear();
		Reservation rObj = reservationRepo.findById(id)
				.orElseThrow(() -> new ResException("Reservation with Id: " + id + " not found"));
		
		double totalPrice = rObj.getPrice();
		
		
		
		if(removed != null) {
			String[] rAdded = removed.split(",");
			for(String entry: rAdded) {
				boolean flPresent = false;
				for(FlightReservationMapping getFl: rObj.getFlights()) {
					if(getFl.getFlightnumber().equals(entry)) flPresent = true;
				}
				if(!flPresent) 
					throw new ResException("Flight " + entry + " is not under the reservation");
				Flight fObj = flightRepository.findById(entry)
						.orElseThrow(() -> new ResException("Flight with Id: " + entry + " not found"));
				totalPrice -= fObj.getPrice();
				em.createQuery("DELETE from Bookings where reservationnumber=:id and flightnumber=:f1")
				.setParameter("id", id)
				.setParameter("f1", fObj.getFlightnumber())
					.executeUpdate();
				int seats = fObj.getSeatsLeft();
				em.createNativeQuery("UPDATE flight set seatsleft=:sl where flightnumber=:fn")
				.setParameter("sl", seats+1)
				.setParameter("fn", fObj.getFlightnumber())
				.executeUpdate();
			}
			em.createNativeQuery("UPDATE reservation set price=:pr where reservationnumber=:r1")
			.setParameter("pr", totalPrice)
			.setParameter("r1", id)
			.executeUpdate();
		}		
	}
	
	
	/**
	 * Method updates an existing reservation entity
	 * @param id
	 * @param added Flights to be added 
	 * @param removed Flight to be deleted
	 * @throws ResException
	 * @throws RelationNotFoundException
	 * @throws ParseException
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateAddReservation(int id, String added, String removed) throws ResException, RelationNotFoundException, ParseException {
		
		
		em.getEntityManagerFactory().getCache().evictAll();
		
		if(added==null) return;
		
		em.clear();
		Reservation rObj = reservationRepo.findById(id)
				.orElseThrow(() -> new ResException("Reservation with Id: " + id + " not found"));
		
		double totalPrice = rObj.getPrice();
		
		Passenger passenger = passengerRepo.findById(rObj.getPassenger().getId())
				.orElseThrow(()->new RelationNotFoundException());
		
		String[] rAdded = added.split(",");
		
		//check overlap between new flights to be added
		for(String flList: rAdded) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");
			Flight fl = flightRepository.findById(flList)
					.orElseThrow(() -> new RelationNotFoundException());
			java.util.Date arrival = sdf.parse(fl.getArrivalTime());
			java.util.Date dep = sdf.parse(fl.getDepartureTime());
			if(null != passenger.extractReseravtion()) {
				for(PassengerReservationMapping rsvn: passenger.extractReseravtion()) {
					for(PassengerFlightMapping oneflight: rsvn.getFlights()) {
						java.util.Date farrival = sdf.parse(oneflight.getArrivalTime());
						java.util.Date fdep = sdf.parse(oneflight.getDepartureTime());
						if( !  (arrival.before(fdep) || dep.after(farrival)) ){
							throw new ResException("Overlap occured!! for flight number: "+oneflight.getFlightnumber());
						}
					}
				}
			}
		}
		
		//check if there is an overalp with existing passenger bookings
		if(added != null) {
			String[] fAdded = added.split(",");
			for(String entry: fAdded) {
				Flight fObj = flightRepository.findById(entry)
						.orElseThrow(() -> new ResException("Flight with Id: " + entry + " not found"));
				
				totalPrice += fObj.getPrice();
				int seats = fObj.getSeatsLeft();
				if(seats==0) 
					throw new ResException("Seats are full for flight:" + fObj.getFlightnumber());
				em.createNativeQuery("INSERT into Bookings (reservationnumber, flightnumber, id) values (?, ?, ?)")
				.setParameter(1, id)
				.setParameter(2, fObj.getFlightnumber())
				.setParameter(3, rObj.getPassenger().getId())
					.executeUpdate();
				
				em.createNativeQuery("UPDATE flight set seatsleft=:sl where flightnumber=:fn")
				.setParameter("sl", seats-1)
				.setParameter("fn", fObj.getFlightnumber())
				.executeUpdate();
			}
		}
		
		em.createNativeQuery("UPDATE reservation set price=:pr where reservationnumber=:r1")
		.setParameter("pr", totalPrice)
		.setParameter("r1", id)
		.executeUpdate();
		
	}

	/**
	 * Creates new reservation for given list of flights for a particular passenger
	 * @param passengerID
	 * @param flightLists
	 * @throws RelationNotFoundException
	 * @throws ParseException
	 * @throws ResException
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void makeReservation(int passengerID,String flightLists) throws RelationNotFoundException, ParseException , ResException{
		
		em.clear();
		
		String[] rAdded = flightLists.split(",");
		double totalPrice=0;
		
		Passenger passenger = passengerRepo.findById(passengerID)
				.orElseThrow(()->new RelationNotFoundException());
		
		
		//check if there is an overlap between input flights
		for(String flList: rAdded) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");
			Flight fl = flightRepository.findById(flList)
					.orElseThrow(() -> new RelationNotFoundException());
			java.util.Date arrival = sdf.parse(fl.getArrivalTime());
			java.util.Date dep = sdf.parse(fl.getDepartureTime());
			
			for(String flig: rAdded) {
				if(!flig.equals(flList)) {
					Flight allflight = flightRepository.findById(flig)
							.orElseThrow(() -> new RelationNotFoundException());
					java.util.Date farrival = sdf.parse(allflight.getArrivalTime());
					java.util.Date fdep = sdf.parse(allflight.getDepartureTime());
					if( !  (arrival.before(fdep) || dep.after(farrival)) ){
						throw new ResException("Overlap occured!! for flight number: "+allflight.getFlightnumber());
					}
				}
			}
			
			if(null != passenger.getReservations()) {
				for(PassengerReservationMapping rsvn: passenger.getReservations()) {
						for(PassengerFlightMapping oneflight: rsvn.getFlights()) {
							java.util.Date farrival = sdf.parse(oneflight.getArrivalTime());
							java.util.Date fdep = sdf.parse(oneflight.getDepartureTime());
							if( ! (arrival.before(fdep) || dep.after(farrival)) ){
								throw new ResException("Overlap occured!! for flight number: "+oneflight.getFlightnumber());
							}
						}
					}
				}
			
			}
		
		em.createNativeQuery("INSERT into reservation (passenger_id, price) values (?, ?)")
		.setParameter(1, passengerID)
		.setParameter(2, totalPrice).executeUpdate();
		
		
	}

	
	/**
	 * Method makes bookings per reservation
	 * @param passengerID
	 * @param flightLists
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int makeBookings(int passengerID,String flightLists) {
		String[] rAdded = flightLists.split(",");
		double totalPrice=0;
		
		Query id=em.createNativeQuery("Select max(reservationnumber) FROM reservation");
		int lastId= (int) id.getSingleResult();
		
		
		em.clear();
		
		for(String entry: rAdded) {
			Flight fObj = flightRepository.findById(entry)
					.orElseThrow(() -> new ResException("Flight with Id: " + entry + " not found"));
			totalPrice += fObj.getPrice();
			
			int seats = fObj.getSeatsLeft();
			if(seats==0) {
				throw new ResException("Seats are full for flight:" + fObj.getFlightnumber());
			}
			
			em.createNativeQuery("INSERT into Bookings (reservationnumber, flightnumber, id) values (?, ?, ?)")
			.setParameter(1, lastId)
			.setParameter(2, fObj.getFlightnumber())
			.setParameter(3, passengerID)
				.executeUpdate();
			
			em.createNativeQuery("UPDATE flight set seatsleft=:sl where flightnumber=:fn")
			.setParameter("sl", seats-1)
			.setParameter("fn", fObj.getFlightnumber())
			.executeUpdate();
		}
		
		em.createNativeQuery("UPDATE reservation set price=:pr where reservationnumber=:r1")
		.setParameter("pr", totalPrice)
		.setParameter("r1", lastId)
		.executeUpdate();
		
		return lastId;
	}
	
	
	/**
	 * Cancel a reservation based on reservation id
	 * @param id
	 * @throws RelationNotFoundException
	 */
	@Transactional
	public void cancelReservation(int id) throws RelationNotFoundException {
		reservationRepo.deleteById(id);
		em.createNativeQuery("DELETE from bookings where reservationnumber=:r1")
		.setParameter("r1", id)
		.executeUpdate();
	}
	
	/**
	 * Cancel booking after cancellation of flight
	 * @param id
	 */
	@Transactional
	public void cancelBookings(int id) throws ResException{
		
			Reservation rObj = reservationRepo.findById(id)
					.orElseThrow(() -> new ResException("Reservation with Id: " + id + " not found"));
			
		for(FlightReservationMapping fObj: rObj.getFlights()) {	
			int seats = fObj.getSeatsLeft();
			
			em.createNativeQuery("UPDATE flight set seatsleft=:sl where flightnumber=:fn")
			.setParameter("sl", seats+1)
			.setParameter("fn", fObj.getFlightnumber())
			.executeUpdate();
		}
		
		em.createNativeQuery("DELETE from Bookings where reservationnumber=?")
		.setParameter(1, id)
		.executeUpdate();
		
	}
	
	
	/**
	 * Method search list of reservations based on either passengerId, origin, to or flightnumber
	 * @param passengerId
	 * @param origin
	 * @param to
	 * @param flightNumber
	 * @return
	 * @throws RelationNotFoundException
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<Reservation> searchReservation(String passengerId, String origin, String to, String flightNumber) throws RelationNotFoundException {

		
		List<Reservation> rObj = (List<Reservation>) reservationRepo.findAll();
		
		if(null != passengerId) {
			int passengerID = Integer.parseInt(passengerId);
			rObj = rObj.stream().filter(resrvtn -> resrvtn.getPassenger().getId() == passengerID)
					.collect(Collectors.toList());
		}
		if(null != origin) {
			for (Iterator<Reservation> resrvtn = rObj.iterator(); resrvtn.hasNext();) {
				Reservation reservation=resrvtn.next();
				for(FlightReservationMapping flight:reservation.getFlights()) {
					if(!flight.getOrigin().trim().equals(origin)) {
						resrvtn.remove();
					}
				}
			}
		}
		
		if(null != to) {
			for (Iterator<Reservation> resrvtn = rObj.iterator(); resrvtn.hasNext();) {
				Reservation reservation=resrvtn.next();
				for(FlightReservationMapping flight:reservation.getFlights()) {
					if(!flight.getDestination().trim().equals(to)) {
						resrvtn.remove();
					}
				}
			}
		}
		
		if(null != flightNumber) {
			for (Iterator<Reservation> resrvtn = rObj.iterator(); resrvtn.hasNext();) {
				Reservation reservation=resrvtn.next();
				for(FlightReservationMapping flight:reservation.getFlights()) {
					if(!flight.getFlightnumber().trim().equals(flightNumber)) {
						resrvtn.remove();
					}
				}
			}
			
		}
		
		return rObj;
	}
	
}