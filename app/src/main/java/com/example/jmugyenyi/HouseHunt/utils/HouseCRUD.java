package com.example.jmugyenyi.HouseHunt.utils;


import android.util.Log;

import com.example.jmugyenyi.HouseHunt.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.jmugyenyi.HouseHunt.model.House;
import com.example.jmugyenyi.HouseHunt.model.Room;

import static android.support.constraint.Constraints.TAG;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/*
This class represents all the CRUD operations you can perform on a house
it also tests methods used for the database CRUD operations
 */

public class HouseCRUD implements HouseMaker {
    //Database reference
    private DatabaseReference databaseReference, innerDatabaseReference;

    private House house;
    private FirebaseAuth authenticatedUser;
    private String houseId;
    private String houseName;

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getHouseName() {

        return houseName;
    }


    public HouseCRUD(FirebaseAuth authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        databaseReference = FirebaseDatabase.getInstance().getReference("House");
    }

    /**
     * This function creates a database collection given the parameters below
     *
     * @param givenHouseName
     * @param houseStreet
     * @param houseCity
     * @param houseCountry
     * @param houseNumberOfRooms
     * @param houseNumberOfMates
     * @param houseRent
     */
    public void createHouseCollection(String givenHouseName, String houseStreet, String houseCity,
                                      String houseCountry, String houseNumberOfRooms,
                                      String houseNumberOfMates, String houseRent) {

        Double latitude = -1.23463;
        Double longitude = 30.343;
        Double wholeHouseRent = Double.parseDouble(houseRent);

        int numberOfRooms = Integer.parseInt(houseNumberOfRooms);
        int numberOfHousemates = Integer.parseInt(houseNumberOfMates);

        String housePicLocation = "/files/pictures/house11";
        String city = houseCity;
        String country = houseCountry;
        String street = houseStreet;
        String name = givenHouseName;
        String chat = authenticatedUser.getCurrentUser().getUid() + "-" + givenHouseName;

        String authenticatedUserId = authenticatedUser.getCurrentUser().getUid();

        DatabaseReference newRef = databaseReference.push();
        houseId = newRef.getKey();

        house = new House(latitude, longitude, wholeHouseRent, numberOfRooms, numberOfHousemates, housePicLocation, city, country, street
                , authenticatedUserId, houseId, name, chat);

        newRef.setValue(house);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(authenticatedUserId).child("chat").setValue(chat);
        databaseReference.child(authenticatedUserId).child("house").child(houseId).setValue(true);
        databaseReference.child(authenticatedUserId).child("My House").setValue(houseId);
        FirebaseDatabase.getInstance().getReference("House").child(houseId).child("HouseMates").child(authenticatedUserId).child("Mates").setValue("Yes");

    }


    public House getSpecificHouse(String houseid) {

        return null;
    }

    /**
     * This function returns a list of all available houses
     */
    public ArrayList<House> getAvailableHouses() {
        // Read from the database
        innerDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");

        ArrayList<User> users = new ArrayList<User>();
        final ArrayList<House> availableHouses = new ArrayList<House>();

        innerDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d("This user", user.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return availableHouses;
    }

    /**
     * This function is used to add a room corresponding to a just added house
     */
    public void addRoomToHouse() {

        Double Price = 134000.0;
        String RoomID;
        String HouseID = getHouseId();
        String Description = "Alert! you may not wake up in this room...";
        String PicFileLocation = "/pic/file/here";

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Rooms");

        DatabaseReference newRef = databaseReference.push();
        RoomID = newRef.getKey();
        Room room = new Room(Price, "fx", RoomID, houseId, Description, PicFileLocation);

        //Add a room to database
        newRef.setValue(room);

        //Add a ref in the house collection
        databaseReference = FirebaseDatabase.getInstance().getReference("House");
        databaseReference.child(getHouseId()).child("rooms").child(RoomID).setValue(true);

    }

}
