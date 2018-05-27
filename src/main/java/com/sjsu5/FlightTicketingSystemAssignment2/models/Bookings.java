package com.sjsu5.FlightTicketingSystemAssignment2.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="bookings")
@NamedQuery(name="Bookings.deleteByReservationNumberAndFlightNumber", query="DELETE from Bookings where reservationnumber=?1 and flightnumber=?2")
public class Bookings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4315724041930116918L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="bookingsid")
	private String bookingsid;
	
	@ManyToOne
	@JoinColumn(name="reservationnumber")
	private Reservation reservation;
	
	@ManyToOne
	@JoinColumn(name="flightnumber")
	private Flight flight;
	
	public Reservation getReservation() {
		return reservation;
	}



	public Bookings setReservation(Reservation reservation) {
		this.reservation = reservation;
		return this;
	}



	public Flight getFlight() {
		return flight;
	}



	public Bookings setFlight(Flight flight) {
		this.flight = flight;
		return this;
	}



	public Bookings() {
		
	}
	
	

	public Bookings(String bookingsid) {
		super();
		this.bookingsid = bookingsid;
		
	}

	public String getBookingsid() {
		return bookingsid;
	}

	public Bookings setBookingsid(String bookingsid) {
		this.bookingsid = bookingsid;
		return this;
	}
	
	
}