package com.example.jmugyenyi.mychat.Activities;

import android.content.Context;
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
import com.example.jmugyenyi.mychat.PaymentProcess.Charges;
import com.example.jmugyenyi.mychat.PaymentProcess.Response;
import com.example.jmugyenyi.mychat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.TokenCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Token;

//This Activity manages of rent payments
public class PaymentActivity extends AppCompatActivity implements Response {

    protected static final String TAG = "PaymentActivity";

    private Button payButton;
    private TextView payText;
    private CardForm cardForm;

    private String groupChat = "";
    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference databaseReference;

    private String houseID, currentUserID;
    private Response cont = this;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //Getting the database reference and the identity of the current user
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();

        houseID = getIntent().getExtras().get("house_id").toString();
        //Getting the card details
        CardForm cardForm = findViewById(R.id.payment);
        final TextView payText = findViewById(R.id.payment_amount);
        final TextView payYear = findViewById(R.id.card_preview_expiry);
        final TextView payCard = findViewById(R.id.card_preview_number);
        final TextView payCVC = findViewById(R.id.card_preview_cvc);
        Button payButton = findViewById(R.id.btn_pay);

        payText.setText("$200");

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checking if the user filled all fields needed for the payment processes
                if (payYear.getText().toString().equals("") || payCard.getText().toString().equals("") || payCVC.getText().toString().equals("")) {
                    Toast.makeText(PaymentActivity.this, "Please fill all the required fields", Toast.LENGTH_LONG).show();
                } else {

                    final String remove_Request = "dont_remove";

                    //Checking if the user has been accepted into the house
                    databaseReference.child("House").child(houseID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            groupChat = dataSnapshot.child("houseChat").getValue().toString();

                            Log.d(TAG, "House DataBase: " + houseID);

                            databaseReference.child("Users").child(currentUserID).child("houses").child(houseID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String requestStatus = dataSnapshot.child("Request").getValue().toString();

                                    Log.d(TAG, "onDataChange: " + requestStatus);

                                    if (requestStatus.equalsIgnoreCase("Pending")) {
                                        Toast.makeText(PaymentActivity.this, "Your Request Still Pending!", Toast.LENGTH_SHORT).show();
                                    } else if (requestStatus.equalsIgnoreCase("Rejected")) {
                                        Toast.makeText(PaymentActivity.this, "Your Request was Rejected!", Toast.LENGTH_SHORT).show();
                                    } else if (requestStatus.equalsIgnoreCase("Accepted")) {

                                        //Validating card information
                                        String dates[] = payYear.getText().toString().split("/");

                                        com.stripe.android.model.Card card = new com.stripe.android.model.Card(
                                                payCard.getText().toString(),
                                                Integer.valueOf(dates[0]),
                                                Integer.valueOf(dates[1]),
                                                payCVC.getText().toString()
                                        );

                                        card.validateNumber();
                                        card.validateCVC();


                                        if (!card.validateCard()) {
                                            Toast.makeText(PaymentActivity.this, "wrong card inputs!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(PaymentActivity.this, "getting token!", Toast.LENGTH_SHORT).show();
                                            getToken(card, getApplicationContext());
                                        }


                                    } else {
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

            }
        });


        Toolbar toolbar = findViewById(R.id.payments_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Payment");


    }

    /**
     * This method requests the token from Stripe for payments
     *
     * @param card:    The user's card
     * @param context: The main activity context
     */
    public void getToken(com.stripe.android.model.Card card, final Context context) {

        Stripe stripe = new Stripe(this, "pk_test_Hci7W3NpdHYpOFUDldaYwyt9");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {

                        // Send token to the server

                        Charges charges = new Charges(token + "");
                        charges.delegate = cont;
                        charges.execute(token + "");


                        Toast.makeText(getApplicationContext(),
                                "Processing your payments",
                                Toast.LENGTH_LONG
                        ).show();
                    }

                    public void onError(Exception error) {
                        Toast.makeText(getApplicationContext(),
                                "Wrong inputs",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    /**
     * This function receives feedback once the payments finished processing and changes the status of the seeker to housemate
     */
    @Override
    public void processFinish(String output) {

        databaseReference.child("Users").child(currentUserID).child("status").setValue("house mate");
        databaseReference.child("Users").child(currentUserID).child("chat").setValue(groupChat);
        databaseReference.child("Users").child(currentUserID).child("My House").setValue(houseID);
        databaseReference.child("House").child(houseID).child("HouseMates").child(currentUserID).child("Mates").setValue("Yes");


        databaseReference.child("House").child(houseID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String houseOwnerID = dataSnapshot.child("authenticatedUserId").getValue().toString();

                databaseReference.child("Users").child(houseOwnerID).child("seekers").child(currentUserID).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
