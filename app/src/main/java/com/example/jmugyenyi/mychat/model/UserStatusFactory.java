package com.example.jmugyenyi.mychat.model;

import com.example.jmugyenyi.mychat.TabsAdapters.HouseHeadTabsAdapter;
import com.example.jmugyenyi.mychat.TabsAdapters.HouseMateTabsAdapter;
import com.example.jmugyenyi.mychat.TabsAdapters.SeekerTabsAdapter;
import com.example.jmugyenyi.mychat.TabsAdapters.driverTabsAdapter;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * * @author Joel Mugyenyi
 *
 * Andrew ID: jmugyeny
 *
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */

// Factory class to create user Tab Adapters to view user specific interfaces
public class UserStatusFactory extends AppCompatActivity{


    // Constructor
    public UserStatusFactory()
    {

    }

    public  ParentUser createUser(String type,FragmentManager fm)
    {
        if (type.equalsIgnoreCase(ParentUser.seeker)) {
            return new SeekerTabsAdapter(fm);
        }
        else if(type.equalsIgnoreCase(ParentUser.househead)) {
                return new HouseHeadTabsAdapter(fm);
        }else if(type.equalsIgnoreCase(ParentUser.housemate)) {
            return new HouseMateTabsAdapter(fm);
        }
        else if(type.equalsIgnoreCase(ParentUser.driver)) {
            return new driverTabsAdapter(fm);
        }
        else
            return null;
    }
}
