package com.example.jmugyenyi.mychat;

/**
 * * @author Joel Mugyenyi
 * <p>
 * Andrew ID: jmugyeny
 * <p>
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
public class User {


    // fields
    //private int userID;
    private String userName;
    private String userStatus;
    private String uid;
    private String houseid;

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

    // constructors
    public User() {}

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", userStatus='").append(userStatus).append('\'');
        sb.append(", uid='").append(uid).append('\'');
        sb.append(", houseid='").append(houseid).append('\'');
        sb.append('}');
        return sb.toString();
    }

    //    public  static  User getInstance(){
//        return  instance;
//    }
    public User( String username, String userstatus) {
       // this.userID = id;
        this.userName = username;
        this.userStatus=userstatus;
    }

    public void setUserName(String username) {
        this.userName = username;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserStatus(String userstatus) {
        this.userStatus = userstatus;
    }
    public String getUserStatus() {
        return this.userStatus;
    }

}
