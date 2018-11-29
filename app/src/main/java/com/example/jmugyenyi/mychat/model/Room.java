package com.example.jmugyenyi.mychat.model;

public class Room {

    private Double Price;

    private String RoomID;
    private String HouseID;
    private String Description;
    private String PicFileLocation;

    public Room(Double price, String roomID, String houseID, String description, String picFileLocation) {
        Price = price;
        RoomID = roomID;
        HouseID = houseID;
        Description = description;
        PicFileLocation = picFileLocation;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public String getRoomID() {
        return RoomID;
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }

    public String getHouseID() {
        return HouseID;
    }

    public void setHouseID(String houseID) {
        HouseID = houseID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicFileLocation() {
        return PicFileLocation;
    }

    public void setPicFileLocation(String picFileLocation) {
        PicFileLocation = picFileLocation;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Room{");
        sb.append("Price=").append(Price);
        sb.append(", RoomID='").append(RoomID).append('\'');
        sb.append(", HouseID='").append(HouseID).append('\'');
        sb.append(", Description='").append(Description).append('\'');
        sb.append(", PicFileLocation='").append(PicFileLocation).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
