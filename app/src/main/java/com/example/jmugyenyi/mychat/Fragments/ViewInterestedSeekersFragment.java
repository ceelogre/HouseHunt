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
import android.widget.TextView;

import com.example.jmugyenyi.mychat.Activities.AcceptOrDeclineSeekerActivity;
import com.example.jmugyenyi.mychat.Activities.PaymentActivity;
import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.model.Interest;
import com.example.jmugyenyi.mychat.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewInterestedSeekersFragment extends Fragment {


    protected static final String TAG = "ViewInterestedSeekers";

    private View viewInterestedSeekersFragment;
    private RecyclerView myRecyclerView;

    private DatabaseReference userRef, seekersRef;
    private FirebaseAuth mfirebaseAuth;

    private String currentUserID;

    public ViewInterestedSeekersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewInterestedSeekersFragment= inflater.inflate(R.layout.fragment_view_interested_seekers, container, false);
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("seekers");
        seekersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        myRecyclerView = viewInterestedSeekersFragment.findViewById(R.id.view_interested_recycler_list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return viewInterestedSeekersFragment;
    }


    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(userRef,User.class)
                .build();


        FirebaseRecyclerAdapter<User,FindUsersViewHolder> adapter = new FirebaseRecyclerAdapter<User, FindUsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FindUsersViewHolder holder, final int position, @NonNull User model)
            {

                String seekersIDs = getRef(position).getKey();

                Log.d(TAG, "seekersIDs: "+seekersIDs);


                seekersRef.child(seekersIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.hasChild("image"))
                        {
                            String _profileImage = dataSnapshot.child("image").getValue().toString();
                            String _profileName = dataSnapshot.child("name").getValue().toString();
                            String _profileStatus = dataSnapshot.child("status").getValue().toString();

                            holder.userName.setText(_profileName);
                            holder.userStatus.setText("wants to join your house");
                            Picasso.get().load(_profileImage).placeholder(R.drawable.profile_image).into(holder.profileImage);

                        }
                        else
                        {
                            String _profileName = dataSnapshot.child("name").getValue().toString();
                            String _profileStatus = dataSnapshot.child("status").getValue().toString();

                            holder.userName.setText(_profileName);
                            holder.userStatus.setText("wants to join your house");
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String seeker_id = getRef(position).getKey();
                               // String str = getRef(position).getRoot().child("House").child(visit_house_id).toString();

                                Intent acceptOrDeclineIntent = new Intent(getActivity(), AcceptOrDeclineSeekerActivity.class);
                                acceptOrDeclineIntent.putExtra("Seeker's ID", seeker_id);
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
            public FindUsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View  view = LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.user_display_layout,viewGroup,false);

                FindUsersViewHolder viewHolder = new FindUsersViewHolder(view);
                return  viewHolder;
            }
        };

        myRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public  static class FindUsersViewHolder extends RecyclerView.ViewHolder
    {

        TextView userName, userStatus;
        CircleImageView profileImage;

        public FindUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.myUser_name);
            userStatus = itemView.findViewById(R.id.myUser_status);
            profileImage = itemView.findViewById(R.id.myUser_profile_image);
        }
    }

}
