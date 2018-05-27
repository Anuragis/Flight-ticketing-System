	package com.sjsu5.FlightTicketingSystemAssignment2.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.management.relation.RelationNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;

import org.aspectj.weaver.patterns.PerSingleton;
//import org.objenesis.instantiator.perc.PercSerializationInstantiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Passenger;
import com.sjsu5.FlightTicketingSystemAssignment2.repositories.PassengerRepository;

@Service
public class PassengerService {
	
	@Autowired
	private PassengerRepository passengerRepository;
	
	
	@PersistenceContext
	EntityManager em;
	/**
	 * Method retruns list of all passengers 
	 * @return list of all passengers
	 */
	public List<Passenger> getAllPersons(){
		List<Passenger>persons=new ArrayList<>();
		passengerRepository.findAll().
				forEach(persons::add);
		  return persons;
	}
	
	/**
	 * Method creates a new passenger entity
	 * @param passenger
	 * @return
	 * @throws DataIntegrityViolationException
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int addNewPassenger(Passenger passenger) throws DataIntegrityViolationException{
		passengerRepository.save(passenger);
		em.clear();
		Query id=em.createNativeQuery("Select max(id) FROM passenger");
		return (int) id.getSingleResult();
	}
	
	/**
	 * Method returns newly created passenger object
	 * @param id
	 * @return
	 * @throws RelationNotFoundException
	 */
	public Passenger getPassenger(int id) throws RelationNotFoundException {
			Passenger passengerObj=passengerRepository.findById(id)
		            .orElseThrow(() -> new RelationNotFoundException());
		return passengerObj;
	}
	
	/**
	 * Method fully updates an existing passenger
	 * @param id
	 * @param passenger
	 * @throws RelationNotFoundException
	 * @throws DataIntegrityViolationException
	 */
	public void updatePassenger(int id, Passenger passenger) throws RelationNotFoundException, DataIntegrityViolationException {
		Passenger passengerObj = passengerRepository.findById(id)
	            .orElseThrow(() -> new RelationNotFoundException());
		passengerObj.setId(id)
					.setFirstname(passenger.getFirstname())
					.setLastname(passenger.getLastname())
					.setAge(passenger.getAge())
					.setGender(passenger.getGender())
					.setPhone(passenger.getPhone());
		passengerRepository.save(passengerObj);
		
	}
	
	/**
	 * Method deletes an existing passenger entity
	 * @param id
	 * @throws RelationNotFoundException
	 */
	public void deletePassenger(int id) throws RelationNotFoundException {
		Passenger passengerObj = passengerRepository.findById(id)
	            .orElseThrow(() -> new RelationNotFoundException());
		passengerRepository.deleteById(id);
	}
		
}
