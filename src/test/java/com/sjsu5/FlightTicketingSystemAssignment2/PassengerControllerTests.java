package com.sjsu5.FlightTicketingSystemAssignment2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.sjsu5.FlightTicketingSystemAssignment2.controllers.PassengerController;
import com.sjsu5.FlightTicketingSystemAssignment2.models.Passenger;
import com.sjsu5.FlightTicketingSystemAssignment2.services.PassengerService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {FlightTicketingSystemAssignment2Application.class})
@WebMvcTest(value=PassengerController.class, secure=false)
public class PassengerControllerTests {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PassengerService passengerService;
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPassengerById() throws Exception {
		Passenger mockPassenger = new Passenger();
		mockPassenger.setId(123);
		mockPassenger.setAge(11);
		mockPassenger.setFirstname("XX4");
		mockPassenger.setGender("famale");
		mockPassenger.setLastname("YY");
		mockPassenger.setPhone("123796");
		String inputInJson = this.mapToJson(mockPassenger);
		Mockito.when(passengerService.getPassenger(Mockito.anyInt())).thenReturn(mockPassenger);
		
		String URI = "/passenger/123";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		
		String outputInJson = response.getContentAsString();
		
		assertThat(outputInJson).isEqualTo(inputInJson);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetAllPassengers() throws Exception{
		String URI = "/passengers";
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
