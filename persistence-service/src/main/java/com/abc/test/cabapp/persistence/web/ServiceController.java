package com.abc.test.cabapp.persistence.web;

import java.util.Collection;
import java.util.Date;

import com.abc.test.cabapp.common.model.TripReportModel;

public interface ServiceController {
	TripReportModel findTripsMadeByACabOn(String medallion, Date date);

	Collection<TripReportModel> findTripsForCabsToday(String[] medallions);
}
