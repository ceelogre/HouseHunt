package com.example.jmugyenyi.mychat.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.CardForm;
import com.example.jmugyenyi.mychat.PaymentProcess.Charges;
import com.example.jmugyenyi.mychat.PaymentProcess.Response;
import com.example.jmugyenyi.mychat.R;
import com.stripe.android.TokenCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Token;

public class PaymentActivity extends AppCompatActivity implements Response {

   private Response cont = this;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


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
                if(payYear.getText().toString().equals("")||payCard.getText().toString().equals("")||payCVC.getText().toString().equals("")){
                    Toast.makeText(PaymentActivity.this, "Please fill all the required fields", Toast.LENGTH_LONG).show();
                }
                else{
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
                    getToken(card,getApplicationContext());
                }

            }
            }
        });

        Toolbar toolbar =  findViewById(R.id.payments_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Payment");


    }
    public void getToken(com.stripe.android.model.Card card, final Context context){

        Stripe stripe = new Stripe(this, "pk_test_Hci7W3NpdHYpOFUDldaYwyt9");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {

                        // Send token to the server

                        Charges charges = new Charges(token+"");
                        charges.delegate = cont;
                        charges.execute(token+"");


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

    @Override
    public void processFinish(String output) {

        Toast.makeText(getApplicationContext(),
                "The deleting happens here",
                Toast.LENGTH_LONG
        ).show();
    }
}
