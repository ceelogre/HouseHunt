package com.example.jmugyenyi.mychat.TabsAdapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jmugyenyi.mychat.Fragments.AvailableHouseFragment;
import com.example.jmugyenyi.mychat.Fragments.MyHousesFragment;
import com.example.jmugyenyi.mychat.model.ParentUser;
import com.example.jmugyenyi.mychat.utils.ContextInterface;
import com.example.jmugyenyi.mychat.utils.HouseCRUD;
import com.google.firebase.auth.FirebaseAuth;

/**
 * * @author Joel Mugyenyi
 * <p>
 * Andrew ID: jmugyeny
 * <p>
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor
 * received unauthorized assistance on this work.!
 */
public class SeekerTabsAdapter extends ParentUser implements ContextInterface {


    public SeekerTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                MyHousesFragment myFragment = new MyHousesFragment();
                return myFragment;
            case 1:
                AvailableHouseFragment availHouseFragment = new AvailableHouseFragment();
                return availHouseFragment;

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

                return "My Houses";
            case 1:

                return "Available Houses";
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
