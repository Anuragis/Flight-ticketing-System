package com.sjsu5.FlightTicketingSystemAssignment2.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.FlightPassengerMapping;

@Entity
@Table(name="flight")
public class Flight implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4697448291749674397L;


	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="flightnumber")
	private String flightnumber; // Each flight has a unique flight number.*/
	
	
    @Column(name="price")
	private double price;
    
    @Column(name="origin")
    private String origin;
    
    @Column(name="destination")
    private String destination;
    
    @Column(name="departuretime")
    private String departureTime;
    
    @Column(name="arrivaltime")
    private String arrivalTime;
    
    @Column(name="seatsleft")
    private int seatsLeft;
    
    @Column(name="description")
    private String description;
    
	@Embedded
    private Plane plane;
    
    @OneToMany(cascade= CascadeType.PERSIST, orphanRemoval = false)
	@JoinTable(
			name="bookings",
			joinColumns= {@JoinColumn(name="flightnumber")},
			inverseJoinColumns= {@JoinColumn(name="id", unique=true)})
	private List<FlightPassengerMapping> passengers;
    
    


	public List<FlightPassengerMapping> getPassengers() {
		return passengers;
	}


	public void setPassengers(List<FlightPassengerMapping> passengers) {
		this.passengers = passengers;
	}


	public Plane getPlane() {
		return plane;
	}


	public Flight setPlane(Plane plane) {
		this.plane = plane;
		return this;
	}


	public Flight() {
    	
    }
    


	public Flight(String flightnumber, double price, String origin, String destination, String departureTime,
			String arrivalTime, int seatsLeft, String description, Plane plane,
			List<FlightPassengerMapping> passengers) {
		super();
		this.flightnumber = flightnumber;
		this.price = price;
		this.origin = origin;
		this.destination = destination;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.seatsLeft = seatsLeft;
		this.description = description;
		this.plane = plane;
		this.passengers = passengers;
	}


	public String getFlightnumber() {
		return flightnumber;
	}


	public Flight setFlightnumber(String flightnumber) {
		this.flightnumber = flightnumber;
		return this;
	}


	public double getPrice() {
		return price;
	}
	public Flight setPrice(double price) {
		this.price = price;
		return this;
	}
	
	public String getOrigin() {
		return origin;
	}


	public Flight setOrigin(String origin) {
		this.origin = origin;
		return this;
	}


	public String getDestination() {
		return destination;
	}


	public Flight setDestination(String destination) {
		this.destination = destination;
		return this;
	}


	public String getDepartureTime() {
		return departureTime;
	}


	public Flight setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
		return this;
	}


	@Override
	public String toString() {
		return "Flight [flightnumber=" + flightnumber + ", price=" + price + ", origin=" + origin + ", destination=" + destination
				+ ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + ", seatsLeft=" + seatsLeft
				+ ", description=" + description + ", plane=" + plane + ", passengers=" + passengers + "]";
	}


	public String getArrivalTime() {
		return arrivalTime;
	}


	public Flight setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
		return this;
	}


	public int getSeatsLeft() {
		return seatsLeft;
	}
	public Flight setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public Flight setDescription(String description) {
		this.description = description;
		return this;
	}




    
}
