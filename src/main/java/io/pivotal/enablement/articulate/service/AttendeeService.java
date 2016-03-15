package io.pivotal.enablement.articulate.service;

import io.pivotal.enablement.articulate.controller.AttendeeController;
import io.pivotal.enablement.articulate.repo.AttendeeRepository;
import io.pivotal.enablement.articulate.server.model.Attendee;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AttendeeServices
 * 
 * The purpose of this service class is to have a clear separation from the UI
 * to the client.
 * 
 * This is a RestController and all the UI stuff is done by the MVC controller.
 * 
 * @see AttendeeController
 * 
 * @author mborges
 *
 */
@RestController
public class AttendeeService {

	private static final Logger logger = LoggerFactory.getLogger(AttendeeService.class);
	
	/// Polluting with server code
	// http://blog.zenika.com/2012/06/15/hateoas-paging-with-spring-mvc-and-spring-data-jpa/
	
	@Autowired
	private AttendeeRepository attendeeRepository;
	
	public void add(io.pivotal.enablement.articulate.client.model.Attendee a1) {
		Attendee a2 = new Attendee();
		a2.setFirstName(a1.getFirstName());
		a2.setLastName(a1.getLastName());
		a2.setEmailAddress(a1.getEmailAddress());
		attendeeRepository.saveAndFlush(a2);
	}
	
	// returning server object
	public Iterable<Attendee> getAttendees() {
		return attendeeRepository.findAll();
	}
	
	// returning server object
	Iterable<Attendee> searchName(String firstName) {
		return attendeeRepository.findByFirstNameContainsIgnoreCase(firstName, new PageRequest(0,100));
	}
	



}
