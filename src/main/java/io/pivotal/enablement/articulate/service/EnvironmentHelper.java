package io.pivotal.enablement.articulate.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EnvironmentHelper {

	private static final Logger logger = LoggerFactory.getLogger(EnvironmentHelper.class);


	/**
	 * addAppEnv - Retrieve information about the application
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> addAppEnv(HttpServletRequest request) throws Exception {

		Map<String, Object> modelMap = new HashMap<String, Object>();

		if(System.getenv("PORT")== null){
			modelMap.put("containerAddr", "localhost");
		}
		else{
			modelMap.put("containerAddr", request.getLocalAddr() + ":" + request.getLocalPort());
		}
		
		String instanceIndex = getVcapApplicationMap().getOrDefault("instance_index", "no index environment variable")
				.toString();
		modelMap.put("instanceIndex", instanceIndex);

		String instanceAddr = System.getenv("CF_INSTANCE_ADDR");
		if (instanceAddr == null) {
			instanceAddr = "localhost";
		}
		modelMap.put("instanceAddr", instanceAddr);

		String applicationName = (String) getVcapApplicationMap().getOrDefault("application_name",
				"no name environment variable");
		modelMap.put("applicationName", applicationName);

		@SuppressWarnings("rawtypes")
		Map services = getVcapServicesMap();
		modelMap.put("applicationServices", services);
		

		String javaVersion = System.getProperty("java.version");
		logger.debug("Java Version (unfiltered): {}", javaVersion);
		
		int pos = javaVersion.indexOf("-");
		if(pos > -1){
			javaVersion = javaVersion.substring(0, pos);
		}
		logger.debug("Java Version (filtered): {}", javaVersion);
		
		modelMap.put("javaVersion",javaVersion);
		return modelMap;
	}

	///////////////////////////////////////
	// Helper Methods
	///////////////////////////////////////

	@SuppressWarnings("rawtypes")
	public Map getVcapApplicationMap() throws Exception {
		return getEnvMap("VCAP_APPLICATION");
	}

	@SuppressWarnings("rawtypes")
	private Map getVcapServicesMap() throws Exception {
		return getEnvMap("VCAP_SERVICES");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map getEnvMap(String vcap) throws Exception {
		String vcapEnv = System.getenv(vcap);
		ObjectMapper mapper = new ObjectMapper();

		if (vcapEnv != null) {
			Map<String, ?> vcapMap = mapper.readValue(vcapEnv, Map.class);
			return vcapMap;
		}

		logger.warn(vcap + " not defined, returning empty Map");
		return new HashMap<String, String>();
	}
}
