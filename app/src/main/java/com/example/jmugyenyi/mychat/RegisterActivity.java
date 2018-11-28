package com.example.jmugyenyi.mychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.utils.HouseCRUD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.House;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText userEmail, userPassword;
    private TextView alreadyHaveAccountLink;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initialiseFields();
        alreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUserToLoginActivity();
            }
        });
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }

    private void CreateNewAccount() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email...",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password...",Toast.LENGTH_SHORT).show();
        }

        loadingBar.setTitle("Creating new Account");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    HouseCRUD p = new HouseCRUD(mFirebaseAuth);
                    p.getAvailableHouses();
                    String currentUserID = mFirebaseAuth.getCurrentUser().getUid();
                    databaseReference.child("Users").child(currentUserID).setValue("");
                    SendUserToMainActivity();
                    Toast.makeText(RegisterActivity.this, "Account created successfully",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }else
                {
                    String message = task.getException().toString();
                    Toast.makeText(RegisterActivity.this, "Error: "+message,Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });



    }

    private void initialiseFields() {
        createAccountButton = findViewById(R.id.register_button);
        userEmail = findViewById(R.id.register_email);
        userPassword = findViewById(R.id.register_password);
        alreadyHaveAccountLink = findViewById(R.id.already_have_account_link);

        loadingBar = new ProgressDialog(this);

    }
    private void SendUserToLoginActivity() {

        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.putExtra("status", userPassword.getText().toString());
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
