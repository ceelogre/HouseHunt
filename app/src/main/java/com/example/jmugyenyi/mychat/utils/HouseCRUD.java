package com.example.jmugyenyi.mychat.utils;



import android.util.Log;

import com.example.jmugyenyi.mychat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.jmugyenyi.mychat.model.House;
import com.example.jmugyenyi.mychat.model.Room;

import static android.support.constraint.Constraints.TAG;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class HouseCRUD {

    private DatabaseReference databaseReference, innerDatabaseReference;

    private House house;
    private FirebaseAuth authenticatedUser;

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    private String houseId;

    public HouseCRUD(FirebaseAuth authenticatedUser){
        this.authenticatedUser = authenticatedUser;
        databaseReference =FirebaseDatabase.getInstance().getReference("House");
        //houseId = databaseReference.child("latitude").toString();
    }

    public void createHouseCollection(String houseName,String houseStreet,String houseCity,
                                      String houseCountry,String houseNumberOfRooms,
                                      String houseNumberOfMates,String houseRent){


         Double latitude = -1.94993;
         Double longitude = 32.343;
         Double wholeHouseRent = Double.parseDouble(houseRent) ;

         int numberOfRooms = Integer.parseInt(houseNumberOfRooms);
         int numberOfHousemates = Integer.parseInt(houseNumberOfMates);

         String housePicLocation = "/files/pictures/house44";
         String city = houseCity;
         String country = houseCountry;
         String street = houseStreet;
         String name = houseName;

        //Userid
        String authenticatedUserId =    authenticatedUser.getCurrentUser().getUid();

        //Create a house id
        houseId = databaseReference.push().getKey();
        house = new House(latitude, longitude, wholeHouseRent, numberOfRooms, numberOfHousemates, housePicLocation, city, country, street, authenticatedUserId, houseId,name);

        databaseReference.push().setValue(house);

        //Add an association between this house and the user who created it
        databaseReference =FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(authenticatedUserId).child("house").child(houseId).setValue(true);


        Log.d("Next cursor id", houseId);
    }

    public House getSpecificHouse(String houseToFind){
        return null;
    }

    public ArrayList<House> getAvailableHouses(){
        // Read from the database
        innerDatabaseReference =FirebaseDatabase.getInstance().getReference("Users");

        ArrayList<User> users = new ArrayList<User>();
        final ArrayList<House> availableHouses = new ArrayList<House>();

        innerDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                //check if the user IS A HOUSE HEAD, otherwise it will crash
                //Get the userId and use it to get the users they've posted

                Log.d("This user", user.toString());
                String userId = innerDatabaseReference.child(user.getUid()).getKey();

                //Get the house posted by this user
                innerDatabaseReference =FirebaseDatabase.getInstance().getReference("House").child(userId);
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        House house = dataSnapshot.getValue(House.class);
                        Log.d("Houses", house.toString());
                        availableHouses.add(house);
                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                innerDatabaseReference.addValueEventListener(postListener);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return availableHouses;
    }

    public void addRoomToHouse(){

        Double Price = 83000.0;
        String RoomID;
        String HouseID = getHouseId();
        String Description = "This room is sooooo.........";
        String PicFileLocation = "/pic/file/here";

        databaseReference =FirebaseDatabase.getInstance().getReference().child("Rooms");

        RoomID = databaseReference.push().getKey();
        Room room = new Room(Price, RoomID, houseId, Description, PicFileLocation);

        databaseReference.push().setValue(room);

    }

}
