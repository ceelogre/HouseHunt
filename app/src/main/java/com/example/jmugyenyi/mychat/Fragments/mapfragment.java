package com.example.jmugyenyi.mychat.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jmugyenyi.mychat.Activities.LocationActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mapfragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mapfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mapfragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public double latitude;
    public double longitude;
    public double houseLatitude;
    public double houseLongitude ;
    GoogleMap mgoogleMap;
    MapView mapView;
    View view;

    private OnFragmentInteractionListener mListener;

    public mapfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mapfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mapfragment newInstance(String param1, String param2) {
        mapfragment fragment = new mapfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_mapfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView)view.findViewById(R.id.map);
//        mapView.getMapAsync(this);

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
//        Bundle address = getActivity().getIntent().getExtras();
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


       googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
           @Override
           public boolean onMarkerClick(Marker marker) {

               getActivity().finish();
               return false;
           }
       });

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Intent one = new Intent(getContex(), Test.class);
       // one.putExtra("databaseUrl", "hello");
       // startActivity(one);
        getActivity().finish();
        return false;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
