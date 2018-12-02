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
import android.widget.Toast;

import com.example.jmugyenyi.mychat.Activities.MainActivity;
import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.Activities.ViewHouseActivity;
import com.example.jmugyenyi.mychat.model.House;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */

public class AvailableHouseFragment extends Fragment {

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

        FirebaseRecyclerAdapter<House,FindMatesViewHolder> adapter =
                new FirebaseRecyclerAdapter<House, FindMatesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindMatesViewHolder holder, final int position, @NonNull House model) {

                        holder.username.setText(model.getHouseId());
                        holder.status.setText(model.getStreet());
//                        holder.username.setText(model.getName());
//                        holder.status.setText(model.getStatus());
                     //   Picasso.get().load(com.example.jmugyenyi.mychat.model.getImage()).into(holder.profileImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

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

                    @NonNull
                    @Override
                    public FindMatesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.house_display_layout,viewGroup,false);

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
