package com.cs206.cs206_g2t7fe;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserEvents {
    private String userID;

    private String eventID;

    private String eventName;

    private Long eventDate;

    private ArrayList<String> eventLocation;

    private ArrayList<String> invitees;

    public UserEvents(){

    }

    //Getter And Setter Methods Here

    public String getEventID() {
        return eventID;
    }

    public String getEventName(){
        return eventName;
    }

    public String getUserID(){
        return userID;
    }

    public Long getEventDate(){
        return eventDate;
    }

    public ArrayList<String> getEventVenue() {return eventLocation;}

    public ArrayList<String> getEventInvitees() {return invitees;}

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public void setEventDate(Long eventDate){
        this.eventDate = eventDate;
    }

    public void setEventLocation(ArrayList<String> eventLocation) {this.eventLocation = eventLocation;}

    public void setEventInvitees(ArrayList<String> invitees) {this.invitees = invitees;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("eventID", eventID);
        result.put("eventName", eventName);
        result.put("userID", userID);
        result.put("eventDate", eventDate);
        result.put("eventLocation", eventLocation);
        result.put("eventInvitees", invitees);
        return result;
    }

}
