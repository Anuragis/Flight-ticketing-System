package com.sjsu5.FlightTicketingSystemAssignment2.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sjsu5.FlightTicketingSystemAssignment2.models.Flight;

/**
 * 
 * @author Anurag
 * Flight repository to carry out all crud operations
 */
public interface FlightRepository extends CrudRepository<Flight, String> {

}
