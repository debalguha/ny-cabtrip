package com.abc.test.cabapp.cache.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.common.util.Utility;

public class TripReportLookupServiceFromPersistenceServiceTest {
	@Mock
	private RestTemplate template;
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldBeAbleToBuildProperURIWithPathVariable() {
		String uri = new TripReportLookupServiceFromPersistenceService(template, "http", "localhost", "8080")
				.buildLookupURIWithPathVariable(Endpoint.REPORT_SINGLE_ENDPT, "ABC", Utility.formatter.format(LocalDate.now().withDayOfMonth(1).withMonth(1).withYear(2019)));
		assertEquals("http://localhost:8080/cabtrip/tripCount/medallion/ABC/on/20190101", uri);
	}
	
	@Test
	public void shouldBeAbleToBuildProperURIWithRequestParams() {
		Map<String, Object[]> queryParams = new HashMap<>();
		queryParams.put("a", new String[] {"b"});
		queryParams.put("c", new String[] {"d", "e"});
		String uri = new TripReportLookupServiceFromPersistenceService(template, "http", "localhost", "8080")
				.buildLookupURIWithQueryParams(Endpoint.REPORT_MULTI_ENDPT, queryParams);
		assertEquals("http://localhost:8080/cabtrip/tripCount?a=b&c=d&c=e", uri);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void givenAMedallionAndADate_whenlookupTripReportIsCalled_shouldFindATripReport() throws Exception {
		when(template.getForObject(anyString(), any(Class.class))).thenReturn(new TripReportModel());
		assertNotNull(new TripReportLookupServiceFromPersistenceService(template, "http", "localhost", "8080")
				.lookupTripReport("ABC", LocalDate.now()));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void given2Medallion_whenlookupTripReportForTodayIsCalled_shouldFind2TripReports() throws Exception {
		ResponseEntity<List<TripReportModel>> responseEntity = new ResponseEntity<List<TripReportModel>>(
				Lists.list(new TripReportModel("ABC", 3L), new TripReportModel("DEF", 3L)), HttpStatus.OK);
		when(template.exchange(anyString(), any(HttpMethod.class), isNull(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
		assertEquals(3, new TripReportLookupServiceFromPersistenceService(template, "http", "localhost", "8080")
				.lookupTripReportForToday(new String[] {"ABC", "DEF"}).stream().filter(report -> report.getMedallion().equals("ABC")).findFirst().get().getTripCount().intValue());
	}
}
