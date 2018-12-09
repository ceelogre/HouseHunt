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



import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyHousesFragment extends Fragment {

    private static final String TAG= "MyHousesFragment";

    private View myHouses;

    private DatabaseReference databaseReference, houseRef;

    private FirebaseAuth mfirebaseAuth;
    private RecyclerView myRecyclerView;

    private String currentUserID;

    public MyHousesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myHouses = inflater.inflate(R.layout.fragment_my_houses, container, false);
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        houseRef  = FirebaseDatabase.getInstance().getReference().child("House");


        myRecyclerView = myHouses.findViewById(R.id.my_houses_recycler_list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        // Inflate the layout for this fragment
        return myHouses;
    }

    @Override
    public void onStart() {
        super.onStart();

        RetrieveUserID();

    }

    private void RetrieveUserID() {

        databaseReference.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("houses")))
{

                    FirebaseRecyclerOptions<House> options =
                            new FirebaseRecyclerOptions.Builder<House>()
                                    .setQuery(FirebaseDatabase.getInstance()
                                            .getReference().child("Users")
                                            .child(currentUserID).child("houses")
                                            ,House.class).build();

                    FirebaseRecyclerAdapter<House,FindMyHousesViewHolder> adapter =
                            new FirebaseRecyclerAdapter<House, FindMyHousesViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull final FindMyHousesViewHolder holder,
                                                                final int position, @NonNull House model) {
                                    DatabaseReference myHouseStatusRef = getRef(position);

                                    myHouseStatusRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                           final String  myHouseStatus = dataSnapshot.child("Request").getValue().toString();


                                            String houseIDs = getRef(position).getKey();
                                            houseRef.child(houseIDs).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                {
                                                    if(dataSnapshot.hasChild("image"))
                                                    {
                                                        String _houseImage = dataSnapshot.child("image").getValue().toString();
                                                        String _houseName = dataSnapshot.child("houseName").getValue().toString();
                                                        String _houseStreet = dataSnapshot.child("street").getValue().toString();

                                                        holder.housename.setText(_houseName);
                                                        holder.street.setText(_houseStreet);
                                                        holder.status.setText(myHouseStatus);
                                                        if(myHouseStatus.equalsIgnoreCase("Accepted")){
                                                            holder.status.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                        }else if(myHouseStatus.equalsIgnoreCase("Rejected"))
                                                        {
                                                            holder.status.setTextColor(Color.RED);
                                                        }else
                                                        {
                                                            holder.status.setTextColor(getResources().getColor(R.color.colorOrange));
                                                        }

                                                        Picasso.get().load(_houseImage).placeholder(R.drawable.house4).into(holder.houseImage);

                                                    }
                                                    else
                                                    {
                                                        String _houseName = dataSnapshot.child("houseName").getValue().toString();
                                                        String _houseStreet = dataSnapshot.child("street").getValue().toString();

                                                        holder.housename.setText(_houseName);
                                                        holder.street.setText(_houseStreet);
                                                        holder.status.setText(myHouseStatus);
                                                        if(myHouseStatus.equalsIgnoreCase("Accepted")){
                                                            holder.status.setTextColor(getResources().getColor(R.color.colorPrimary));
                                                        }else if(myHouseStatus.equalsIgnoreCase("Rejected"))
                                                        {
                                                            holder.status.setTextColor(Color.RED);
                                                        }else
                                                        {
                                                            holder.status.setTextColor(getResources().getColor(R.color.colorOrange));
                                                        }
                                                    }

                                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            String visit_house_id = getRef(position).getKey();
                                                            String str = getRef(position).getRoot().child("House").child(visit_house_id).toString();

                                                            Intent houseStatusIntent = new Intent(getActivity(), PaymentActivity.class);
                                                            houseStatusIntent.putExtra("house_id", visit_house_id);
                                                            startActivity(houseStatusIntent);

                                                        }
                                                    });

                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

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
                                public FindMyHousesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                    View view = LayoutInflater.from(viewGroup.getContext())
                                            .inflate(R.layout.house_interest_layout,viewGroup,false);

                                    FindMyHousesViewHolder viewHolder = new FindMyHousesViewHolder(view);
                                    return viewHolder;
                                }
                            };
                    myRecyclerView.setAdapter(adapter);
                    adapter.startListening();


                }else{
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
