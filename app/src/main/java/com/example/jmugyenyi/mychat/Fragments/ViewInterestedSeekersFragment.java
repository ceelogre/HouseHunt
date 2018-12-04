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
public class ViewInterestedSeekersFragment extends Fragment {


    private View viewInterestedSeekersFragment;

    public ViewInterestedSeekersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewInterestedSeekersFragment= inflater.inflate(R.layout.fragment_view_interested_seekers, container, false);
        return viewInterestedSeekersFragment;
    }

}
