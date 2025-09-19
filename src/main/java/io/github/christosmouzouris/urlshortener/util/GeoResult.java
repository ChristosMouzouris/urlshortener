package io.github.christosmouzouris.urlshortener.util;

import java.util.Map;

public class GeoResult {

    private final String countryCode;
    private final Map<String, String> locationDetails;

    public GeoResult(String countryCode, Map<String, String> locationDetails) {
        this.countryCode = countryCode;
        this.locationDetails = locationDetails;
    }

    public String getCountryCode() {return countryCode;}

    public Map<String, String> getLocationDetails() {return locationDetails;}
}
