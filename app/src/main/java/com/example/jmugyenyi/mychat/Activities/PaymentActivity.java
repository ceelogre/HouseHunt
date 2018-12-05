package com.example.jmugyenyi.mychat.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.CardForm;
import com.example.jmugyenyi.mychat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentActivity extends AppCompatActivity {

    protected static final String TAG = "PaymentActivity";

    private Button payButton;
    private TextView payText;
    private CardForm cardForm;


    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference databaseReference;

    private String houseID, currentUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();

        cardForm = findViewById(R.id.payment);
        payText = findViewById(R.id.payment_amount);
        payButton = findViewById(R.id.btn_pay);

        houseID = getIntent().getExtras().get("house_id").toString();
       // Log.d(TAG, "onCreate HouseID: "+houseID);

        //Toast.makeText(PaymentActivity.this, houseID, Toast.LENGTH_SHORT).show();

        payText.setText("$200");
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String remove_Request = "dont_remove";



            databaseReference.child("House").child(houseID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String groupChat = dataSnapshot.child("houseChat").getValue().toString();

                    Log.d(TAG, "House DataBase: "+houseID);

                    databaseReference.child("Users").child(currentUserID).child("houses").child(houseID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String requestStatus = dataSnapshot.child("Request").getValue().toString();

                            Log.d(TAG, "onDataChange: "+requestStatus);

                            if(requestStatus.equalsIgnoreCase("Pending"))
                            {
                                Toast.makeText(PaymentActivity.this, "Your Request Still Pending!", Toast.LENGTH_SHORT).show();
                            }else if(requestStatus.equalsIgnoreCase("Rejected"))
                            {
                                Toast.makeText(PaymentActivity.this, "Your Request was Rejected!", Toast.LENGTH_SHORT).show();
                            }else if(requestStatus.equalsIgnoreCase("Accepted"))
                            {


                                //Log.d(TAG, "Accepted loop: "+houseID);
                                databaseReference.child("Users").child(currentUserID).child("status").setValue("house mate");
                                databaseReference.child("Users").child(currentUserID).child("chat").setValue(groupChat);


                                databaseReference.child("House").child(houseID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       // Log.d(TAG, "Remove Seeker: ");
                                        String houseOwnerID = dataSnapshot.child("authenticatedUserId").getValue().toString();
                                        //Log.d(TAG, "Remove Seeker123: "+houseOwnerID);
                                        databaseReference.child("Users").child(houseOwnerID).child("seekers").child(currentUserID).removeValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });




                            }else
                            {
                                Toast.makeText(PaymentActivity.this, "Unknown Request Status", Toast.LENGTH_SHORT).show();
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
        });


//        databaseReference.child("House").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String houseOwnerID = dataSnapshot.child("authenticatedUserId").getValue().toString();
//                databaseReference.child("Users").child(houseOwnerID).child("seekers").child(currentUserID).removeValue();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        Toolbar toolbar =  findViewById(R.id.payments_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");


    }

}
