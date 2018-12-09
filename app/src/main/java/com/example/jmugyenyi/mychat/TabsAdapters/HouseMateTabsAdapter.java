package com.example.jmugyenyi.mychat.TabsAdapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jmugyenyi.mychat.Fragments.ChatFragment;
import com.example.jmugyenyi.mychat.Fragments.HouseFacilitiesFragment;
import com.example.jmugyenyi.mychat.Fragments.HouseMatesFragment;
import com.example.jmugyenyi.mychat.model.ParentUser;

/**
 * * @author Joel Mugyenyi
 * <p>
 * Andrew ID: jmugyeny
 * <p>
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
public class HouseMateTabsAdapter extends ParentUser {


    public HouseMateTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i)
        {
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 1:
                HouseFacilitiesFragment houseFragment = new HouseFacilitiesFragment();
                return houseFragment;
            case 2:
                HouseMatesFragment houseMatesFragment = new HouseMatesFragment();
                return houseMatesFragment;

                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:

                return "Chat";
            case 1:

                return "House Facilities";
            case 2:

                return "House Mates";
            default:
                return null;
        }
    }
}
