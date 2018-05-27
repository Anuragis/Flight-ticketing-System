package com.sjsu5.FlightTicketingSystemAssignment2.models;

import javax.persistence.Embeddable;

@Embeddable
public class Plane {
	
	
	private int capacity;
    private String model; 
    private String manufacturer;
    private int year;
    
    
	public int getCapacity() {
		return capacity;
	}
	public Plane setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}
	public String getModel() {
		return model;
	}
	public Plane setModel(String model) {
		this.model = model;
		return this;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public Plane setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
		return this;
	}
	public int getYear() {
		return year;
	}
	public Plane setYear(int year) {
		this.year = year;
		return this;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + ((manufacturer == null) ? 0 : manufacturer.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + year;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Plane other = (Plane) obj;
		if (capacity != other.capacity)
			return false;
		if (manufacturer == null) {
			if (other.manufacturer != null)
				return false;
		} else if (!manufacturer.equals(other.manufacturer))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

    
}
