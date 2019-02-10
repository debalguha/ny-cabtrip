package com.abc.test.cabapp.cache.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cache.CacheManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.abc.test.cabapp.cache.CacheServiceApplication;
import com.abc.test.cabapp.cache.service.TripReportLookupService;
import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.common.util.Utility;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CacheServiceApplication.class)
@TestPropertySource(
  locations = "classpath:application-integrationtest.properties")
public class TripReportLookupControllerIT {
	
	@LocalServerPort
	private int port;
	static String MEDALLION = "ABC";
	static String dateStr = Utility.formatter.format(LocalDate.now());
	
	@MockBean
	RestTemplate template;
	
	@SpyBean
	TripReportLookupService lookupService;
	
	@Autowired
	CacheManager cacheManager;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setupTest() {
		when(template.getForObject(anyString(), any(Class.class))).thenReturn(new TripReportModel(MEDALLION, Date.from(Instant.now()), 3));
		ResponseEntity<List<TripReportModel>> responseEntity = new ResponseEntity<List<TripReportModel>>(
				Lists.list(new TripReportModel("ABC", 3L), new TripReportModel("DEF", 3L)), HttpStatus.OK);
		when(template.exchange(anyString(), any(HttpMethod.class), isNull(), any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
	}
	
	@Test
	public void givenAMedallionAndADate_whenFindTripReportCalled_thenReturnsTripCount() throws Exception {
		assertEquals(3, invokeSingleMedallionService().getTripCount().intValue());
		assertEquals(3, invokeSingleMedallionService().getTripCount().intValue());
		assertEquals(3, invokeSingleMedallionService().getTripCount().intValue());
		assertEquals(3, invokeSingleMedallionService().getTripCount().intValue());
		Mockito.verify(lookupService, Mockito.times(1)).lookupTripReport(anyString(), any(LocalDate.class));
	}
	@Test
	public void givenAListOfmedallions_whenFindTripReportCalled_thenReturnsTripCountForEachForToday() throws Exception {
		assertEquals(2, invokeMultiMedallionService().size());
		assertEquals(2, invokeMultiMedallionService().size());
		assertEquals(2, invokeMultiMedallionService().size());
		assertEquals(2, invokeMultiMedallionService().size());
		Mockito.verify(lookupService, Mockito.times(1)).lookupTripReportForToday(any(String[].class));
	}
	
	private TripReportModel invokeSingleMedallionService() throws Exception {
		return new RestTemplate().getForEntity("http://localhost:"+port+"/tripreport/tripCount/medallion/ABC/on/"+ dateStr, TripReportModel.class).getBody();
	}
	private Collection<TripReportModel> invokeMultiMedallionService() throws Exception {
		return new RestTemplate().exchange("http://localhost:"+port+"/tripreport/tripsFor?medallion=ABC&medallion=DEF", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<TripReportModel>>() {}).getBody();
	}
}
