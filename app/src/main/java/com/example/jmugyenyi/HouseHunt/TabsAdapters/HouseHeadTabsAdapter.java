package com.example.jmugyenyi.HouseHunt.TabsAdapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.jmugyenyi.HouseHunt.Fragments.ChatFragment;
import com.example.jmugyenyi.HouseHunt.Fragments.HouseMatesFragment;
import com.example.jmugyenyi.HouseHunt.model.ParentUser;
import com.example.jmugyenyi.HouseHunt.utils.ContextInterface;
import com.example.jmugyenyi.HouseHunt.utils.HouseCRUD;
import com.example.jmugyenyi.HouseHunt.Fragments.PostAHouseFragment;
import com.example.jmugyenyi.HouseHunt.Fragments.ViewInterestedSeekersFragment;
import com.google.firebase.auth.FirebaseAuth;

/**
 * * @author Joel Mugyenyi
 *
 * Andrew ID: jmugyeny
 *
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
public class HouseHeadTabsAdapter extends ParentUser implements ContextInterface {



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
            case 2:
                ViewInterestedSeekersFragment interestedSeeker = new ViewInterestedSeekersFragment();
                return interestedSeeker;

            case 3:
                HouseMatesFragment houseMatesFragment = new HouseMatesFragment();
                return houseMatesFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
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
            case 2:

                return "Seekers";
            case 3:

                return "House Mates";
            default:
                return null;
        }
    }

    @Override
    public void getAssociatedHouses(FirebaseAuth authenticatedUser) {
        HouseCRUD houseCRUD = new HouseCRUD(authenticatedUser);
        houseCRUD.getAvailableHouses();
    }
}
