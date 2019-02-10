package com.abc.test.cabapp.cache.service;

public enum Endpoint {
	REPORT_SINGLE_ENDPT("/cabtrip/tripCount/medallion/{medallion}/on/{date}", null), REPORT_MULTI_ENDPT("/cabtrip/tripCount", new String[] {"medallions"});
	
	private final String endpointUri;
	private final String [] queryParameterNames;
	
	Endpoint(String endpointUri, String [] queryParameterNames){
		this.endpointUri = endpointUri;
		this.queryParameterNames = queryParameterNames;
	}
	
	public String[] getQueryParameterNames() {
		return queryParameterNames;
	}

	public String getEndpointUri() {return endpointUri;}
}
