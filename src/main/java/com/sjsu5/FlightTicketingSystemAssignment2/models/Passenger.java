package com.sjsu5.FlightTicketingSystemAssignment2.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sjsu5.FlightTicketingSystemAssignment2.controllers.FlightController;
import com.sjsu5.FlightTicketingSystemAssignment2.controllers.PassengerController;
import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.PassengerReservationMapping;


@Entity
@Table(name="PASSENGER")
public class Passenger implements Serializable {
	 	
		/**
	 * 
	 */
	private static final long serialVersionUID = 8362829842509425543L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name="id")
		private int id;   
		
		public Passenger() {
		}
		
		public Passenger(String firstname, String lastname, int age, String gender, String phone) {
			super();
			this.firstname = firstname;
			this.lastname = lastname;
			this.age = age;
			this.gender = gender;
			this.phone = phone;
		}

		@Column(name="firstname")
	    private String firstname;
		
		@Column(name="lastname")
	    private String lastname;
		
		@Column(name="age")
	    private int age;
		
		@Column(name="gender")
	    private String gender;

		@Column(unique=true)
	    private String phone;
		
		@OneToMany(mappedBy="passenger")
		private List<PassengerReservationMapping> reservations;
		
		

		public List<PassengerReservationMapping> getReservations() {
			if(Reservation.looping) {
				Reservation.looping=false;
				return null;
			}
			return reservations;
		}

		public int getId() {
			return id;
		}

		public Passenger setId(int id) {
			this.id = id;
			return this;
		}

		public String getFirstname() {
			return firstname;
		}

		public Passenger setFirstname(String firstname) {
			this.firstname = firstname;
			return this;
		}

		public String getLastname() {
			return lastname;
		}

		public Passenger setLastname(String lastname) {
			this.lastname = lastname;
			return this;
		}

		public int getAge() {
			return age;
		}

		public Passenger setAge(int age) {
			this.age = age;
			return this;
		}

		public String getGender() {
			return gender;
		}

		public Passenger setGender(String gender) {
			this.gender = gender;
			return this;
		}

		public String getPhone() {
			return phone;
		}

		public Passenger setPhone(String phone) {
			this.phone = phone;
			return this;
		}

		public List<PassengerReservationMapping> extractReseravtion() {
			return reservations;
		}
		
		

}
