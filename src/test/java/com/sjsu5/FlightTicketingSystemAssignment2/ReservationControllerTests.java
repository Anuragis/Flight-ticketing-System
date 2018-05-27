package com.sjsu5.FlightTicketingSystemAssignment2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu5.FlightTicketingSystemAssignment2.controllers.FlightController;
import com.sjsu5.FlightTicketingSystemAssignment2.controllers.ReservationController;
import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.FlightReservationMapping;
import com.sjsu5.FlightTicketingSystemAssignment2.entity.mappings.ReservationPassenger;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Flight;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Passenger;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Plane;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Reservation;
import com.sjsu5.FlightTicketingSystemAssignment2.services.FlightService;
import com.sjsu5.FlightTicketingSystemAssignment2.services.PassengerService;
import com.sjsu5.FlightTicketingSystemAssignment2.services.ReservationService;




@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {FlightTicketingSystemAssignment2Application.class})
@WebMvcTest(value=ReservationController.class, secure=false)
public class ReservationControllerTests {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReservationService reservationService;
	
	
	@Test
	public void testGetReseravtionById() throws Exception {
		Reservation mockReseravtion = new Reservation();
		mockReseravtion.setReservationnumber(123);
		mockReseravtion.setPrice(120);
		
		
		
		FlightReservationMapping mockFlight = new FlightReservationMapping();
		mockFlight.setFlightnumber("HI1231");
		mockFlight.setPrice(120);
		mockFlight.setOrigin("AA");
		mockFlight.setDestination("BB");
		mockFlight.setArrivalTime("2018-10-15 10");
		mockFlight.setDepartureTime("2018-10-15 13");
		mockFlight.setDescription("VIP Flight");
		mockFlight.setSeatsLeft(2);
		Plane plane =new Plane();
		plane.setCapacity(100);
		plane.setManufacturer("Boeing");
		plane.setModel("H123");
		plane.setYear(1997);
		mockFlight.setPlane(plane);
		
		List<FlightReservationMapping> flights=new ArrayList<FlightReservationMapping>();
		flights.add(mockFlight);
		mockReseravtion.setFlights(flights);
		
		
		ReservationPassenger mockPassenger = new ReservationPassenger();
		mockPassenger.setId(123);
		mockPassenger.setAge(11);
		mockPassenger.setFirstname("XX4");
		mockPassenger.setGender("famale");
		mockPassenger.setLastname("YY");
		mockPassenger.setPhone("1234");
	
		mockReseravtion.setPassenger(mockPassenger);
		
		
		
		
		
		String inputInJson = this.mapToJson(mockReseravtion);
		Mockito.when(reservationService.findOneReservation(Mockito.anyInt())).thenReturn(mockReseravtion);
		
		String URI = "/reservation/123";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		String outputInJson = response.getContentAsString();
		
		assertThat(outputInJson).isEqualTo(inputInJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		}
	
	@Test
	public void testGetAllReseravtions() throws Exception{
		String URI = "/reservations";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}
	
}
