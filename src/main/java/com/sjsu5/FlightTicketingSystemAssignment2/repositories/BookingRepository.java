package com.sjsu5.FlightTicketingSystemAssignment2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sjsu5.FlightTicketingSystemAssignment2.models.Bookings;


/**
 * 
 * @author Anurag
 * Booking repository to carry out all crud operations 
 */
public interface BookingRepository extends JpaRepository<Bookings, String>{
}
