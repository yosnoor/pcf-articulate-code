package com.gopivotal.pcf.sme.ers.server;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import io.pivotal.enablement.articulate.PcfErsDemo1Application;
import io.pivotal.enablement.articulate.repo.AttendeeRepository;
import io.pivotal.enablement.articulate.server.model.Attendee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PcfErsDemo1Application.class)
@WebAppConfiguration
public class PcfErsDemo1ApplicationTests {
	
	@Autowired
	private AttendeeRepository attendeeRepository;

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void attendeeRepository() throws ParseException {
		
		attendeeRepository.deleteAll();
		
		Attendee a1 = new Attendee();
		a1.setFirstName("Phil");
		a1.setLastName("Berman");
		attendeeRepository.save(a1);
		
		a1 = new Attendee();
		a1.setFirstName("Marcelo");
		a1.setLastName("Borges");
		attendeeRepository.save(a1);
		
		List<Attendee> attendees = attendeeRepository.findAll();
		assertThat(attendees.size(), is(2));
		
	}

}
