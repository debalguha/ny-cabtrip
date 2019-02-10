package com.abc.test.cabapp.cache.web;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

import com.abc.test.cabapp.cache.service.TripReportLookupService;
import com.abc.test.cabapp.cache.web.TripReportLookupController;
import com.abc.test.cabapp.common.model.TripReportModel;

@RunWith(SpringRunner.class)
@WebMvcTest(TripReportLookupController.class)
public class TripReportLookupControllerTest {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private TripReportLookupService service;
	
	@Test
	public void shouldBeAbleToInvokeServiceAndreturnJson() throws Exception {
		when(service.lookupTripReport(anyString(), any(LocalDate.class))).thenReturn(new TripReportModel("ABC", Date.from(Instant.now()), 5));
		mvc.perform(get("/tripreport/tripCount/medallion/ABC/on/20190206")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.tripCount", equalTo(5)));
	}
	
	@Test
	public void shouldBeAbleToFindReportsForMedallions() throws Exception {
		when(service.lookupTripReportForToday(any(String[].class))).thenReturn(Lists.list(new TripReportModel("ABC", Date.from(Instant.now()), 5),
				new TripReportModel("DEF", Date.from(Instant.now()), 5)));
		mvc.perform(get("/tripreport/tripsFor?medallion=ABC&medallion=DEF")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.size()", equalTo(2)));
	}
}
