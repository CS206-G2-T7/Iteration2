package com.cs206.cs206_g2t7fe;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInformation {
    private String userID;

    private String userName;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String dateJoined;

    private ArrayList<String> initalPreference;

    private Boolean quizDone;

    public UserInformation(){

    }

    public String getUserID() {
        return userID;
    }
    public String getUserName() {return userName;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getDateJoined() {return dateJoined;}
    public String getDateOfBirth() {return dateOfBirth;}
    public ArrayList<String> getInitalPreference() {return initalPreference;}
    public Boolean getQuizDone() {return quizDone;}
    public void setUserID(String userID) {this.userID = userID;}
    public void setUserName(String userName) {this.userName = userName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setDateOfBirth(String dateOfBirth) {this.dateOfBirth = dateOfBirth;}
    public void setDateJoined(String dateJoined) {this.dateJoined = dateJoined;}
    public void setQuizDone(Boolean quizDone) {this.quizDone = quizDone;}
    public void setInitalPreference(ArrayList<String> initalPreference) {this.initalPreference = initalPreference;}

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("userName", userName);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("dateOfBirth", dateOfBirth);
        result.put("dateJoined", dateJoined);
        result.put("initialPreference", initalPreference);
        result.put("quizDone", quizDone);
        return result;
    }
}
