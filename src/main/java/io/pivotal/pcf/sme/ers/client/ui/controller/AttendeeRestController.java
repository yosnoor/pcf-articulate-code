package io.pivotal.pcf.sme.ers.client.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendeeRestController {

	@Autowired
	private EnvironmentHelper environmentHelper;
	/**
	 * bluegreenRequest - It really just returns the app name.
	 * 
	 * The purpose is to show PCF load balancing between applications that share
	 * routes
	 * 
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/bluegreen-check")
	public String bluegreenRequest() throws Exception {
		return (String) environmentHelper.getVcapApplicationMap().
				getOrDefault("application_name", "no name environment variable");
	}
}
