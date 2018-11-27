package com.example.jmugyenyi.mychat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * * @author Joel Mugyenyi
 * <p>
 * Andrew ID: jmugyeny
 * <p>
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
public class MyDBHandler  extends SQLiteOpenHelper {

    //information of database
    private static final String TAG = "MyDBHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gradesDB.db";


    //initialize the database

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(User.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void insert(User user)
    {


    }

}
