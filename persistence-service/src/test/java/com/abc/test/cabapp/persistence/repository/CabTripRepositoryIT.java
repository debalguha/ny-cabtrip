package com.abc.test.cabapp.persistence.repository;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.persistence.CabTripBuilder;
import com.abc.test.cabapp.persistence.model.CabTrip;
import com.abc.test.cabapp.persistence.model.TripId;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CabTripRepositoryIT {
	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
	private CabTripRepository repo;
	
	private List<CabTrip> trips;
	
	@Before
	public void setup() throws Exception {
		trips = Lists.list(new CabTripBuilder().withRandomTripId().build(), new CabTripBuilder().withRandomTripId().build(), new CabTripBuilder().withRandomTripId().build(),
				new CabTripBuilder().withTripId(new TripId("ABC", LocalDateTime.now().plusSeconds(10))).build(),
				new CabTripBuilder().withTripId(new TripId("ABC", LocalDateTime.now().plusSeconds(20))).build(),
				new CabTripBuilder().withTripId(new TripId("ABC", LocalDateTime.now().plusSeconds(30))).build(),
				new CabTripBuilder().withTripId(new TripId("DEF", LocalDateTime.now().plusSeconds(10))).build(),
				new CabTripBuilder().withTripId(new TripId("DEF", LocalDateTime.now().plusSeconds(20))).build(),
				new CabTripBuilder().withTripId(new TripId("DEF", LocalDateTime.now().plusSeconds(30))).build());
		trips.forEach(trip -> entityManager.persist(trip));
		entityManager.flush();
	}
	
	@After
	public void tearDown() {
		trips.forEach(trip -> entityManager.remove(trip));
		entityManager.flush();
	}
	
	@Test
	public void shouldCountTheNumberOfTripsToday() throws Exception {
		LocalDateTime beginDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime endDate = LocalDate.now().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toLocalDateTime();
		assertEquals(3, repo.numberOfTripsOnADayByACab("ABC", beginDate, endDate).size());
	}
	
	@Test
	public void shouldcountTheNumberOfTripsForCabsToday() throws Exception {
		LocalDateTime beginDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime endDate = LocalDate.now().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toLocalDateTime();
		Collection<TripReportModel> trips = repo.numberOfTripsOnADayForCabs(Lists.list("ABC", "DEF"), beginDate, endDate);
		assertEquals(2,  trips.size());
	}
}
