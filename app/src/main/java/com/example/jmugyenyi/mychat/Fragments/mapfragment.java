package com.example.jmugyenyi.mychat.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jmugyenyi.mychat.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;


public class mapfragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {


    protected static final String TAG = "mapfragment";


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public double latitude;
    public double longitude;
    public double houseLatitude;
    public double houseLongitude ;
    GoogleMap mgoogleMap;
    MapView mapView;
    View view;

    private String houseID;
    private DatabaseReference databaseReference;

    private OnFragmentInteractionListener mListener;

    public mapfragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Log.d(TAG, "onCreate: ");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        houseID = getActivity().getIntent().getExtras().get("house_ID").toString();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        view =  inflater.inflate(R.layout.fragment_mapfragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView)view.findViewById(R.id.map);
        Log.d(TAG, "onViewCreated: ");
        
        if(mapView !=null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        latitude = -1.935114;
        longitude = 30.082111;
        mgoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       Marker place =  googleMap.addMarker(new MarkerOptions().position( new LatLng(latitude,longitude)).draggable(true));
       place.setDraggable(true);
        CameraPosition kacyiru = CameraPosition.builder().target( new LatLng(latitude,longitude)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(kacyiru));
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.d("System out", "onMarkerDragEnd..."+marker.getPosition().latitude+"..."+marker.getPosition().longitude);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.d("System out", "onMarkerDragEnd..."+marker.getPosition().latitude+"..."+marker.getPosition().longitude);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d("System out", "onMarkerDragEnd..."+marker.getPosition().latitude+"..."+marker.getPosition().longitude);
                houseLatitude = marker.getPosition().latitude;
                houseLongitude = marker.getPosition().longitude;

            }
        });
        googleMap.setOnMarkerClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        HashMap<String , Object> location = new HashMap<>();
        location.put("latitude",houseLatitude);
        location.put("longitude",houseLongitude);
        Log.d(TAG, "onMarkerClick: "+houseLongitude);
        databaseReference.child("House").child(houseID).updateChildren(location);
        getActivity().finish();
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
