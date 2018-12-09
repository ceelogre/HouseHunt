package com.example.jmugyenyi.HouseHunt.utils;

import android.util.Log;

import com.example.jmugyenyi.HouseHunt.model.Interest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//This class creates and uses records about the houses in which the seeker is interested
public class InterestCRUD {

    protected static final String TAG = "InterestCRUD";

    private DatabaseReference databaseReference, userRef;

    private Interest interest;
    private FirebaseAuth authenticatedUser;

    private String interestId;


    public InterestCRUD(FirebaseAuth authenticatedUser) {

        this.authenticatedUser = authenticatedUser;
        databaseReference = FirebaseDatabase.getInstance().getReference("Interest");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    /**
     * The method belows associate the seeker to the house they are interested in based on the parameters below
     *
     * @param _seekerID
     * @param _houseID
     * @param _ownerID
     */
    public void createInterestTable(String _seekerID, String _houseID, String _ownerID) {

        String _status = "pending";

        String authenticatedUserId = authenticatedUser.getCurrentUser().getUid();

        //Add an association between this house and the user who created it
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(authenticatedUserId).child("houses").child(_houseID).child("Request").setValue("Pending");
        databaseReference.child(_ownerID).child("seekers").child(_seekerID).child("HouseID").setValue(_houseID);

        Log.d(TAG, "createInterestTable: " + _seekerID);

    }


    public String getInterestId() {
        return interestId;
    }

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }

}
