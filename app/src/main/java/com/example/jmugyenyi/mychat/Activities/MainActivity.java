package com.example.jmugyenyi.mychat.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.model.ParentUser;
import com.example.jmugyenyi.mychat.model.User;
import com.example.jmugyenyi.mychat.model.UserStatusFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "MainActivity";



    // Instance Variables
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private FirebaseUser currentUser;
    private String currentUserId;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference databaseReference;
    private ParentUser userStatus;


    final User myUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        currentUserId = "1";// Temporary assign 1 to UserID, (temporary fix for a bug, permanent fix to be added later)

        // Assign title to Toolbar
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("HouseHunt");

        myViewPager = findViewById(R.id.main_tabs_pager); // Set title for MainActivity toolbar

    }

    // Menu fragment methods for logout, settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if (item.getItemId()== R.id.main_settings_option)
        {
            SendUserToSettingsActivity();
        }
        if (item.getItemId()== R.id.main_logout_option)
        {
            mFirebaseAuth.signOut();
            SendUserToLoginActivity();
        }
        return  true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null)
        {
            // Send user to login activity if no account of the user is found
            SendUserToLoginActivity();
        }else
        {
            VerifyUser();
        }
    }

    // Method to verify user credential on app start-up. If failed, send user to settings activity
    private void VerifyUser() {

         currentUserId = mFirebaseAuth.getCurrentUser().getUid();
        Log.d(TAG, "VerifyUser: "+"User verified ID"+currentUserId);
        databaseReference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists())
                {
                    // Display toast if user profile exists
                    Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                }else
                {
                    SendUserToSettingsActivity(); // Send user to settings activity if they don't yet have a profile
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        User user = RetrieveUserInfo();

        Log.d(TAG, "VerifyUser: Status: "+user.getStatus());
    }

    //Method to send user to login activity
    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    // Method to send user to settings activity
    private void SendUserToSettingsActivity() {

        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


    // Method to retrieve current user profile info from firebase
    private User RetrieveUserInfo( ) {

        Log.d(TAG, "RetrieveUserInfo: "+ currentUserId);
        databaseReference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image"))) {
                    String retrieveUsername = dataSnapshot.child("name").getValue().toString();
                    String retrieveStatus = dataSnapshot.child("status").getValue().toString();
                    String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();


                    myUser.setName(retrieveUsername);
                    myUser.setStatus(retrieveStatus);

                   // Log.d(TAG, "retrieveUser: "+ myUser.getStatus());

                    String setUser = myUser.getStatus().trim();

                    // Call Factory to create User based of status which was selected
                    userStatus = new UserStatusFactory().createUser(setUser,getSupportFragmentManager());
                    getSupportActionBar().setTitle(setUser);
                    myViewPager.setAdapter(userStatus);
                    myTabLayout = findViewById(R.id.main_tabs);
                    myTabLayout.setupWithViewPager(myViewPager);

                } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {

                    String retrieveUsername = dataSnapshot.child("name").getValue().toString();
                    String retrieveStatus = dataSnapshot.child("status").getValue().toString();

                    myUser.setName(retrieveUsername);
                    myUser.setStatus(retrieveStatus);

                    Log.d(TAG, "retrieveUser: "+ myUser.getStatus());

                    String setUser = myUser.getStatus().trim();

                    // Call Factory to create User based of status which was selected
                    userStatus = new UserStatusFactory().createUser(setUser,getSupportFragmentManager());
                    getSupportActionBar().setTitle(setUser);
                    myViewPager.setAdapter(userStatus);
                    myTabLayout = findViewById(R.id.main_tabs);
                    myTabLayout.setupWithViewPager(myViewPager);

                } else {
                    Toast.makeText(MainActivity.this, "Update Profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d(TAG, "myUser: "+myUser.getStatus());
        return myUser;
    }

}
