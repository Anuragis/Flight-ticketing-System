package com.sjsu5.FlightTicketingSystemAssignment2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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
import com.sjsu5.FlightTicketingSystemAssignment2.models.Flight;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Plane;
import com.sjsu5.FlightTicketingSystemAssignment2.services.FlightService;



@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {FlightTicketingSystemAssignment2Application.class})
@WebMvcTest(value=FlightController.class, secure=false)
public class FlightControllerTests {
		
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FlightService flightService;
	
	@Test
	public void testGetFlightById() throws Exception {
		Flight mockFlight = new Flight();
		mockFlight.setFlightnumber("GH2Z2");
		mockFlight.setPrice(120);
		mockFlight.setOrigin("AA");
		mockFlight.setDestination("BB");
		mockFlight.setArrivalTime("2018-10-15 10");
		mockFlight.setDepartureTime("2018-10-15 7");
		mockFlight.setDescription("VIP Flight");
		Plane plane =new Plane();
		plane.setCapacity(10);
		plane.setManufacturer("Boeing");
		plane.setModel("H123");
		plane.setYear(1997);
		mockFlight.setPlane(plane);
		
		
		String inputInJson = this.mapToJson(mockFlight);
		Mockito.when(flightService.getFlight(Mockito.anyString())).thenReturn(mockFlight);
		
		String URI = "/flight/GH2Z2";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		String outputInJson = response.getContentAsString();
		
		assertThat(outputInJson).isEqualTo(inputInJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		}
	
	
	@Test
	public void testGetAllFlights() throws Exception{
		String URI = "/flights";
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
