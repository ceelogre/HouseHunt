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


//    public static final String TABLE_NAME = "USER";
//    public static final String COLUMN_ID = "UserID";
//    public static final String COLUMN_NAME = "UserName";
//    public static final String COLUMN_STATUS = "UserStatus";
//
//    public static final String CREATE_TABLE =
//            "CREATE TABLE " + TABLE_NAME + "("
//                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + COLUMN_NAME + " TEXT, "
//                    + COLUMN_STATUS+ " TEXT "
//                    + ")";


   // private static final User  instance = new User();

    // constructors
    public User() {}



//    public  static  User getInstance(){
//        return  instance;
//    }
    public User( String username, String userstatus) {
       // this.userID = id;
        this.userName = username;
        this.userStatus=userstatus;
    }
    // properties
//    public void setID(int id) {
//        this.userID = id;
//    }
//    public int getID() {
//        return this.userID;
//    }
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
