package com.abc.test.cabapp.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "cab_trip_data")
public class CabTrip {
	@EmbeddedId
	private TripId id;
	@Column(name = "hack_license", columnDefinition="TEXT", nullable = false)
	private String hackLicense;
	@Column(name = "vendor_id", columnDefinition="TEXT", nullable = false)
	private String vendorId;
	@Column(name = "rate_code")
	private Integer rateCode;
	@Column(name = "store_and_fwd_flag", columnDefinition="TEXT", nullable = false)
	private String storeAndForwardFlag;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dropoff_datetime")
	private Date dropOffDateTime;
	@Column(name = "passenger_count")
	private Integer passengerCount;
	@Column(name = "trip_time_in_secs")
	private Integer tripTimeInSecs;
	@Column(name = "trip_distance")
	private Double tripDistance;
	@Column(name = "pickup_longitude")
	private Double pickupLongitude;
	@Column(name = "pickup_latitude")
	private Double pickupLatitude;
	@Column(name = "dropoff_longitude")
	private Double dropoffLongitude;
	@Column(name = "dropoff_latitude")
	private Double dropoffLatitude;
	
	public CabTrip() {}
	
	public CabTrip(TripId id, String hackLicense, String vendorId, Integer rateCode, String storeAndForwardFlag,
			Date dropoff_datetime, Integer passengerCount, Integer tripTimeInSecs,
			Double tripDistance, Double pickupLongitude, Double pickupLatitude, Double dropoffLongitude,
			Double dropoffLatitude) {
		super();
		this.id = id;
		this.hackLicense = hackLicense;
		this.vendorId = vendorId;
		this.rateCode = rateCode;
		this.storeAndForwardFlag = storeAndForwardFlag;
		this.dropOffDateTime = dropoff_datetime;
		this.passengerCount = passengerCount;
		this.tripTimeInSecs = tripTimeInSecs;
		this.tripDistance = tripDistance;
		this.pickupLongitude = pickupLongitude;
		this.pickupLatitude = pickupLatitude;
		this.dropoffLongitude = dropoffLongitude;
		this.dropoffLatitude = dropoffLatitude;
	}

	public TripId getId() {
		return id;
	}
	public String getHackLicense() {
		return hackLicense;
	}

	public String getVendorId() {
		return vendorId;
	}

	public Integer getRateCode() {
		return rateCode;
	}

	public String getStoreAndForwardFlag() {
		return storeAndForwardFlag;
	}

	public Date getDropoff_datetime() {
		return dropOffDateTime;
	}

	public Integer getPassengerCount() {
		return passengerCount;
	}

	public Integer getTripTimeInSecs() {
		return tripTimeInSecs;
	}

	public Double getTripDistance() {
		return tripDistance;
	}

	public Double getPickupLongitude() {
		return pickupLongitude;
	}

	public Double getPickupLatitude() {
		return pickupLatitude;
	}

	public Double getDropoffLongitude() {
		return dropoffLongitude;
	}

	public Double getDropoffLatitude() {
		return dropoffLatitude;
	}

	public void setHackLicense(String hackLicense) {
		this.hackLicense = hackLicense;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public void setRateCode(Integer rateCode) {
		this.rateCode = rateCode;
	}

	public void setStoreAndForwardFlag(String storeAndForwardFlag) {
		this.storeAndForwardFlag = storeAndForwardFlag;
	}

	public void setDropoff_datetime(Date dropoff_datetime) {
		this.dropOffDateTime = dropoff_datetime;
	}

	public void setPassengerCount(Integer passengerCount) {
		this.passengerCount = passengerCount;
	}

	public void setTripTimeInSecs(Integer tripTimeInSecs) {
		this.tripTimeInSecs = tripTimeInSecs;
	}

	public void setTripDistance(Double tripDistance) {
		this.tripDistance = tripDistance;
	}

	public void setPickupLongitude(Double pickupLongitude) {
		this.pickupLongitude = pickupLongitude;
	}

	public void setPickupLatitude(Double pickupLatitude) {
		this.pickupLatitude = pickupLatitude;
	}

	public void setDropoffLongitude(Double dropoffLongitude) {
		this.dropoffLongitude = dropoffLongitude;
	}

	public void setDropoffLatitude(Double dropoffLatitude) {
		this.dropoffLatitude = dropoffLatitude;
	}

	public void setId(TripId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CabTrip other = (CabTrip) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CabTrip [id=" + id + ", hackLicense=" + hackLicense + ", vendorId=" + vendorId + ", rateCode="
				+ rateCode + ", storeAndForwardFlag=" + storeAndForwardFlag + ", dropoff_datetime=" + dropOffDateTime
				+ ", passengerCount=" + passengerCount + ", tripTimeInSecs=" + tripTimeInSecs + ", tripDistance="
				+ tripDistance + ", pickupLongitude=" + pickupLongitude + ", pickupLatitude=" + pickupLatitude
				+ ", dropoffLongitude=" + dropoffLongitude + ", dropoffLatitude=" + dropoffLatitude + "]";
	}
	
}
