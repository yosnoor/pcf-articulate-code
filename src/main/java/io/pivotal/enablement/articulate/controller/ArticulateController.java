package io.pivotal.enablement.articulate.controller;

import io.pivotal.enablement.articulate.model.Attendee;
import io.pivotal.enablement.articulate.service.AttendeeService;
import io.pivotal.enablement.articulate.service.EnvironmentHelper;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @author mborges
 *
 */
@Controller
public class ArticulateController {

	private static final Logger logger = LoggerFactory.getLogger(ArticulateController.class);

	@Autowired
	private AttendeeService attendeeService;

	@Autowired
	private EnvironmentHelper environmentHelper;

	@RequestMapping("/")
	public String index(HttpServletRequest request, Model model) throws Exception {
		addAppEnv(request,model);
		return "index";
	}

	@RequestMapping(value = "/basics", method = RequestMethod.GET)
	public String kill(HttpServletRequest request,@RequestParam(value = "doit", required = false) boolean doit, Model model) throws Exception {

		addAppEnv(request, model);

		if (doit) {
			model.addAttribute("killed", true);
			logger.warn("*** The system is shutting down. ***");
			Runnable killTask = () -> {
				try {
					String name = Thread.currentThread().getName();
					logger.warn("killing shortly " + name);
					TimeUnit.SECONDS.sleep(5);
					logger.warn("killed " + name);
					System.exit(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
			new Thread(killTask).start();
		}

		return "basics";

	}

	@RequestMapping(value = "/services", method = RequestMethod.GET)
	public String attendees(HttpServletRequest request,Model model) throws Exception {

		model.addAttribute("attendees", attendeeService.getAttendees());
		model = clearAttendeeFormData(model);
		
		addAppEnv(request,model);
		return "services";
	}

	@RequestMapping(value = "/add-attendee", method = RequestMethod.POST)
	public String addAttendee(HttpServletRequest request,@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("emailAddress") String emailAddress, Model model) throws Exception {

		Attendee attendee = new Attendee();
		attendee.setFirstName(firstName);
		attendee.setLastName(lastName);
		attendee.setEmailAddress(emailAddress);
		boolean addFailed = false;
		try {
			attendeeService.add(attendee);
		}
		catch(Exception e){
			addFailed = true;
			logger.error("Failed to add attendee.", e);
		}
		model.addAttribute("addFailed", addFailed);
		if(addFailed){
			model.addAttribute("firstName", firstName);
			model.addAttribute("lastName", lastName);
			model.addAttribute("emailAddress", emailAddress);
		}
		else{
			model = clearAttendeeFormData(model);
		}
		model.addAttribute("attendees", attendeeService.getAttendees());

		addAppEnv(request, model);
		return "services";
	}


	private Model clearAttendeeFormData(Model model) {
		model.addAttribute("firstName", "");
		model.addAttribute("lastName", "");
		model.addAttribute("emailAddress", "");
		return model;
	}

	@RequestMapping("/bluegreen")
	public String bluegreen(HttpServletRequest request,Model model) throws Exception {

		for (String key : System.getenv().keySet()) {
			System.out.println(key + ":" + System.getenv(key));
		}

		addAppEnv(request, model);

		return "bluegreen";
	}


	private void addAppEnv(HttpServletRequest request, Model model) throws Exception {

		Map<String, Object> modelMap = environmentHelper.addAppEnv(request);

		model.addAllAttributes(modelMap);
	}

}
