package com.abc.test.cabapp.cache.web;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import com.abc.test.cabapp.common.model.TripReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.test.cabapp.cache.service.TripReportLookupService;

@RestController
public class TripReportLookupController {
	private TripReportLookupService lookupService;
	//private CacheManager cacheManager;
	
	public TripReportLookupController(@Autowired TripReportLookupService lookupService) {
		this.lookupService = lookupService;
		//this.cacheManager = cacheManager;
	}

	@GetMapping("/tripreport/cache/clear")
	@CacheEvict(value = {"trip-report", "trip-reports"}, allEntries = true)
	public void clearTripReportCache(){}

	@GetMapping("/tripreport/tripCount/medallion/{medallion}/on/{date}")
	public TripReportModel findTripReport(@PathVariable String medallion, @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date date, @RequestParam(name = "noCache", required = false) boolean noCache) {
		if(noCache){
			return lookupService.lookupTripReport(medallion, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		} else{
			return lookupService.lookupTripReportFromCache(medallion, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
	}
	
	@GetMapping("/tripreport/tripsFor")
	public Collection<TripReportModel> findReportsForMedallions(@RequestParam("medallion") String[] medallions, @RequestParam(name = "noCache", required = false) boolean noCache){
		if(noCache) {
			return lookupService.lookupTripReportForToday(medallions);
		} else {
			return lookupService.lookupTripReportForTodayFromCache(medallions);
		}
	}
	
}
