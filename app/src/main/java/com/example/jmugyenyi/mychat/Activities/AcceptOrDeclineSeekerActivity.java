package com.example.jmugyenyi.mychat.Activities;

import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcceptOrDeclineSeekerActivity extends AppCompatActivity {


    // Instance Variables
    protected static final String TAG = "AcceptOrDecline";
    private TextView seekerName, seekerStatus;
    private CircleImageView seekerImage;
    private Button acceptButton, declineButton;

    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference databaseReference;

    private String seekerID, currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_or_decline_seeker);


        // Instances of firebase authentication and database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();

        initializeFields();
        Toolbar toolbar =  findViewById(R.id.accept_decline_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Review Seeker");

        // Seeker ID got using intent
        seekerID = getIntent().getExtras().get("Seeker's ID").toString();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                acceptRequest();
                declineButton.setVisibility(View.INVISIBLE);
                acceptButton.setText("Request Accepted");
                acceptButton.setEnabled(false);
                acceptButton.getBackground().setColorFilter(new LightingColorFilter(0x8FBC8F, 0x90EE90));

            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineRequest();
                
                //acceptButton.setVisibility(View.INVISIBLE);
                acceptButton.setText("Request Declined");
                acceptButton.setEnabled(false);
                declineButton.setEnabled(false);

                declineButton.setVisibility(View.INVISIBLE);
                acceptButton.setText("Request Rejected");
                acceptButton.setEnabled(false);
                acceptButton.getBackground().setColorFilter(new LightingColorFilter(0x800000, 0xFF6347));

            }
        });

        RetrieveSeekerInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("Users").child(currentUserID)
                .child("seekers").child(seekerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String house_id = dataSnapshot.child("HouseID").getValue().toString();

                databaseReference.child("Users").child(seekerID)
                        .child("houses").child(house_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if(dataSnapshot.child("Request").getValue().toString().equalsIgnoreCase("Accepted"))
                        {
                            declineButton.setVisibility(View.INVISIBLE);
                            acceptButton.setText("Request Accepted");
                            acceptButton.setEnabled(false);
                            acceptButton.getBackground().setColorFilter(new LightingColorFilter(0x8FBC8F, 0x90EE90));
                        }
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

    // Method to initialize fields from xml file
    private void initializeFields() {

        seekerName   = findViewById(R.id.accept_decline_name);
        seekerStatus = findViewById(R.id.accept_decline_status);
        seekerImage  = findViewById(R.id.accept_decline_image);
        acceptButton = findViewById(R.id.accept_button);
        declineButton= findViewById(R.id.decline_button);
    }


    // Method to retrieve seeker information from database
    private void RetrieveSeekerInfo() {
        databaseReference.child("Users").child(seekerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists())&&(dataSnapshot.hasChild("name")))
                {
                    if(dataSnapshot.hasChild("name")){

                        String retrieveHousename = dataSnapshot.child("name").getValue().toString();
                        seekerName.setText(retrieveHousename);
                    }
                    if(dataSnapshot.hasChild("status")){

                        String retrieveHousename = dataSnapshot.child("bio").getValue().toString();
                        seekerStatus.setText(retrieveHousename);
                    }
                    if(dataSnapshot.hasChild("image"))
                    {
                        String retrieveImage = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(retrieveImage).into(seekerImage);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    // Method to accept a join house request from seeker
    private void acceptRequest() {
        databaseReference.child("Users").child(currentUserID)
                .child("seekers").child(seekerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String house_id = dataSnapshot.child("HouseID").getValue().toString();
                // Log.d(TAG, "AcceptOrDecline House ID: "+house_id);
                databaseReference.child("Users").child(seekerID)
                        .child("houses").child(house_id).child("Request").setValue("Accepted");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    // Method to decline a join house request from seeker
    private void declineRequest() {
        databaseReference.child("Users").child(currentUserID)
                .child("seekers").child(seekerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String house_id = dataSnapshot.child("HouseID").getValue().toString();
               // Log.d(TAG, "AcceptOrDecline House ID: "+house_id);
                databaseReference.child("Users").child(seekerID)
                        .child("houses").child(house_id).child("Request").setValue("Rejected");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        databaseReference.child("Users").child(currentUserID)
                .child("seekers").child(seekerID).removeValue();

    }
}
