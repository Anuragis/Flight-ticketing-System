package com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Flight;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Passenger;

@Entity
@Table(name="reservation")
public class PassengerReservationMapping  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2711813558780195593L;
	@Id
	@Column(name="reservationnumber")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int reservationnumber;
	
	@Column(name="price")
	private double price;
	
	@ManyToOne
	private Passenger passenger;

	
	@OneToMany
	@JoinTable(
			name="bookings",
			joinColumns= {@JoinColumn(name="reservationnumber")},
			inverseJoinColumns= {@JoinColumn(name="flightnumber", unique=true)})
	private List<PassengerFlightMapping> flights;




	public int getReservationnumber() {
		return reservationnumber;
	}




	public void setReservationnumber(int reservationnumber) {
		this.reservationnumber = reservationnumber;
	}




	public double getPrice() {
		return price;
	}




	public void setPrice(double price) {
		this.price = price;
	}




	public List<PassengerFlightMapping> getFlights() {
		return flights;
	}




	public void setFlights(List<PassengerFlightMapping> flights) {
		this.flights = flights;
	}


	

	
	
}
