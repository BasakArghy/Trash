package com.example.recyclabletrashclassificationapp;

public class UserModel {

    private String userId,userName,userEmail,userProfile;
    public UserModel()
    {

    }

    public UserModel(String userId, String userName,  String userEmail,String userProfile) {
        this.userId = userId;
        this.userName = userName;

        this.userEmail = userEmail;
        this.userProfile=userProfile;


    }


    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
