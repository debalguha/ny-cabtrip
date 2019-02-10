package com.abc.test.cabapp.persistence;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

import com.abc.test.cabapp.persistence.model.CabTrip;
import com.abc.test.cabapp.persistence.model.TripId;

public class CabTripBuilder {
	private TripId tripId;
	
	public CabTripBuilder() {}
	
	public CabTripBuilder withTripId(TripId tripId) {
		this.tripId = tripId;
		return this;
	}
	
	public CabTripBuilder withRandomTripId() {
		this.tripId = new TripId(RandomStringUtils.random(10, true, true), LocalDateTime.now());
		return this;
	}
	
	public CabTrip build() {
		return new CabTrip(tripId, "test", "test", 123, "test", Date.from(Instant.now()), 100, 
				100, 10d, 1d, 1d, 1d, 1d);
	}
}
