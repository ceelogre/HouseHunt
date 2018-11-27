package com.example.jmugyenyi.mychat;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class DatabaseHelperClass extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Student.db";
    public static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;
    String user_email;
    String user_password;
    String user_status;

    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + UsersCredentialsContract.FeedCredentialsEntry.TABLE_NAME + " (" +
                    UsersCredentialsContract.FeedCredentialsEntry._ID + " INTEGER PRIMARY KEY," +
                    UsersCredentialsContract.FeedCredentialsEntry.user_email + " TEXT," +
                    UsersCredentialsContract.FeedCredentialsEntry.user_password + " TEXT," +
                    UsersCredentialsContract.FeedCredentialsEntry.user_status + " TEXT)";


    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + UsersCredentialsContract.FeedCredentialsEntry.TABLE_NAME;



    public DatabaseHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_USERS_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USERS);
        onCreate(db);
    }




    public void insertUsers(String user_email,String user_password,String user_status)
    {
        // Gets the data repository in write mode
        db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(UsersCredentialsContract.FeedCredentialsEntry.user_email,user_email );
        values.put(UsersCredentialsContract.FeedCredentialsEntry.user_password,user_password);
        values.put(UsersCredentialsContract.FeedCredentialsEntry.user_status,user_status);
        long newRowId = db.insert(UsersCredentialsContract.FeedCredentialsEntry.TABLE_NAME, null, values);


    }

    public String getUserStatus(String user_email,String user_password)
    {
        String status  = "no_status";
        Cursor cursor = db.rawQuery("SELECT *  FROM users",null);
        if (!cursor.getString(3).equals(null))
        {
            status = cursor.getString(3);
        }
        else
            {
                status  = "no_status";
            }

        return status;
    }



}

