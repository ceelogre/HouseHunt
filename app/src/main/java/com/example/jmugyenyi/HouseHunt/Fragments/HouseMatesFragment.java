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

import com.example.jmugyenyi.HouseHunt.Activities.ViewHouseMatesActivity;
import com.example.jmugyenyi.HouseHunt.R;
import com.example.jmugyenyi.HouseHunt.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
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
public class HouseMatesFragment extends Fragment {


    protected static final String TAG = "HouseMatesFragment";

    private View housematesFragment;

    //Database refererences for the house and users in Firebase db
    private DatabaseReference databaseReference, houseMatesRef;

    private FirebaseAuth mfirebaseAuth;
    private RecyclerView myRecyclerView;

    private String currentUserID;

    public HouseMatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        housematesFragment=  inflater.inflate(R.layout.fragment_housemates, container, false);


        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        houseMatesRef  = FirebaseDatabase.getInstance().getReference().child("House");


        //Recyclerview for the housemates items from Firebase DB
        myRecyclerView = housematesFragment.findViewById(R.id.house_mates_recycler_list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return housematesFragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: "+currentUserID);

        //Retrieve results from Firebase DB for the currently logged in user
        databaseReference.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("Exist", "true");
                    if (dataSnapshot.hasChild("My House")) {
                        Log.d("got in", "true");
                        String houseID = dataSnapshot.child("My House").getValue().toString();

                        Log.d(TAG, "HouseId: " + houseID);
                        FirebaseRecyclerOptions<User> options =
                                new FirebaseRecyclerOptions.Builder<User>()
                                        .setQuery(houseMatesRef.child(houseID).child("HouseMates"), User.class)
                                        .build();


                        FirebaseRecyclerAdapter<User, FindMyMatesViewHolder> adapter = new FirebaseRecyclerAdapter<User
                                , FindMyMatesViewHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull final FindMyMatesViewHolder holder, final int position, @NonNull User model) {

                                final String houseMateIDs = getRef(position).getKey();


                                databaseReference.child(houseMateIDs).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("image")) {
                                            String _profileImage = dataSnapshot.child("image").getValue().toString();
                                            String _profileName = dataSnapshot.child("name").getValue().toString();
                                            String _profileStatus = dataSnapshot.child("status").getValue().toString();

                                            holder.userName.setText(_profileName);
                                            holder.userStatus.setText(_profileStatus);
                                            Picasso.get().load(_profileImage).placeholder(R.drawable.profile_image).into(holder.profileImage);

                                        } else {

                                            String _profileName = dataSnapshot.child("name").getValue().toString();
                                            String _profileStatus = dataSnapshot.child("status").getValue().toString();


                                            holder.userName.setText(_profileName);
                                            holder.userStatus.setText(_profileStatus);
                                        }

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String seeker_id = getRef(position).getKey();
                                                // String str = getRef(position).getRoot().child("House").child(visit_house_id).toString();

                                                Intent acceptOrDeclineIntent = new Intent(getActivity(), ViewHouseMatesActivity.class);
                                                acceptOrDeclineIntent.putExtra("House Mate ID", seeker_id);
                                                startActivity(acceptOrDeclineIntent);

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                            @NonNull
                            @Override
                            public FindMyMatesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                View view = LayoutInflater.from(viewGroup.getContext()).
                                        inflate(R.layout.user_display_layout, viewGroup, false);

                                FindMyMatesViewHolder viewHolder = new FindMyMatesViewHolder(view);
                                return viewHolder;
                            }
                        };

                        myRecyclerView.setAdapter(adapter);
                        adapter.startListening();
                    }
                }
                else {

                    Log.d("Error", "Reference");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Method to find people whom you share a room   
    public  static class FindMyMatesViewHolder extends RecyclerView.ViewHolder
    {

        TextView userName, userStatus;
        CircleImageView profileImage;

        public FindMyMatesViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.myUser_name);
            userStatus = itemView.findViewById(R.id.myUser_status);
            profileImage = itemView.findViewById(R.id.myUser_profile_image);
        }
    }

}
