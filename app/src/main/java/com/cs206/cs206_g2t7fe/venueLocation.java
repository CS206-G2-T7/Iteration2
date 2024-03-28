package com.cs206.cs206_g2t7fe;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class venueLocation {

    private String locationID;
    private String locationName;
    private String venueAddress;

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getLocationID() {
        return locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("locationID", locationID);
        result.put("locationName", locationName);
        result.put("locationAddress", venueAddress);
        return result;
    }
}
