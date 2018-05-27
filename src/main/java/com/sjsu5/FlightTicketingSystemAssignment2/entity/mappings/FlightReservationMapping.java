package com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sjsu5.FlightTicketingSystemAssignment2.models.Plane;

@Entity
@Table(name="flight")
public class FlightReservationMapping {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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


	public String getFlightnumber() {
		
		return flightnumber;
	}


	public void setFlightnumber(String flightnumber) {
		this.flightnumber = flightnumber;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getOrigin() {
		return origin;
	}


	public void setOrigin(String origin) {
		this.origin = origin;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}

	

	public String getDepartureTime() {
		return departureTime;
	}


	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}


	public String getArrivalTime() {
		return arrivalTime;
	}


	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}


	public int getSeatsLeft() {
		return seatsLeft;
	}


	public void setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Plane getPlane() {
		return plane;
	}


	public void setPlane(Plane plane) {
		this.plane = plane;
	}
    
}
