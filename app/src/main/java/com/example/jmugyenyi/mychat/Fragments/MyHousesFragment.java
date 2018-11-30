package com.example.jmugyenyi.mychat.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jmugyenyi.mychat.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyHousesFragment extends Fragment {

    private View myHouses;

    public MyHousesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myHouses = inflater.inflate(R.layout.fragment_my_houses, container, false);

        // Inflate the layout for this fragment
        return myHouses; //inflater.inflate(R.layout.fragment_my_houses, container, false);
    }

}
