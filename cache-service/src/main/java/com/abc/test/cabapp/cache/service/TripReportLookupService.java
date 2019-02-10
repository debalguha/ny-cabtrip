package com.abc.test.cabapp.cache.service;

import java.time.LocalDate;
import java.util.Collection;

import com.abc.test.cabapp.common.model.TripReportModel;

public interface TripReportLookupService {

	TripReportModel lookupTripReport(String medallion, LocalDate tripDate);

	Collection<TripReportModel> lookupTripReportForToday(String[] medallions);

	TripReportModel lookupTripReportFromCache(String medallion, LocalDate toLocalDate);

	Collection<TripReportModel> lookupTripReportForTodayFromCache(String[] medallions);
}