package com.example.jmugyenyi.mychat.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * * @author Joel Mugyenyi
 * <p>
 * Andrew ID: jmugyeny
 * <p>
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
// Parent class for all user types
public class ParentUser extends FragmentPagerAdapter {

    public static final String seeker ="seeker";
    public static final String househead ="house head";
    public static final String housemate ="house mate";
    public static final String driver ="driver";

    public ParentUser(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
