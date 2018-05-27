package com.sjsu5.FlightTicketingSystemAssignment2.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sjsu5.FlightTicketingSystemAssignment2.models.Passenger;

/**
 * 
 * @author Anurag
 * Passenger repository to carryot all crud operations
 */
public interface PassengerRepository extends CrudRepository<Passenger, Integer>{

}
