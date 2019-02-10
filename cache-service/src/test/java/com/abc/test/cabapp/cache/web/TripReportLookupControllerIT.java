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
import java.util.concurrent.ConcurrentHashMap;

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
import org.springframework.cache.concurrent.ConcurrentMapCache;
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
	public void givenAMedallionAndADate_whenFindTripReportCalledWithCache_thenReturnsTripCount() throws Exception {
		assertEquals(3, invokeSingleMedallionServiceWithCache().getTripCount().intValue());
		assertEquals(3, invokeSingleMedallionServiceWithCache().getTripCount().intValue());
		assertEquals(3, invokeSingleMedallionServiceWithCache().getTripCount().intValue());
		assertEquals(3, invokeSingleMedallionServiceWithCache().getTripCount().intValue());
		Mockito.verify(lookupService, Mockito.times(1)).lookupTripReport(anyString(), any(LocalDate.class));
	}
	@Test
	public void givenAListOfmedallions_whenFindTripReportCalledWithCache_thenReturnsTripCountForEachForToday() throws Exception {
		assertEquals(2, invokeMultiMedallionServiceWithCache().size());
		assertEquals(2, invokeMultiMedallionServiceWithCache().size());
		assertEquals(2, invokeMultiMedallionServiceWithCache().size());
		assertEquals(2, invokeMultiMedallionServiceWithCache().size());
		Mockito.verify(lookupService, Mockito.times(1)).lookupTripReportForToday(any(String[].class));
	}

	@Test
	public void givenAMedallionAndADate_whenFindTripReportCalledWithoutCache_thenReturnsTripCount() throws Exception {
		assertEquals(3, invokeSingleMedallionServiceWithoutCache().getTripCount().intValue());
		assertEquals(3, invokeSingleMedallionServiceWithoutCache().getTripCount().intValue());
		assertEquals(3, invokeSingleMedallionServiceWithoutCache().getTripCount().intValue());
		assertEquals(3, invokeSingleMedallionServiceWithoutCache().getTripCount().intValue());
		Mockito.verify(lookupService, Mockito.times(4)).lookupTripReport(anyString(), any(LocalDate.class));
	}
	@Test
	public void givenAListOfmedallions_whenFindTripReportCalledWithoutCache_thenReturnsTripCountForEachForToday() throws Exception {
		assertEquals(2, invokeMultiMedallionServiceWithoutCache().size());
		assertEquals(2, invokeMultiMedallionServiceWithoutCache().size());
		assertEquals(2, invokeMultiMedallionServiceWithoutCache().size());
		assertEquals(2, invokeMultiMedallionServiceWithoutCache().size());
		Mockito.verify(lookupService, Mockito.times(4)).lookupTripReportForToday(any(String[].class));
	}

	@Test
	public void shouldBeAbleToClearAllCache() throws Exception {
		doEvictCacheEndpoint();
		cacheManager.getCacheNames().forEach(cache -> assertEquals(0, ((ConcurrentHashMap)(cacheManager.getCache(cache).getNativeCache())).size()));
	}

	private TripReportModel invokeSingleMedallionServiceWithCache() throws Exception {
		return new RestTemplate().getForEntity("http://localhost:"+port+"/tripreport/tripCount/medallion/ABC/on/"+ dateStr, TripReportModel.class).getBody();
	}
	private TripReportModel invokeSingleMedallionServiceWithoutCache() throws Exception {
		return new RestTemplate().getForEntity("http://localhost:"+port+"/tripreport/tripCount/medallion/ABC/on/"+ dateStr+"?noCache=true", TripReportModel.class).getBody();
	}
	private Collection<TripReportModel> invokeMultiMedallionServiceWithCache() throws Exception {
		return new RestTemplate().exchange("http://localhost:"+port+"/tripreport/tripsFor?medallion=ABC&medallion=DEF", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<TripReportModel>>() {}).getBody();
	}
	private Collection<TripReportModel> invokeMultiMedallionServiceWithoutCache() throws Exception {
		return new RestTemplate().exchange("http://localhost:"+port+"/tripreport/tripsFor?medallion=ABC&medallion=DEF&noCache=true", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<TripReportModel>>() {}).getBody();
	}
	private void doEvictCacheEndpoint() {
		new RestTemplate().getForEntity("http://localhost:"+port+"/tripreport/cache/clear", Void.class);
	}
}
