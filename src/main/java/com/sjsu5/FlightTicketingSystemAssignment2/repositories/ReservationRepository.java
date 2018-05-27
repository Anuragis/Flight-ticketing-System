package com.sjsu5.FlightTicketingSystemAssignment2.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sjsu5.FlightTicketingSystemAssignment2.models.Reservation;

/**
 * 
 * @author Anurag
 * Reservation repository to carry out all crud operations
 */
public interface ReservationRepository extends CrudRepository<Reservation, Integer>{
	
}