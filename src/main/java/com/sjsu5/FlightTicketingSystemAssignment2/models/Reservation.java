package com.sjsu5.FlightTicketingSystemAssignment2.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sjsu5.FlightTicketingSystemAssignment2.controllers.ReservationController;
import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.FlightReservationMapping;
import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.ReservationPassenger;

@Entity
@Table(name="reservation")
public class Reservation implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3547620464986085776L;
	
	
	public static boolean looping=false;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="reservationnumber")
	private int reservationnumber;
	
	@Column(name="price")
	private double price;


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}

	
	@ManyToOne
	@JoinColumn(name="passenger_id", referencedColumnName="id")
	private ReservationPassenger passenger;
	

	@OneToMany
	@JoinTable(
			name="bookings",
			joinColumns= {@JoinColumn(name="reservationnumber")},
			inverseJoinColumns= {@JoinColumn(name="flightnumber")})
	private List<FlightReservationMapping> flights;
	



	public int getReservationnumber() {
		return reservationnumber;
	}

	public void setReservationnumber(int reservationnumber) {
		this.reservationnumber = reservationnumber;
	}

 
	public ReservationPassenger getPassenger() {
		if(this.passenger.getId()!=0) {
			looping = true;
			return this.passenger;
		}
		return passenger;
	
 }

	public void setPassenger(ReservationPassenger passenger) {
		this.passenger = passenger;
	}
	

	public List<FlightReservationMapping> getFlights() {
		return flights;
	}


	public void setFlights(List<FlightReservationMapping> flights) {
		this.flights = flights;
	}
	
	
}