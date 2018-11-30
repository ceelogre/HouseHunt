package com.example.jmugyenyi.mychat.TabsAdapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jmugyenyi.mychat.Fragments.ChatFragment;
import com.example.jmugyenyi.mychat.Fragments.PostAHouseFragment;

/**
 * * @author Joel Mugyenyi
 *
 * Andrew ID: jmugyeny
 *
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
public class HouseHeadTabsAdapter extends FragmentPagerAdapter {



    public HouseHeadTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                ChatFragment chat = new ChatFragment();
                return chat;
            case 1:
                PostAHouseFragment postedHouse = new PostAHouseFragment();
                return postedHouse;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:

                return "Chat";
            case 1:

                return "Post a house";
            default:
                return null;
        }
    }
}
