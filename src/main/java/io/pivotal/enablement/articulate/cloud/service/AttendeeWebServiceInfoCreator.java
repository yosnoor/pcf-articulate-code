package io.pivotal.enablement.articulate.cloud.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

public class AttendeeWebServiceInfoCreator extends
		CloudFoundryServiceInfoCreator<WebServiceInfo> {

	public static final String ATTENDEE_SERVICE_TAG = "attendee-service";

	public AttendeeWebServiceInfoCreator() {
		super(new Tags(ATTENDEE_SERVICE_TAG));
	}

	@Override
	public boolean accept(Map<String, Object> serviceData) {
		Map<String, Object> credentials = getCredentials(serviceData);
		String tag = (String) credentials.get("tag");
		return super.accept(serviceData) || ATTENDEE_SERVICE_TAG.equals(tag);
	}

	@Override
	public WebServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get("name");

		Map<String, Object> credentials = getCredentials(serviceData);
		String uri = getStringFromCredentials(credentials, "uri", "url");

		return new WebServiceInfo(id, uri);
	}


}