package com.abc.test.cabapp.persistence.web;

import static com.abc.test.cabapp.persistence.TestUtils.getARandomCabTrip;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.persistence.model.CabTrip;
import com.abc.test.cabapp.persistence.service.CabTripService;

@RunWith(SpringRunner.class)
@WebMvcTest(PersistenceServiceController.class)
public class PersistenceServiceControllerTest {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CabTripService service;
	
	@Test
	public void shouldBeAbleToInvokeServiceForAMedallionAndReturnJson() throws Exception {
		List<CabTrip> cabTrips = Lists.list(getARandomCabTrip(), getARandomCabTrip(), getARandomCabTrip());
		when(service.dailyTripsOfACab(anyString(), any(LocalDate.class))).thenReturn(cabTrips);
		mvc.perform(get("/cabtrip/tripCount/medallion/123/on/20190206")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.tripCount", equalTo(3)));
	}
	
	@Test
	public void shouldBeAbleToInvokeServiceFor2MedallionsAndReturnJson() throws Exception {
		List<TripReportModel> reports = Lists.list(new TripReportModel("ABC", 3L), new TripReportModel("DEF", 3L));
		when(service.findTripCountsOfCabs(any(String[].class), any(LocalDate.class))).thenReturn(reports);
		mvc.perform(get("/cabtrip/tripsFor").param("medallion", "ABC").param("medallion", "DEF")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.size()", equalTo(2)));
	}

}
