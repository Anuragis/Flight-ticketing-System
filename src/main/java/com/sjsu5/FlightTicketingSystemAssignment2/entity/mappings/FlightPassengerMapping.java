package com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * @author anurag
 *
 */
@Entity
@Table(name="PASSENGER")
public class FlightPassengerMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4541726584539640473L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;   
	
	public FlightPassengerMapping() {
	}
	
	public FlightPassengerMapping(String firstname, String lastname, int age, String gender, String phone) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.age = age;
		this.gender = gender;
		this.phone = phone;
	}
	
	
	
	@Column(name="phone")
	public String getPhone() {
		return phone;
	}

	@Column(unique=true)
    private String phone;
	
	@Column(name="firstname")
    private String firstname;
	
	@Column(name="lastname")
    private String lastname;
	
	@Column(name="age")
    private int age;
	
	@Column(name="gender")
    private String gender;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	

}
