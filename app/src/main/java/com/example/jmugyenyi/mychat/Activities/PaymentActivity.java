package com.example.jmugyenyi.mychat.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.CardForm;
import com.example.jmugyenyi.mychat.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        CardForm cardForm = findViewById(R.id.payment);
        TextView payText = findViewById(R.id.payment_amount);
        Button payButton = findViewById(R.id.btn_pay);

        payText.setText("$200");
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PaymentActivity.this, "Paid!", Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar =  findViewById(R.id.payments_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Payment");


    }

}
