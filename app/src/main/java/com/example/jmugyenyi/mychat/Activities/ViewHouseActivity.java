package com.example.jmugyenyi.mychat.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.utils.HouseCRUD;
import com.example.jmugyenyi.mychat.utils.InterestCRUD;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHouseActivity extends AppCompatActivity {

    protected static final String TAG = "ViewHouseActivity";
    private TextView hseName, hseStreet, rentAmount,hseCity,hseCountry, hseMates,hseRooms;
    private CircleImageView hseImage;
    private Button join;
    
    private String receiverHouseID,currentUserID;
    private DatabaseReference databaseReference;
    private FirebaseAuth mfirebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house);
        initializeFields();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();
        receiverHouseID = getIntent().getExtras().get("visit_house_id").toString();



        
        


        RetrieveHouseInfo(receiverHouseID);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinHouse();
            }
        });

        //Toast.makeText(this, "House ID: "+ receiverHouseID, Toast.LENGTH_SHORT).show();
    }


    private void initializeFields() {

        hseName   = findViewById(R.id.visit_house_name);
        hseStreet = findViewById(R.id.visit_house_street);
        hseImage  = findViewById(R.id.visit_house_image);
        rentAmount = findViewById(R.id.view_rent_amount);
        hseCity = findViewById(R.id.view_city_name);
        hseCountry = findViewById(R.id.view_country_name);
        hseRooms = findViewById(R.id.view_room_number);
        hseMates = findViewById(R.id.view_mates_number);
        join = findViewById(R.id.join_button);
    }

    private void joinHouse() {

        InterestCRUD interestCRUD = new InterestCRUD(mfirebaseAuth);
        interestCRUD.createInterestTable(currentUserID,receiverHouseID);
        //houseCRUD.addRoomToHouse();
    }

    private void RetrieveHouseInfo(String retrieveInfo) {

        Log.d(TAG, "RetrieveHouseInfo: "+retrieveInfo);
        databaseReference.child("House").child(retrieveInfo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists())&&(dataSnapshot.hasChild("houseId")))
                {
                    if(dataSnapshot.hasChild("houseName")){

                        String retrieveHousename = dataSnapshot.child("houseName").getValue().toString();
                        hseName.setText(retrieveHousename);
                    }
                    if(dataSnapshot.hasChild("image"))
                    {
                        String retrieveHouseImage = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(retrieveHouseImage).into(hseImage);
                    }

                    if(dataSnapshot.hasChild("wholeHouseRent"))
                    {
                        String retrieveRent = dataSnapshot.child("wholeHouseRent").getValue().toString();
                        rentAmount.setText(retrieveRent);
                    }
                    if(dataSnapshot.hasChild("street"))
                    {
                        String retrieveStreet = dataSnapshot.child("street").getValue().toString();
                        hseStreet.setText(retrieveStreet);
                    }

                    if(dataSnapshot.hasChild("city"))
                    {
                        String retrieveCity = dataSnapshot.child("city").getValue().toString();
                        hseCity.setText(retrieveCity);
                    }
                    if(dataSnapshot.hasChild("country"))
                    {
                        String retrieveCountry = dataSnapshot.child("country").getValue().toString();
                        hseCountry.setText(retrieveCountry);
                    }
                    if(dataSnapshot.hasChild("numberOfHousemates"))
                    {
                        String retrieveNumberOfHousemates = dataSnapshot.child("numberOfHousemates").getValue().toString();
                        hseMates.setText(retrieveNumberOfHousemates);
                    }
                    if(dataSnapshot.hasChild("numberOfRooms"))
                    {
                        String retrieveNumberOfRooms = dataSnapshot.child("numberOfRooms").getValue().toString();
                        hseRooms.setText(retrieveNumberOfRooms);
                    }

                }else
                {
                    Toast.makeText(ViewHouseActivity.this,"Update House!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
