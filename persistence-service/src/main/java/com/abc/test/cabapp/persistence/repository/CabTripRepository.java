package com.abc.test.cabapp.persistence.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.abc.test.cabapp.common.model.TripReportModel;
import com.abc.test.cabapp.persistence.model.CabTrip;
import com.abc.test.cabapp.persistence.model.TripId;

public interface CabTripRepository extends CrudRepository<CabTrip, TripId>{
	@Query("select t from CabTrip t where t.id.medallion = :medallion and t.id.pickupDateTime >= :beginDate and t.id.pickupDateTime <= :endDate")
	Collection<CabTrip> numberOfTripsOnADayByACab(@Param("medallion") String medallion, @Param("beginDate") LocalDateTime beginDate, 
			@Param("endDate") LocalDateTime endDate);
	@Query("select new com.abc.test.cabapp.common.model.TripReportModel(t.id.medallion, count(*)) from CabTrip t where t.id.medallion in (:medallions) and t.id.pickupDateTime >= :beginDate and t.id.pickupDateTime <= :endDate group by t.id.medallion")
	Collection<TripReportModel> numberOfTripsOnADayForCabs(@Param("medallions") List<String> medallions, @Param("beginDate") LocalDateTime beginDate, 
			@Param("endDate") LocalDateTime endDate);
}
