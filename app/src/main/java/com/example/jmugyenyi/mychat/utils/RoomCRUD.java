package com.example.jmugyenyi.mychat.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmugyenyi.mychat.model.User;
import com.example.jmugyenyi.mychat.model.House;
import com.example.jmugyenyi.mychat.model.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class RoomCRUD {

    private DatabaseReference databaseReference, innerDatabaseReference;
    //private FirebaseAuth authenticatedUser;
    private Room room;

    private String roomID;

    public RoomCRUD(){
        //this.authenticatedUser = authenticatedUser;
        databaseReference =FirebaseDatabase.getInstance().getReference("Room");

    }

    public void createRoomCollection(String houseId, String description, Double price){

        String picFileLocation = "/Pic/File/here/";
        DatabaseReference newDbReference = databaseReference.push();
        roomID = newDbReference.getKey();


        Room room = new Room(roomID, houseId, description, picFileLocation, price);
        newDbReference.setValue(room);

        //Add a ref in the house collection
        databaseReference =FirebaseDatabase.getInstance().getReference("House");
        databaseReference.child(houseId).child("rooms").child(roomID).setValue(true);


    }

    public Room getRoomDetails(final String houseid){


        innerDatabaseReference =FirebaseDatabase.getInstance().getReference("Rooms");
        innerDatabaseReference.child(houseid).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.hasChild("houseID")){
                                room = dataSnapshot.getValue(Room.class);
                                Log.d("been", room.toString());
                            }
                            else{
                                Log.d("res", "No ooo");
                            }

                        }
                        else Log.d("datasnapshot", "Doesn't exist.");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

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
}
