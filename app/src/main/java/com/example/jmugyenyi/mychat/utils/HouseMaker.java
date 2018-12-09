package com.example.jmugyenyi.mychat.utils;

/**
 * * @author Joel Mugyenyi
 * <p>
 * Andrew ID: jmugyeny
 * <p>
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
public interface HouseMaker {

    void createHouseCollection(String givenHouseName,String houseStreet,String houseCity,
                               String houseCountry,String houseNumberOfRooms,
                               String houseNumberOfMates,String houseRent);

    void addRoomToHouse();
}
