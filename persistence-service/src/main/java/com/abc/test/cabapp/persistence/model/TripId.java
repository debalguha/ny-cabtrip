package com.abc.test.cabapp.persistence.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TripId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "medallion")
	private String medallion;
	
	@Column(name = "pickup_datetime", columnDefinition = "TIMESTAMP")
	private LocalDateTime pickupDateTime;

	public TripId(String medallion, LocalDateTime pickupDateTime) {
		super();
		this.medallion = medallion;
		this.pickupDateTime = pickupDateTime;
	}
	
	public TripId() {}

	public String getMedallion() {
		return medallion;
	}

	public void setMedallion(String medallion) {
		this.medallion = medallion;
	}

	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}

	public void setPickupDateTime(LocalDateTime pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((medallion == null) ? 0 : medallion.hashCode());
		result = prime * result + ((pickupDateTime == null) ? 0 : pickupDateTime.hashCode());
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
		TripId other = (TripId) obj;
		if (medallion == null) {
			if (other.medallion != null)
				return false;
		} else if (!medallion.equals(other.medallion))
			return false;
		if (pickupDateTime == null) {
			if (other.pickupDateTime != null)
				return false;
		} else if (!pickupDateTime.equals(other.pickupDateTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TripId [medallion=" + medallion + ", pickupDateTime=" + pickupDateTime + "]";
	}
}
