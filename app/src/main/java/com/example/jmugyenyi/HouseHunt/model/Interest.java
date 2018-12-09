package com.example.jmugyenyi.HouseHunt.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Interest {
   // private String interestID;
    private String houseID;
    private String ownerID;
    private String seekerID;
    private String status;
   // private String authenticatedUserId;



    public Interest(){

    }

    public Interest(String _houseID,String _seekerID,String _ownerID,  String _status)
    {

        this.seekerID = _seekerID;
        this.houseID = _houseID;
        this.status = _status;
        this.ownerID = _ownerID;
    }

//    public String getInterestID() {
//        return interestID;
//    }
//
//    public void setInterestID(String interestID) {
//        this.interestID = interestID;
//    }

    public String getSeekerID() {
        return seekerID;
    }

    public void setSeekerID(String seekerID) {
        this.seekerID = seekerID;
    }

    public String getHouseID() {
        return houseID;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
    }

    public String getStatus() {
        return status;
    }
    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

//    public void setStatus(String status) {
//        this.status = status;
//    }
//    public String getAuthenticatedUserId() {
//        return authenticatedUserId;
//    }
//
//    public void setAuthenticatedUserId(String authenticatedUserId) {
//        this.authenticatedUserId = authenticatedUserId;
   // }





}
