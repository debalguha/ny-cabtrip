package com.abc.test.cabapp.persistence.web;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.common.util.Utility;
import com.abc.test.cabapp.persistence.CabTripBuilder;
import com.abc.test.cabapp.persistence.PersistenceServiceApplication;
import com.abc.test.cabapp.persistence.model.CabTrip;
import com.abc.test.cabapp.persistence.model.TripId;
import com.abc.test.cabapp.persistence.service.CabTripService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PersistenceServiceApplication.class)
@TestPropertySource(
  locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class PersistenceServiceControllerIT {
	private static final String LOCAL_HOST = "http://localhost:";
	@LocalServerPort
	private int port;
	private TestRestTemplate template = new TestRestTemplate();
	
	
	@Autowired
	private CabTripService service;
	
	private Collection<CabTrip> trips;
	
	
	@Before
	public void setup() {
		trips = new ArrayList<>();
		trips.add(new CabTripBuilder().withRandomTripId().build());
		trips.add(new CabTripBuilder().withRandomTripId().build());
		trips.add(new CabTripBuilder().withRandomTripId().build());
		trips.add(new CabTripBuilder().withTripId(new TripId("ABC", LocalDateTime.now().plusSeconds(10))).build());
		trips.add(new CabTripBuilder().withTripId(new TripId("ABC", LocalDateTime.now().plusSeconds(20))).build());
		trips.add(new CabTripBuilder().withTripId(new TripId("ABC", LocalDateTime.now().plusSeconds(30))).build());
		trips.add(new CabTripBuilder().withTripId(new TripId("DEF", LocalDateTime.now().plusSeconds(40))).build());
		trips.add(new CabTripBuilder().withTripId(new TripId("DEF", LocalDateTime.now().plusSeconds(50))).build());
		trips.add(new CabTripBuilder().withTripId(new TripId("DEF", LocalDateTime.now().plusMinutes(1))).build());
		service.createTrips(trips);
	}
	
	@After
	public void tearDown() throws Exception {
		service.removeTrips();
	}
	
	private String createURL(String uri) {
		return LOCAL_HOST + port + uri;
	}

	@Test
	public void givenThreeTripsToday_whenFindTripsCalled_thenReturnsCount3() throws Exception {
		ResponseEntity<TripReportModel> response = template.getForEntity(createURL("/cabtrip/tripCount/medallion/ABC/on/"+Utility.formatter.format(LocalDate.now())), TripReportModel.class);
		assertEquals(3, response.getBody().getTripCount().intValue());
	}
	
	@Test
	public void given3TripsTodayForTwoMedallions_whenAskedToReportTripsForCabs_shouldReturnReportWithCount3ForEach() throws Exception {
		List<TripReportModel> reports = template.exchange(createURL("/cabtrip/tripsFor?medallion=ABC&medallion=DEF"), HttpMethod.GET, null, new ParameterizedTypeReference<List<TripReportModel>>() {}).getBody();
		assertEquals(3, reports.stream().filter(report -> report.getMedallion().equals("ABC")).findFirst().get().getTripCount().intValue());
		assertEquals(3, reports.stream().filter(report -> report.getMedallion().equals("DEF")).findFirst().get().getTripCount().intValue());		
	}
    
}
