

package com.example.jmugyenyi.mychat.TabsAdapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


import com.example.jmugyenyi.mychat.Fragments.map_houses;
import com.example.jmugyenyi.mychat.Fragments.mapfragment;
import com.example.jmugyenyi.mychat.model.ParentUser;

public class driverTabsAdapter extends ParentUser {

    public driverTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                map_houses mapHouses = new map_houses();
                return mapHouses;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:

                return "All houses";
//            case 1:
//
//                return "Post a house";
            default:
                return null;
        }
    }
}
