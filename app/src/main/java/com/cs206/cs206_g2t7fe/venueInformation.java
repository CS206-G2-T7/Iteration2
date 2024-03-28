package com.cs206.cs206_g2t7fe;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class venueInformation {

    private String locationID;
    private String locationName;
    private String venueAddress;

    public venueInformation(String locationID, String locationName, String venueAddress){
        this.locationID = locationID;
        this.locationName = locationName;
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

}