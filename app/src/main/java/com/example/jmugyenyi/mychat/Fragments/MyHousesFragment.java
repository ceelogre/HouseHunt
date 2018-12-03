package com.example.jmugyenyi.mychat.Fragments;


import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.example.jmugyenyi.mychat.Activities.PaymentActivity;
import com.example.jmugyenyi.mychat.Activities.SettingsActivity;
import com.example.jmugyenyi.mychat.Activities.ViewHouseActivity;
import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.model.House;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyHousesFragment extends Fragment {

    private static final String TAG= "MyHousesFragment";

    private View myHouses;
    private DatabaseReference databaseReferenceInterest;
    private DatabaseReference databaseReferenceUsers;
    private DatabaseReference databaseReference;

    private FirebaseAuth mfirebaseAuth;
    private RecyclerView myRecyclerView;

    private String currentUserID, retrieveHouseID, retrieveInterestStatus;

    public MyHousesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myHouses = inflater.inflate(R.layout.fragment_my_houses, container, false);
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();
        databaseReferenceInterest = FirebaseDatabase.getInstance().getReference().child("interest");
        databaseReference = FirebaseDatabase.getInstance().getReference();


        myRecyclerView = myHouses.findViewById(R.id.my_houses_recycler_list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d(TAG, "onCreateView: Testing " +currentUserID);

        //RetrieveUserID();

        // Inflate the layout for this fragment
        return myHouses; //inflater.inflate(R.layout.fragment_my_houses, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();




        RetrieveUserID();


//        FirebaseRecyclerOptions<House> options =
//                new FirebaseRecyclerOptions.Builder<House>()
//                        .setQuery(databaseReference,House.class).build();
//
//        FirebaseRecyclerAdapter<House,FindMyHousesViewHolder> adapter =
//                new FirebaseRecyclerAdapter<House, FindMyHousesViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull FindMyHousesViewHolder holder, final int position, @NonNull House model) {
//
//                        holder.housename.setText(model.getHouseName());
//                        holder.street.setText(model.getStreet());
//                        Picasso.get().load(model.getImage()).placeholder(R.drawable.house4).into(holder.houseImage);
//
//                        Log.d(TAG, "onClick: "+model.getImage());
//
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String visit_house_id= getRef(position).getKey();
//                                String str= getRef(position).getRoot().child("House").child(visit_house_id).toString();
//
//                                Intent houseStatusIntent = new Intent(getActivity(),ViewHouseActivity.class);
//                                houseStatusIntent.putExtra("visit_house_id",visit_house_id);
//                                //startActivity(viewHouseIntent);
//
//                            }
//                        });
//                    }
//
//                    @NonNull
//                    @Override
//                    public FindMyHousesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//                        View view = LayoutInflater.from(viewGroup.getContext())
//                                .inflate(R.layout.house_display_layout,viewGroup,false);
//
//                        FindMyHousesViewHolder viewHolder = new FindMyHousesViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//        myRecyclerView.setAdapter(adapter);
//        adapter.startListening();
    }




    private void RetrieveUserID() {

        databaseReference.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("interest")))
                {

                    String retrieveInterestID = dataSnapshot.child("interest").getValue()
                            .toString().replace("=true","")
                            .replaceAll("\\{","")
                            .replaceAll("\\}","");

                    Log.d(TAG, "onDataChange 1: "+retrieveInterestID);







                    //databaseReference.child("Interest").child(retrieveInterestID).addValueEventListener(new ValueEventListener()
                    databaseReference.child("Interest").addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //Log.d(TAG, "onDataChange: We made it!");
                           // if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("houseID")))
                            {

//                                 retrieveHouseID = dataSnapshot.child("houseID").getValue()
//                                        .toString().replace("=true","")
//                                        .replaceAll("\\{","")
//                                        .replaceAll("\\}","");
//
//                                Log.d(TAG, "onDataChange 2: "+retrieveHouseID);


                                Log.d(TAG, "onDataChange: "+dataSnapshot.getChildrenCount());



//                                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
//
//                                List<Object> values = td.values();

                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                                    Log.d(TAG, "onDataChange: "+postSnapshot.getKey());
                                    String interestID = postSnapshot.getKey();
                                    Log.d(TAG, "key: "+interestID);
                                    retrieveHouseID = dataSnapshot.child(interestID).child("houseID").getValue()
                                            .toString().replace("=true","")
                                            .replaceAll("\\{","")
                                            .replaceAll("\\}","");
                                    retrieveInterestStatus = dataSnapshot.child(interestID).child("status").getValue()
                                            .toString().replace("=true","")
                                            .replaceAll("\\{","")
                                            .replaceAll("\\}","");


                                }


                                Log.d(TAG, "retrieveHouseID "+retrieveHouseID);

                                {

                                    FirebaseRecyclerOptions<House> options =
                                            new FirebaseRecyclerOptions.Builder<House>()
                                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("House"),House.class).build();

                                    FirebaseRecyclerAdapter<House,FindMyHousesViewHolder> adapter =
                                            new FirebaseRecyclerAdapter<House, FindMyHousesViewHolder>(options) {
                                                @Override
                                                protected void onBindViewHolder(@NonNull FindMyHousesViewHolder holder, final int position, @NonNull House model) {


                                                    Log.d(TAG, "onBindViewHolder: We have got up to here! "+model.getHouseId());
                                                    if (model.getHouseId().equalsIgnoreCase(retrieveHouseID))
                                                    {

                                                        holder.housename.setText(model.getHouseName());
                                                        holder.street.setText(model.getStreet());
                                                        holder.status.setText(retrieveInterestStatus);
                                                        holder.status.setTextColor(Color.RED);
                                                        Picasso.get().load(model.getImage()).placeholder(R.drawable.house4).into(holder.houseImage);

                                                        Log.d(TAG, "onClick: " + model.getImage());

                                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                String visit_house_id = getRef(position).getKey();
                                                                String str = getRef(position).getRoot().child("House").child(visit_house_id).toString();

                                                                Intent houseStatusIntent = new Intent(getActivity(), PaymentActivity.class);
                                                                //houseStatusIntent.putExtra("visit_house_id", visit_house_id);
                                                                startActivity(houseStatusIntent);

                                                            }
                                                        });
                                                    }
                                                }

                                                @NonNull
                                                @Override
                                                public FindMyHousesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                                    View view = LayoutInflater.from(viewGroup.getContext())
                                                            .inflate(R.layout.house_interest_layout,viewGroup,false);

                                                    FindMyHousesViewHolder viewHolder = new FindMyHousesViewHolder(view);
                                                    return viewHolder;
                                                }
                                            };
                                    myRecyclerView.setAdapter(adapter);
                                    adapter.startListening();
                                }











                            }
//                            else
//                            {
//                                //Toast.makeText(SettingsActivity.this,"Update Profile",Toast.LENGTH_SHORT).show();
//                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });







                }else
                {
                    //Toast.makeText(SettingsActivity.this,"Update Profile",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    public static class FindMyHousesViewHolder extends RecyclerView.ViewHolder{
        TextView housename , street,status;
        CircleImageView houseImage;
        public FindMyHousesViewHolder(@NonNull View itemView) {
            super(itemView);

            housename = itemView.findViewById(R.id.house_interest_name);
            street = itemView.findViewById(R.id.house_interest_street);
            houseImage = itemView.findViewById(R.id.house_interest_image);
            status = itemView.findViewById(R.id.house_interest_status);
        }
    }
}
