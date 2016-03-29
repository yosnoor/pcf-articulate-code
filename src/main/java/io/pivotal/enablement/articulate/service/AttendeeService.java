package io.pivotal.enablement.articulate.service;

import io.pivotal.enablement.articulate.cloud.service.WebServiceInfo;
import io.pivotal.enablement.articulate.model.Attendee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class AttendeeService {

	private static final Logger logger = LoggerFactory
			.getLogger(AttendeeService.class);

	@Value("${articulate.attendee-service.uri:http://localhost:8181/attendees}")
	private String uri;
	
	@PostConstruct
	public void init() {
		try {
			CloudFactory cloudFactory = new CloudFactory();
			Cloud cloud = cloudFactory.getCloud();
			List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
			for (ServiceInfo serviceInfo : serviceInfos) {
				if (serviceInfo instanceof WebServiceInfo) {
					WebServiceInfo webServiceInfo = (WebServiceInfo) serviceInfo;
					logger.info("Setting attendee-service uri: {}", webServiceInfo.getUri() );
					this.uri = webServiceInfo.getUri();
				}
			}
		} catch (CloudException e) {
			logger.debug("Failed to read cloud environment.  Ignore if running locally.");
		}
	}

	public void add(Attendee attendee) {

		RestTemplate restTemplate = restTemplate();
		ResponseEntity<Attendee> responseEntity = restTemplate.postForEntity(
				uri, attendee, Attendee.class);
		logger.debug("ResponseEntity<Attendee>: {}", responseEntity);

	}

	@HystrixCommand(fallbackMethod="defaultList")
	public List<Attendee> getAttendees() {

		RestTemplate restTemplate = restTemplate();
		ResponseEntity<PagedResources<Attendee>> responseEntity = restTemplate
				.exchange(
						uri,
						HttpMethod.GET,
						getHttpEntity(),
						new ParameterizedTypeReference<PagedResources<Attendee>>() {
						});
		PagedResources<Attendee> pagedResources = responseEntity.getBody();
		logger.debug("PagedResources<Attendee>: {}", pagedResources);

		List<Attendee> attendeeList = new ArrayList<Attendee>();
		for (Attendee attendee : pagedResources) {
			attendeeList.add(attendee);
		}
		return attendeeList;
	}
	
	@SuppressWarnings("unused")
	private List<Attendee> defaultList(){
		return new ArrayList<Attendee>();
	}

	private RestTemplate restTemplate() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.registerModule(new Jackson2HalModule());

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType
				.parseMediaTypes("application/json"));
		converter.setObjectMapper(mapper);
		return new RestTemplate(Arrays.asList(converter));
	}

	private HttpEntity<String> getHttpEntity() {

		HttpHeaders headers = new HttpHeaders();
		List<MediaType> accepts = new ArrayList<>();
		accepts.add(MediaType.APPLICATION_JSON);
		headers.setAccept(accepts);
		return new HttpEntity<String>(headers);
	}

}
