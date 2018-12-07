package com.example.jmugyenyi.mychat.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link map_houses.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link map_houses#newInstance} factory method to
 * create an instance of this fragment.
 */
public class map_houses extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public double houseLatitude;
    public double houseLongitude ;
    GoogleMap mgoogleMap;
    MapView mapView;
    View view;

    private OnFragmentInteractionListener mListener;

    public map_houses() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment map_houses.
     */
    // TODO: Rename and change types and number of parameters
    public static map_houses newInstance(String param1, String param2) {
        map_houses fragment = new map_houses();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_map_houses, container, false);
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
            mapView.getMapAsync((OnMapReadyCallback) this);
        }
    }

    public HashMap<String , Object> getAllHouses ()
    {
        HashMap<String, Object> houses = new HashMap<>();
        ArrayList<Double> location = new ArrayList<>();
        location.add(-1.935114);
        location.add(30.082111);
        houses.put ("house1",location);
        //location.clear();
        location = new ArrayList<>();

        location.add(-1.5035);
        location.add(29.6333);
        houses.put ("house2",location);


        return houses;
    }


    public void onMapReady(GoogleMap googleMap ) {
        MapsInitializer.initialize(getContext());
//        Bundle address = getActivity().getIntent().getExtras();
        HashMap<String,Object> houses = new HashMap<>();
        ArrayList<Double> location = new ArrayList<Double>();
        houses = getAllHouses ();
       double latitude = -1.935114;
      double  longitude = 30.082111;
        mgoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Iterator it = houses.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            location = (ArrayList<Double> )pair.getValue();
            String house_name = (String)pair.getKey();
            Marker place =  googleMap.addMarker(new MarkerOptions().position( new LatLng(location.get(0),location.get(1))).draggable(false));
           CameraPosition kacyiru = CameraPosition.builder().target( new LatLng(location.get(0),location.get(1))).zoom(8).bearing(0).tilt(45).build();
           googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(kacyiru));
        }



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   // @Override
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
