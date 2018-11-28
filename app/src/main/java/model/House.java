package model;

import java.util.Map;

public class House {

    private Double latitude;
    private Double longitude;
    private Double wholeHouseRent;

    private int numberOfRooms;
    private int numberOfHousemates;

    private String housePicLocation;
    private String city;
    private String country;
    private String street;

    private String houseId;

    public String getAuthenticatedUserId() {
        return authenticatedUserId;
    }

    public void setAuthenticatedUserId(String authenticatedUserId) {
        this.authenticatedUserId = authenticatedUserId;
    }

    private String authenticatedUserId;

    private Map<String, Boolean> houseRoomMap;

    public House(){

    }

    public House(Double latitude, Double longitude, Double wholeHouseRent, int numberOfRooms, int numberOfHousemates, String housePicLocation, String city, String country, String street,
                 String authenticatedUserId, String houseId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.wholeHouseRent = wholeHouseRent;
        this.numberOfRooms = numberOfRooms;
        this.numberOfHousemates = numberOfHousemates;
        this.housePicLocation = housePicLocation;
        this.city = city;
        this.country = country;
        this.street = street;
        this.houseId = houseId;
        this.authenticatedUserId = authenticatedUserId;
    }

    public Map<String, Boolean> getHouseRoomMap() {
        return houseRoomMap;
    }

    public void setHouseRoomMap(Map<String, Boolean> houseRoomMap) {
        this.houseRoomMap = houseRoomMap;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getWholeHouseRent() {
        return wholeHouseRent;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public int getNumberOfHousemates() {
        return numberOfHousemates;
    }

    public String getHousePicLocation() {
        return housePicLocation;
    }

    public String getCity() {
        return city;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getCountry() {
        return country;
    }

    public String getStreet() {
        return street;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setWholeHouseRent(Double wholeHouseRent) {
        this.wholeHouseRent = wholeHouseRent;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setNumberOfHousemates(int numberOfHousemates) {
        this.numberOfHousemates = numberOfHousemates;
    }

    public void setHousePicLocation(String housePicLocation) {
        this.housePicLocation = housePicLocation;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "House{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", wholeHouseRent=" + wholeHouseRent +
                ", numberOfRooms=" + numberOfRooms +
                ", numberOfHousemates=" + numberOfHousemates +
                ", housePicLocation='" + housePicLocation + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
