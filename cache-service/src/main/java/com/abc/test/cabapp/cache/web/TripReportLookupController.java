package com.abc.test.cabapp.cache.web;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import com.abc.test.cabapp.common.model.TripReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.test.cabapp.cache.service.TripReportLookupService;

@RestController
public class TripReportLookupController {
	private TripReportLookupService lookupService;
	
	public TripReportLookupController(@Autowired TripReportLookupService lookupService) {
		this.lookupService = lookupService;
	}
	
	@GetMapping("/tripreport/tripCount/medallion/{medallion}/on/{date}")
	public TripReportModel findTripReport(@PathVariable String medallion, @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date date) {
		return lookupService.lookupTripReport(medallion, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}
	
	@GetMapping("/tripreport/tripsFor")
	public Collection<TripReportModel> findReportsForMedallions(@RequestParam("medallion") String[] medallions){
		return lookupService.lookupTripReportForToday(medallions);
	}
	
}
