package com.abc.test.cabapp.common.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TripReportModel {
	private String medallion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
	private Date tripDate;
	private Integer tripCount;
	public TripReportModel() {}
	public TripReportModel(@JsonProperty("medallion") String medallion, @JsonProperty("tripDate") Date tripDate, 
			@JsonProperty("tripCount") Integer tripCount) {
		super();
		this.medallion = medallion;
		this.tripDate = tripDate;
		this.tripCount = tripCount;
	}
	public TripReportModel(String medallion, Long tripCount) {
		super();
		this.medallion = medallion;
		this.tripDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
		this.tripCount = tripCount.intValue();
	}
	public String getMedallion() {
		return medallion;
	}
	public Integer getTripCount() {
		return tripCount;
	}
	public Date getTripDate() {
		return tripDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((medallion == null) ? 0 : medallion.hashCode());
		result = prime * result + ((tripDate == null) ? 0 : tripDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TripReportModel other = (TripReportModel) obj;
		if (medallion == null) {
			if (other.medallion != null)
				return false;
		} else if (!medallion.equals(other.medallion))
			return false;
		if (tripDate == null) {
			if (other.tripDate != null)
				return false;
		} else if (!tripDate.equals(other.tripDate))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TripReportModel [medallion=" + medallion + ", tripDate=" + tripDate + ", tripCount=" + tripCount + "]";
	}
}
