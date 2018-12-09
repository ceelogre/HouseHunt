package com.example.jmugyenyi.mychat.utils;

import android.util.Log;

import com.example.jmugyenyi.mychat.model.House;
import com.example.jmugyenyi.mychat.model.Interest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * * @author Joel Mugyenyi
 * <p>
 * Andrew ID: jmugyeny
 * <p>
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
public class InterestCRUD {

    protected static final String TAG = "InterestCRUD";

    private DatabaseReference databaseReference,userRef;

    private Interest interest;
    private FirebaseAuth authenticatedUser;

    private String interestId;



    public InterestCRUD(FirebaseAuth authenticatedUser){

        this.authenticatedUser = authenticatedUser;
        databaseReference =FirebaseDatabase.getInstance().getReference("Interest");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    public void createInterestTable(String _seekerID, String _houseID,String _ownerID)
    {

        String _status = "pending";


        //setHouseName(name);

        //Userid
        String authenticatedUserId =    authenticatedUser.getCurrentUser().getUid();

        //Create a house id
//        DatabaseReference newRef = databaseReference.push();
//        interestId =newRef.getKey();
//        interest = new Interest(_houseID,_seekerID,_ownerID,_status);
//
//        newRef.setValue(interest);

        //Add an association between this house and the user who created it
        databaseReference =FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(authenticatedUserId).child("houses").child(_houseID).child("Request").setValue("Pending");
        databaseReference.child(_ownerID).child("seekers").child(_seekerID).child("Request").setValue("Sent");

        Log.d(TAG, "createInterestTable: "+_seekerID);

    }


    public String getInterestId() {
        return interestId;
    }

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }

}
