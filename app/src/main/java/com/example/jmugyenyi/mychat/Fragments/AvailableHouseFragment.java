package com.example.jmugyenyi.mychat.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.Activities.ViewHouseActivity;
import com.example.jmugyenyi.mychat.model.House;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */

public class AvailableHouseFragment extends Fragment {

    private static final String TAG= "AvailableHouseFragment";


    private View availableHouseFragmentView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfGroups = new ArrayList<>();
    private DatabaseReference databaseReference;
    private android.support.v7.widget.Toolbar mToolbar;
    private RecyclerView recyclerView;
    private RecyclerView myRecyclerView;
   // private List<Contacts> listContact;


    public AvailableHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        availableHouseFragmentView=  inflater.inflate(R.layout.fragment_available_house, container, false);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("House");

        myRecyclerView = availableHouseFragmentView.findViewById(R.id.available_houses_recycler_list);
        //RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(),listContact);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        myRecyclerView.setAdapter(recyclerViewAdapter);

        return  availableHouseFragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<House> options =
                new FirebaseRecyclerOptions.Builder<House>()
                        .setQuery(databaseReference,House.class).build();

        FirebaseRecyclerAdapter<House,FindAvailableHousesViewHolder> adapter =
                new FirebaseRecyclerAdapter<House, FindAvailableHousesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindAvailableHousesViewHolder holder, final int position, @NonNull House model) {

                        holder.username.setText(model.getHouseName());
                        holder.status.setText(model.getStreet());
//                        holder.username.setText(model.getName());
//                        holder.status.setText(model.getStatus());
                     //   Picasso.get().load(com.example.jmugyenyi.mychat.model.getImage()).into(holder.profileImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visit_house_id= getRef(position).getKey();
                                String str= getRef(position).getRoot().child("House").child(visit_house_id).toString();

                                Log.d(TAG, "onClick: "+str);

                                Intent viewHouseIntent = new Intent(getActivity(),ViewHouseActivity.class);
                                viewHouseIntent.putExtra("visit_house_id",visit_house_id);
                                startActivity(viewHouseIntent);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FindAvailableHousesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.house_display_layout,viewGroup,false);

                        FindAvailableHousesViewHolder viewHolder = new FindAvailableHousesViewHolder(view);
                        return viewHolder;
                    }
                };
        myRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class FindAvailableHousesViewHolder extends RecyclerView.ViewHolder{
        TextView username , status;
        CircleImageView profileImage;
        public FindAvailableHousesViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_profile_name);
            status = itemView.findViewById(R.id.user_profile_status);
            profileImage = itemView.findViewById(R.id.housemates_profile_image);
        }
    }
}
