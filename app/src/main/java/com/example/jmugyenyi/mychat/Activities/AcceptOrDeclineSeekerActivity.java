package com.example.jmugyenyi.mychat.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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


    private TextView seekerName, seekerStatus;
    private CircleImageView seekerImage;

    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference databaseReference;

    private String seekerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_or_decline_seeker);



        databaseReference = FirebaseDatabase.getInstance().getReference();

        initializeFields();
        Toolbar toolbar =  findViewById(R.id.accept_decline_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Review Seeker");

        seekerID = getIntent().getExtras().get("Seeker's ID").toString();

        Toast.makeText(this, seekerID, Toast.LENGTH_SHORT).show();

        RetrieveSeekerInfo();
    }


    private void initializeFields() {

        seekerName   = findViewById(R.id.accept_decline_name);
        seekerStatus = findViewById(R.id.accept_decline_status);
        seekerImage  = findViewById(R.id.accept_decline_image);
        //toolbar      = findViewById(R.id.accept_decline_toolbar);
    }


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

                        String retrieveHousename = dataSnapshot.child("status").getValue().toString();
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

}
