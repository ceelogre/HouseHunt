package com.example.jmugyenyi.mychat.utils;



import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jmugyenyi.mychat.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.jmugyenyi.mychat.model.House;
import com.example.jmugyenyi.mychat.model.Room;

import static android.support.constraint.Constraints.TAG;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class HouseCRUD  implements HouseMaker{

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



    public HouseCRUD(FirebaseAuth authenticatedUser){
        this.authenticatedUser = authenticatedUser;
        databaseReference =FirebaseDatabase.getInstance().getReference("House");
        //houseId = databaseReference.child("latitude").toString();
    }

    public void createHouseCollection(String givenHouseName,String houseStreet,String houseCity,
                                      String houseCountry,String houseNumberOfRooms,
                                      String houseNumberOfMates,String houseRent){

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
        String chat = authenticatedUser.getCurrentUser().getUid()+"-"+givenHouseName;

        //setHouseName(name);

        //Userid
        String authenticatedUserId = authenticatedUser.getCurrentUser().getUid();

        //Create a house id
        DatabaseReference newRef = databaseReference.push();
        houseId =newRef.getKey();

        house = new House(latitude, longitude, wholeHouseRent, numberOfRooms, numberOfHousemates, housePicLocation, city, country, street
                , authenticatedUserId, houseId, name, chat);

        newRef.setValue(house);

        //Add an association between this house and the user who created it
        databaseReference =FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(authenticatedUserId).child("chat").setValue(chat);
        databaseReference.child(authenticatedUserId).child("house").child(houseId).setValue(true);
        databaseReference.child(authenticatedUserId).child("My House").setValue(houseId);
        FirebaseDatabase.getInstance().getReference("House").child(houseId).child("HouseMates").child(authenticatedUserId).child("Mates").setValue("Yes");

    }


    public House getSpecificHouse(String houseid){

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

    public void addRoomToHouse(){

        Double Price = 134000.0;
        String RoomID;
        String HouseID = getHouseId();
        String Description = "Alert! you may not wake up in this room...";
        String PicFileLocation = "/pic/file/here";

        databaseReference =FirebaseDatabase.getInstance().getReference().child("Rooms");

        DatabaseReference newRef = databaseReference.push();
        RoomID = newRef.getKey();
        Room room = new Room(Price, RoomID, houseId, Description, PicFileLocation);

        //Add a room to database
        newRef.setValue(room);

        //Add a ref in the house collection
        databaseReference =FirebaseDatabase.getInstance().getReference("House");
        databaseReference.child(getHouseId()).child("rooms").child(RoomID).setValue(true);

    }


}
