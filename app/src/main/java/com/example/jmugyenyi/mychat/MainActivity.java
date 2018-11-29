package com.example.jmugyenyi.mychat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.utils.HouseCRUD;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "MainActivity";



    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;
    private FirebaseUser currentUser;
    private String currentUserId;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference databaseReference;
    private SeekerTabsAdapter seekers;
    private HouseHeadTabsAdapter head;

    final User myUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        currentUserId = "1";

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("HouseHunt");

        myViewPager = findViewById(R.id.main_tabs_pager);

         String myStatus = "House head";//user.getUserStatus();

        if(myStatus == "seeker")
        {
            seekers = new SeekerTabsAdapter(getSupportFragmentManager());
        }
        else if (myStatus == "House head")
        {
            myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if (item.getItemId()== R.id.main_housemates_option)
         {
             SendUserToFindMatesActivity();
         }
        if (item.getItemId()== R.id.main_settings_option)
        {

            SendUserToSettingsActivity();
        }
        if (item.getItemId()== R.id.main_logout_option)
        {
            mFirebaseAuth.signOut();
            SendUserToLoginActivity();
        }

        if (item.getItemId()== R.id.main_create_group_option)
        {
            RequestNewGroup();
        }

        return  true;
    }

    private void RequestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter group name: ");

        final EditText groupnameField = new EditText(MainActivity.this);
        builder.setView(groupnameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupnameField.getText().toString();

                if (TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(MainActivity.this,"Write group name!",Toast.LENGTH_SHORT).show();
                }else
                {
                    CreateNewGroup(groupName);
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.cancel();

            }
        });
        builder.show();
    }

    private void CreateNewGroup(final String groupName) {

        databaseReference.child("Groups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, groupName+" group is created successfully.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null)
        {
            SendUserToLoginActivity();
        }else
        {
            VerifyUser();
        }
    }

    private void VerifyUser() {

         currentUserId = mFirebaseAuth.getCurrentUser().getUid();
        Log.d(TAG, "VerifyUser: "+"User verified ID"+currentUserId);
        databaseReference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists())
                {
                    Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                }else
                {
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        User user = RetrieveUserInfo();

        Log.d(TAG, "VerifyUser: Status: "+user.getUserStatus());
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SendUserToSettingsActivity() {
        String password = getIntent().getExtras().getString("password1");

        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        settingsIntent.putExtra("password2", password);
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
        finish();
    }

    private void SendUserToFindMatesActivity() {
        Intent findMatesIntent = new Intent(MainActivity.this, FindMatesActivity.class);
        startActivity(findMatesIntent);

    }

    private User  RetrieveUserInfo( ) {

        Log.d(TAG, "RetrieveUserInfo: "+ currentUserId);
        databaseReference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image"))) {
                    String retrieveUsername = dataSnapshot.child("name").getValue().toString();
                    String retrieveStatus = dataSnapshot.child("status").getValue().toString();
                    String retrieveProfileImage = dataSnapshot.child("image").getValue().toString();
                    myUser.setUserName(retrieveUsername);
                    myUser.setUserStatus("my turn");

                } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))) {

                    String retrieveUsername = dataSnapshot.child("name").getValue().toString();
                    String retrieveStatus = dataSnapshot.child("status").getValue().toString();

                    myUser.setUserName(retrieveUsername);
                    myUser.setUserStatus(retrieveStatus);

                    Log.d(TAG, "retrieveUser: "+ myUser.getUserStatus());

                    if(myUser.getUserStatus().trim().equalsIgnoreCase("seeker")) {

                        seekers = new SeekerTabsAdapter(getSupportFragmentManager());
                        myViewPager.setAdapter(seekers);
                        myTabLayout = findViewById(R.id.main_tabs);
                        myTabLayout.setupWithViewPager(myViewPager);

                    }
                    else if(myUser.getUserStatus().trim().equalsIgnoreCase("house head")){

                        head = new HouseHeadTabsAdapter(getSupportFragmentManager());
                        myViewPager.setAdapter(head);
                        myTabLayout = findViewById(R.id.main_tabs);
                        myTabLayout.setupWithViewPager(myViewPager);

                    }else{

                        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
                        myViewPager.setAdapter(myTabsAccessorAdapter);
                        myTabLayout = findViewById(R.id.main_tabs);
                        myTabLayout.setupWithViewPager(myViewPager);

                    }
                } else {
                    Toast.makeText(MainActivity.this, "Update Profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d(TAG, "myUser: "+myUser.getUserStatus());
        return myUser;
    }

}
