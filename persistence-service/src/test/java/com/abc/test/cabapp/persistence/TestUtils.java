package com.abc.test.cabapp.persistence;

import java.time.Instant;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.persistence.model.CabTrip;

import net.bytebuddy.utility.RandomString;

public class TestUtils {
	public static CabTrip getARandomCabTrip() {
		return new CabTripBuilder().withRandomTripId().build();
	}
	public static TripReportModel getArandomtripReport() {
		return new TripReportModel(RandomString.make(10), Date.from(Instant.now()), RandomUtils.nextInt());
	}
}
