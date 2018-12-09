package com.example.jmugyenyi.HouseHunt.utils;


public interface HouseMaker {

    void createHouseCollection(String givenHouseName,String houseStreet,String houseCity,
                               String houseCountry,String houseNumberOfRooms,
                               String houseNumberOfMates,String houseRent);

    void addRoomToHouse();
}
