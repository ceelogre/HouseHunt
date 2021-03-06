package com.example.jmugyenyi.HouseHunt.Fragments;


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
import android.widget.TextView;
import com.example.jmugyenyi.HouseHunt.R;
import com.example.jmugyenyi.HouseHunt.Activities.ViewHouseActivity;
import com.example.jmugyenyi.HouseHunt.model.House;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */

public class AvailableHouseFragment extends Fragment {
    // this fragments is going to help the user see all the available houses

    private static final String TAG= "AvailableHouseFragment";

    private View availableHouseFragmentView;
    private DatabaseReference databaseReference;
    private RecyclerView myRecyclerView;

    private String houseOwnerID,visit_house_id;


    public AvailableHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        availableHouseFragmentView=  inflater.inflate(R.layout.fragment_available_house, container, false);

        //Reference for the House node in Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("House");

        //Recyclerviewer holder for the house items to be retrieved from Firebase db
        myRecyclerView = availableHouseFragmentView.findViewById(R.id.available_houses_recycler_list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

                        {

                        holder.housename.setText(model.getHouseName());
                        holder.street.setText(model.getStreet());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.house4).into(holder.houseImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                 visit_house_id= getRef(position).getKey();
                                String str= getRef(position).getDatabase().getReference().child("House").child(visit_house_id).child("city").getKey();

                                databaseReference.child(visit_house_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                         houseOwnerID = dataSnapshot.child("authenticatedUserId").getValue().toString();
                                        Log.d(TAG, "HouseID: "+houseOwnerID);


                                        Intent viewHouseIntent = new Intent(getActivity(),ViewHouseActivity.class);
                                        viewHouseIntent.putExtra("visit_house_id",visit_house_id);
                                        viewHouseIntent.putExtra("ownerID",houseOwnerID);
                                        startActivity(viewHouseIntent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                final String houseRecordId= getRef(position).getKey();

                                databaseReference.child(houseRecordId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("houseId")){
                                             String houseId = dataSnapshot.child("houseId").getValue().toString();
                                            Intent viewHouseIntent = new Intent(getActivity(),ViewHouseActivity.class);
                                            viewHouseIntent.putExtra("visit_house_id",houseRecordId);
                                            startActivity(viewHouseIntent);

                                        }else{

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                    }
                                });

                            }
                        });
                        }
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
        TextView housename , street;
        CircleImageView houseImage;
        public FindAvailableHousesViewHolder(@NonNull View itemView) {
            super(itemView);

            housename = itemView.findViewById(R.id.user_profile_name);
            street = itemView.findViewById(R.id.user_profile_status);
            houseImage = itemView.findViewById(R.id.housemates_profile_image);
        }
    }
}
