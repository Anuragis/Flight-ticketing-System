package com.sjsu5.FlightTicketingSystemAssignment2;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

@Override
public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

	//set path extension to false
	configurer.favorPathExtension(false).
    //request parameter ("format" by default) should be used to determine the requested media type
    favorParameter(true).
    //the favour parameter is set to "xml" instead of default "format"
    parameterName("xml").
    //ignore the accept headers
    ignoreAcceptHeader(true).
    //dont use Java Activation Framework since we are manually specifying the mediatypes required below
    useJaf(false).
    defaultContentType(MediaType.APPLICATION_JSON).
    mediaType("true", MediaType.APPLICATION_XML).
    mediaType("false", MediaType.APPLICATION_JSON);
	

  }
}