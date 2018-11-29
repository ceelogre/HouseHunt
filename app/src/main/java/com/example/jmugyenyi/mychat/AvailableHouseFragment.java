package com.example.jmugyenyi.mychat;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */

public class AvailableHouseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<String> itemList = new ArrayList<>();
    ArrayList<String>prices = new ArrayList<>();
    ArrayList<String>sizes = new ArrayList<>();
    ArrayList<String>countries = new ArrayList<>();
    ArrayList<String>cities = new ArrayList<>();
    String chosenSize = "none";
    String chosenPrice = "0";
    String chosenCountry = "None";
    String chosenCity = "None";
    RecyclerView mRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MyAdapter adapter;


    private View availableHouseFragmentView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfGroups = new ArrayList<>();
    private DatabaseReference databaseReference;
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView recyclerView;
    private RecyclerView myRecyclerView;
    private List<Contacts> listContact;



    public AvailableHouseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment seekerHomePage.
     */
    // TODO: Rename and change types and number of parameters
    public static AvailableHouseFragment newInstance(String param1, String param2) {
        AvailableHouseFragment fragment = new AvailableHouseFragment();
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

        Log.d("Fragment","Got here");
        //the recylcer view
        View view = inflater.inflate(R.layout.fragment_available_house, container, false);
        Spinner size = (Spinner)view.findViewById(R.id.size);
        Spinner price = (Spinner)view.findViewById(R.id.price);
        Spinner country = (Spinner)view.findViewById(R.id.country);
        Spinner city = (Spinner)view.findViewById(R.id.city);

        getChoiceCriteria();

        ArrayAdapter<String> adapterSize = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, sizes);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size.setAdapter(adapterSize);
        chosenSize = size.getSelectedItem().toString();


        ArrayAdapter<String> adapterPrice = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, prices);
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        price.setAdapter(adapterPrice);
        chosenPrice = price.getSelectedItem().toString();

        ArrayAdapter<String> adapterCountry = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, sizes);
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adapterCountry);
        chosenCountry = country.getSelectedItem().toString();

        ArrayAdapter<String> adapterCity = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, sizes);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapterCity);
        chosenCity = country.getSelectedItem().toString();

        return  view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.rv);
        adapter = new MyAdapter(itemList);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        generateList();
    }

    private void generateList() {
        for (int i=1;i<20;i++){
            itemList.add("Item: "+i);
        }

        if (adapter!=null)
            adapter.notifyDataSetChanged();

    }

    public void getChoiceCriteria()
    {
        // populating the size array
        sizes.add("large");
        sizes.add("small");
        sizes.add("villa");

        //polutating the price array
        prices.add("10K-100K  (rwf)");
        prices.add("100K-200K  (rwf)");
        prices.add("200K-300K  (rwf)");






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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                        .setQuery(databaseReference,Contacts.class).build();

        FirebaseRecyclerAdapter<Contacts,FindMatesViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, FindMatesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindMatesViewHolder holder, int position, @NonNull Contacts model) {
                        holder.username.setText(model.getName());
                        holder.status.setText(model.getStatus());
                        //   Picasso.get().load(model.getImage()).into(holder.profileImage);
                    }

                    @NonNull
                    @Override
                    public FindMatesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.housemates_display_layout,viewGroup,false);

                        FindMatesViewHolder viewHolder = new FindMatesViewHolder(view);
                        return viewHolder;
                    }
                };
        myRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class FindMatesViewHolder extends RecyclerView.ViewHolder{
        TextView username , status;
        CircleImageView profileImage;
        public FindMatesViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_profile_name);
            status = itemView.findViewById(R.id.user_profile_status);
            profileImage = itemView.findViewById(R.id.housemates_profile_image);
        }

    }


}




