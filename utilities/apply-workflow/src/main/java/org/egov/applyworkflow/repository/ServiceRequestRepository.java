package org.egov.applyworkflow.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.ServiceCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Repository
public class ServiceRequestRepository {

    private static final Logger log = LoggerFactory.getLogger(ServiceRequestRepository.class);


    private ObjectMapper mapper;

    private RestTemplate restTemplate;


    /**
	 * Fetches results from a REST service using the uri and object
	 * 
	 * @return Object
	 * @author vishal
	 * @updated Bimal
	 */
    @Autowired
    public ServiceRequestRepository(ObjectMapper mapper, RestTemplate restTemplate) {
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }


    public Object fetchResult(StringBuilder uri, Object request) {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object response = null;
		log.debug("URI: " + uri.toString());
        try {
        	log.debug("Request: " + mapper.writeValueAsString(request));
            response = restTemplate.postForObject(uri.toString(), request, Map.class);
        }catch(HttpClientErrorException e) {
            log.error("External Service threw an Exception: ",e);
            throw new ServiceCallException(e.getResponseBodyAsString());
        }catch(Exception e) {
            log.error("Exception while fetching from searcher: ",e);
        }

        return response;
    }
}