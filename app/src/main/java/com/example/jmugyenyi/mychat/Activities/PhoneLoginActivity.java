package com.example.jmugyenyi.mychat.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    // Login in using phone number 
    private Button sendVerCodeButton, verifyButton;
    private EditText inputPhoneNumber, inputVerCode;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private FirebaseAuth mAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        mAuth = FirebaseAuth.getInstance();

        initialiseFields();


        sendVerCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String phoneNumber = inputPhoneNumber.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(PhoneLoginActivity.this, "Phone Number required!", Toast.LENGTH_SHORT).show();
                }else{
                    loadingbar.setTitle("Phone Verification");
                    loadingbar.setMessage("Please Wait!");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS, PhoneLoginActivity.this, mCallbacks);
                }
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerCodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);

                String verificationCode = inputVerCode.getText().toString();

                if (TextUtils.isEmpty(verificationCode)){
                    Toast.makeText(PhoneLoginActivity.this, "No code entered!", Toast.LENGTH_SHORT).show();
                }else
                {
                    loadingbar.setTitle("Code Verification");
                    loadingbar.setMessage("Please Wait!");
                    loadingbar.setCanceledOnTouchOutside(false);
                    loadingbar.show();


                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode );

                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingbar.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "Invalid Phone Number!", Toast.LENGTH_SHORT).show();

                sendVerCodeButton.setVisibility(View.VISIBLE);
                inputPhoneNumber.setVisibility(View.VISIBLE);

                verifyButton.setVisibility(View.INVISIBLE);
                inputVerCode.setVisibility(View.INVISIBLE);
            }


            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                //super.onCodeSent(verificationId, token);
                mVerificationId = verificationId;
                mResendToken = token;
                loadingbar.dismiss();
                Toast.makeText(PhoneLoginActivity.this, "Code successfully sent!", Toast.LENGTH_SHORT).show();

                sendVerCodeButton.setVisibility(View.INVISIBLE);
                inputPhoneNumber.setVisibility(View.INVISIBLE);

                verifyButton.setVisibility(View.VISIBLE);
                inputVerCode.setVisibility(View.VISIBLE);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            loadingbar.dismiss();
                            Toast.makeText(PhoneLoginActivity.this, "Login in success!", Toast.LENGTH_SHORT).show();
                            SendUserToMainActivity();
                        }else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(PhoneLoginActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                });
    }


    private void initialiseFields() {

        sendVerCodeButton = findViewById(R.id.send_ver_code_button);
        verifyButton = findViewById(R.id.verify_button);
        inputPhoneNumber = findViewById(R.id.phone_number_input);
        inputVerCode = findViewById(R.id.verification_code_input);
        loadingbar = new ProgressDialog(this);
    }


    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(PhoneLoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
