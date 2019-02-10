package com.abc.test.cabapp.persistence.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.persistence.model.CabTrip;
import com.abc.test.cabapp.persistence.repository.CabTripRepository;
import com.abc.test.cabapp.persistence.service.CabTripService;

@Service
public class CabTripJPAService implements CabTripService{
	
	private CabTripRepository cabTripRepo;
	
	public CabTripJPAService(@Autowired CabTripRepository cabTripRepo) {
		super();
		this.cabTripRepo = cabTripRepo;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<CabTrip> dailyTripsOfACab(String medallion, LocalDate date) {
		return cabTripRepo.numberOfTripsOnADayByACab(medallion, date.atStartOfDay(), date.atTime(LocalTime.MAX));
	}

	@Override
	@Transactional
	public void createTrips(Collection<CabTrip> trips) {
		cabTripRepo.saveAll(trips);
		
	}

	@Transactional
	public void removeTrips() {
		cabTripRepo.deleteAll();
		
	}

	@Override
	public Collection<TripReportModel> findTripCountsOfCabs(String[] medallions, LocalDate date) {
		return cabTripRepo.numberOfTripsOnADayForCabs(Arrays.asList(medallions), date.atStartOfDay(), date.atTime(LocalTime.MAX));
	}


}
