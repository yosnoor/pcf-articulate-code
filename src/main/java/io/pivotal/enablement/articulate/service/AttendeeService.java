package io.pivotal.enablement.articulate.service;

import io.pivotal.enablement.articulate.model.Attendee;
import io.pivotal.enablement.articulate.repository.AttendeeRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendeeService {

	private static final Logger logger = LoggerFactory.getLogger(AttendeeService.class);
		
	@Autowired
	private AttendeeRepository attendeeRepository;
	
	public void add(Attendee a1) {
		Attendee a2 = new Attendee();
		a2.setFirstName(a1.getFirstName());
		a2.setLastName(a1.getLastName());
		a2.setEmailAddress(a1.getEmailAddress());
		attendeeRepository.saveAndFlush(a2);
	}
	
	// returning server object
	public List<Attendee> getAttendees() {
		return attendeeRepository.findAll();
	}
	
	



}
