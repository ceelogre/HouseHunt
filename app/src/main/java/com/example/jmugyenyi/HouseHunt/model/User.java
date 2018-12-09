package com.example.jmugyenyi.HouseHunt.model;

public  class User {


    // fields
    private String name;
    private String status;
    private String uid;
    private String houseid;
    private String image;
    private String bio;




    // constructors
    public User() {}

    public User( String username, String userstatus) {
        // this.userID = id;
        this.name = username;
        this.status =userstatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public void setName(String username) {
        this.name = username;
    }
    public String getName() {
        return this.name;
    }
    public void setStatus(String userstatus) {
        this.status = userstatus;
    }
    public String getStatus() {
        return this.status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", uid='").append(uid).append('\'');
        sb.append(", houseid='").append(houseid).append('\'');
        sb.append('}');
        return sb.toString();
    }


}
