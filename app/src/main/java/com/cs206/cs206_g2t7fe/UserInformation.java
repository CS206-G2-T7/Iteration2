package com.cs206.cs206_g2t7fe;

public class UserInformation {
    private String userID;

    private String userName;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String dateJoined;

    private String[] initalPreference;

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
    public String[] getInitalPreference() {return initalPreference;}
    public Boolean getQuizDone() {return quizDone;}
    public void setUserID(String userID) {this.userID = userID;}
    public void setUserName(String userName) {this.userName = userName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setDateOfBirth(String dateOfBirth) {this.dateOfBirth = dateOfBirth;}
    public void setDateJoined(String dateJoined) {this.dateJoined = dateJoined;}
    public void setQuizDone(Boolean quizDone) {this.quizDone = quizDone;}
    public void setInitalPreference(String[] initalPreference) {this.initalPreference = initalPreference;}
}
