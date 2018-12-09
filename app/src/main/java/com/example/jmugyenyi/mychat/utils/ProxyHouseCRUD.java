package com.example.jmugyenyi.mychat.utils;

import com.google.firebase.auth.FirebaseAuth;

/**
 * * @author Joel Mugyenyi
 * <p>
 * Andrew ID: jmugyeny
 * <p>
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
public class ProxyHouseCRUD implements  HouseMaker {

    private FirebaseAuth mfirebaseAuth;

    private HouseCRUD houseCRUD;

    public ProxyHouseCRUD(FirebaseAuth mfirebaseAuth) {
        this.mfirebaseAuth = mfirebaseAuth;
    }

    @Override
    public void createHouseCollection(String givenHouseName, String houseStreet, String houseCity,
                                      String houseCountry, String houseNumberOfRooms,
                                      String houseNumberOfMates, String houseRent) {


        mfirebaseAuth = FirebaseAuth.getInstance();


        if(houseCRUD == null){
            houseCRUD = new HouseCRUD(this.mfirebaseAuth);
        }

        houseCRUD.createHouseCollection( givenHouseName,  houseStreet,  houseCity,
                 houseCountry,  houseNumberOfRooms,
                 houseNumberOfMates,  houseRent);

    }

    @Override
    public void addRoomToHouse() {


        if(houseCRUD == null){
            houseCRUD = new HouseCRUD(this.mfirebaseAuth);
        }

        houseCRUD.addRoomToHouse();
    }
}
