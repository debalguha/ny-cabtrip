package com.abc.test.cabapp.persistence.service.impl;

import static com.abc.test.cabapp.persistence.TestUtils.getARandomCabTrip;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.persistence.model.CabTrip;
import com.abc.test.cabapp.persistence.repository.CabTripRepository;
import com.abc.test.cabapp.persistence.service.CabTripService;

public class CabTripJPAServiceTest {
	@Mock
	private CabTripRepository repo;
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void shouldReturnAnIntegerWhenAskedToCountNumberOfTripsOnAGivenDayForACab() throws Exception {
		CabTripService service = new CabTripJPAService(repo);
		List<CabTrip> cabTrips = Lists.list(getARandomCabTrip(), getARandomCabTrip(), getARandomCabTrip());
		when(repo.numberOfTripsOnADayByACab(anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(cabTrips);
		assertEquals(3, service.dailyTripsOfACab("ABC", LocalDate.now()).size());
	}
	@SuppressWarnings("unchecked")
	@Test
	public void shouldReturn20WhenAskedToCountNumberOfTripsForCabs() throws Exception {
		CabTripService service = new CabTripJPAService(repo);
		when(repo.numberOfTripsOnADayForCabs(any(List.class), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Lists.list(new TripReportModel("ABC", new Date(), 20),
				new TripReportModel("DEF", new Date(), 30)));
		assertEquals(20, service.findTripCountsOfCabs(new String[]{"ABC", "DEF"}, LocalDate.now()).stream().filter(trip -> trip.getMedallion().equals("ABC")).findFirst().get().getTripCount().intValue());
	}
	@Test
	public void verifyStartAndEndOfDayWerePassedToTheRepositoryMethod() throws Exception {
		CabTripService service = new CabTripJPAService(repo);
		service.dailyTripsOfACab("ABC", LocalDate.now());
		verify(repo).numberOfTripsOnADayByACab(anyString(), 
				argThat((LocalDateTime date) -> date.getHour() == 0), 
				argThat((LocalDateTime date) -> date.getHour() == 23));
	}
	
}
