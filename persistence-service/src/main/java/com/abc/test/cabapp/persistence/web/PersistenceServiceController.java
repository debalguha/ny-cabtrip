package com.abc.test.cabapp.persistence.web;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.persistence.model.CabTrip;
import com.abc.test.cabapp.persistence.service.CabTripService;

@RestController
public class PersistenceServiceController implements ServiceController {
	private CabTripService service;

	public PersistenceServiceController(@Autowired CabTripService service) {
		super();
		this.service = service;
	}

	@Override
	@GetMapping("/cabtrip/tripCount/medallion/{medallion}/on/{date}")
	public TripReportModel findTripsMadeByACabOn(@PathVariable String medallion, @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date date) {
		Collection<CabTrip> dailyTripsOfACab = service.dailyTripsOfACab(medallion,
				date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		return new TripReportModel(medallion, date, dailyTripsOfACab.size());
	}
	
	@Override
	@GetMapping("/cabtrip/tripsFor")
	public Collection<TripReportModel> findTripsForCabsToday(@RequestParam("medallion") String[] medallions) {
		LocalDate date = LocalDate.now();
		return service.findTripCountsOfCabs(medallions, date);
	}
	
}
