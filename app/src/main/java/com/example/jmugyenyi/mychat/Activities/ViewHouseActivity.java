package com.example.jmugyenyi.mychat.Activities;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private Button joinHouseButton;
    
    private String joiningHouseID,currentUserID, current_State, ownerhouseID,interestID;
    private DatabaseReference databaseReference, userRef;
    private FirebaseAuth mfirebaseAuth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house);
        initializeFields();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();
        joiningHouseID = getIntent().getExtras().get("visit_house_id").toString();
        ownerhouseID = getIntent().getExtras().get("ownerID").toString();


        // Method to retrieve House Details
        RetrieveHouseInfo(joiningHouseID);

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
        joinHouseButton = findViewById(R.id.join_button);
        current_State = "Request_Not_Sent";
    }

    private void joinHouse() {

        InterestCRUD interestCRUD = new InterestCRUD(mfirebaseAuth);
        interestCRUD.createInterestTable(currentUserID,joiningHouseID,ownerhouseID);


    }

    private void RetrieveHouseInfo(String retrieveInfo) {




        databaseReference.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists())&&(dataSnapshot.hasChild("houses"))) {


                    String houseid = dataSnapshot.child(currentUserID).child(joiningHouseID).getKey();

                   // Log.d(TAG, "Getting to the end 2: "+joiningHouseID);

                    for (DataSnapshot test :dataSnapshot.getChildren()) {

                        if (test.getValue().toString().contains(joiningHouseID))
                        {
                            Log.d(TAG, "Getting : " + test.getValue());

                            {
                                joinHouseButton.setText("Cancel Join Request");
                                current_State ="Request_Sent";
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       // Log.d(TAG, "RetrieveHouseInfo: "+retrieveInfo);
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

                    ManageJoinRequests();

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

    private void ManageJoinRequests() {

        joinHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current_State.equalsIgnoreCase("Request_Not_Sent"))
                {
                   // Log.d(TAG, "Join request worked!: ");
                    joinHouse();
//                    joinHouseButton.setText("Cancel Join Request");
//                    current_State = "Request_Sent";
                }
                if (current_State.equalsIgnoreCase("Request_Sent"))
                {
                    //Log.d(TAG, "Cancel request worked: ");
                    CancelJoinHouseRequest();
                    joinHouseButton.setText("Join House");
                    current_State = "Request_Not_Sent";
                }

            }
        });

    }

    private void CancelJoinHouseRequest() {

        databaseReference.child("Users").child(currentUserID).child("houses").child(joiningHouseID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Log.d(TAG, "Finally entered the loop: ");
                         if(task.isSuccessful())
                        {
                            Log.d(TAG, "Total success!!!!: ");
                            joinHouseButton.setText("Join House");
                            current_State="Request_Not_Sent";
                        }
                    }
                });
        databaseReference.child("Users").child(ownerhouseID).child("seekers").child(currentUserID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Log.d(TAG, "Finally entered the loop: ");
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "Total success!!!!: ");
                            joinHouseButton.setText("Join House");
                            current_State="Request_Not_Sent";
                        }

                    }
                });


    }
}
