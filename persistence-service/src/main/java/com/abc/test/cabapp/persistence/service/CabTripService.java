package com.abc.test.cabapp.persistence.service;

import java.time.LocalDate;
import java.util.Collection;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.persistence.model.CabTrip;

public interface CabTripService {
	Collection<CabTrip> dailyTripsOfACab(String medallion, LocalDate date);
	void createTrips(Collection<CabTrip> trips);
	void removeTrips();
	Collection<TripReportModel> findTripCountsOfCabs(String[] medallions, LocalDate date);
}
