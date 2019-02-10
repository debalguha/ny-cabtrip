package com.abc.test.cabapp.cache.service;

import static com.abc.test.cabapp.common.util.Utility.formatter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.abc.test.cabapp.common.model.TripReportModel;

@Service
public class TripReportLookupServiceFromPersistenceService implements TripReportLookupService {
	
	private RestTemplate template;
	private String host;
	private String port;
	private String scheme;
	public TripReportLookupServiceFromPersistenceService(@Autowired RestTemplate template, 
			@Value("${lookup.service.scheme}") String scheme, @Value("${lookup.service.host}") String host, 
			@Value("${lookup.service.port}") String port) {
		this.template = template;
		this.scheme = scheme;
		this.host = host;
		this.port = port;
	}
	@Override
	@Cacheable(value = "trip-report", keyGenerator = "customTriReportKeyGenerator", sync = true)
	public TripReportModel lookupTripReport(String medallion, LocalDate tripDate) {
		return template.getForObject(buildLookupURIWithPathVariable(Endpoint.REPORT_SINGLE_ENDPT, medallion, formatter.format(tripDate)), TripReportModel.class);
	}
	public String buildLookupURIWithPathVariable(Endpoint endpoint, Object ... params) {
		return UriComponentsBuilder.fromPath(endpoint.getEndpointUri()).scheme(scheme)
				.host(host).port(port).buildAndExpand(params).toUriString();
	}
	
	public String buildLookupURIWithQueryParams(Endpoint endpoint, Map<String, Object[]> queryParams) {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(endpoint.getEndpointUri()).scheme(scheme).host(host).port(port);
		queryParams.entrySet().forEach((Map.Entry<String, Object[]> entry) -> uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue()));
		return uriComponentsBuilder.build().toUriString();
	}
	@Override
	@Cacheable(value = "trip-reports", sync = true)
	public Collection<TripReportModel> lookupTripReportForToday(String[] medallions) {
		Map<String, Object[]> params = new HashMap<>();
		params.put(Endpoint.REPORT_MULTI_ENDPT.getQueryParameterNames()[0], medallions);
		ResponseEntity<List<TripReportModel>> reportEntities = template.exchange(buildLookupURIWithQueryParams(Endpoint.REPORT_MULTI_ENDPT, params)
				, HttpMethod.GET, null, new ParameterizedTypeReference<List<TripReportModel>>() {});
		return reportEntities.getBody();
	}
}
