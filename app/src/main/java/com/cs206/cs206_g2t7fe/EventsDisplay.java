package com.cs206.cs206_g2t7fe;

public class EventsDisplay {
    private String name;
    private Long date;

    private String eventID;

    private String eventOwner;

    public EventsDisplay(String name, String eventID, Long date, String eventOwner){
        this.name = name;
        this.date = date;
        this.eventID = eventID;
        this.eventOwner = eventOwner;
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
