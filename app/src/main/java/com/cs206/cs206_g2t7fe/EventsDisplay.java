package com.cs206.cs206_g2t7fe;

import java.util.ArrayList;

public class EventsDisplay {
    private String name;
    private Long date;

    private String eventID;

    private String eventOwner;

    private ArrayList<String>eventVenues;

    public EventsDisplay(String name, String eventID, Long date, String eventOwner, ArrayList<String> eventVenues){
        this.name = name;
        this.date = date;
        this.eventID = eventID;
        this.eventOwner = eventOwner;
        this.eventVenues = eventVenues;
    }

    public String getName() {
        return name;
    }

    public Long getDate() {
        return date;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventOwner(){
        return eventOwner;
    }
}
