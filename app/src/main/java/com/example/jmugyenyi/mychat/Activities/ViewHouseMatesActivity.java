package com.example.jmugyenyi.mychat.Activities;

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

import com.example.jmugyenyi.mychat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHouseMatesActivity extends AppCompatActivity {


    protected static final String TAG = "ViewHouseMatesActivity";
    private TextView houseMateName, houseMateStatus;
    private CircleImageView houseMateImage;


    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference databaseReference;

    private String houseMateID, currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house_mates);
        Toolbar toolbar =  findViewById(R.id.house_mates_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My HouseMates");



        databaseReference = FirebaseDatabase.getInstance().getReference();
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();

        initializeFields();

        houseMateID = getIntent().getExtras().get("House Mate ID").toString();
        Log.d(TAG, "onCreate: "+houseMateID);



        RetrieveHouseMateInfo();
    }


    private void RetrieveHouseMateInfo() {
        databaseReference.child("Users").child(houseMateID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if((dataSnapshot.exists())&&(dataSnapshot.hasChild("name")))
                {
                    if(dataSnapshot.hasChild("name")){

                        String retrieveHousename = dataSnapshot.child("name").getValue().toString();
                        houseMateName.setText(retrieveHousename);
                    }
                    if(dataSnapshot.hasChild("status")){

                        String retrieveHousename = dataSnapshot.child("bio").getValue().toString();
                        houseMateStatus.setText(retrieveHousename);
                    }
                    if(dataSnapshot.hasChild("image"))
                    {
                        String retrieveImage = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(retrieveImage).into(houseMateImage);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void initializeFields() {

        houseMateName   = findViewById(R.id.housemate_name);
        houseMateStatus = findViewById(R.id.housemate_status);
        houseMateImage  = findViewById(R.id.housemate_image);
    }

}
