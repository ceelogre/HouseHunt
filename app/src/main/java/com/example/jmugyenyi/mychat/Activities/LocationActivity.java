package com.example.jmugyenyi.mychat.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.jmugyenyi.mychat.Fragments.PostAHouseFragment;
import com.example.jmugyenyi.mychat.Fragments.mapfragment;
import com.example.jmugyenyi.mychat.R;

public class LocationActivity extends AppCompatActivity {

    FrameLayout mapLayout;
    double latitude;
    double longitude;
    double house_latitude;
    double house_longitude;
    LocationManager lm;
    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1 ;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 111;
    Boolean permission = false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mapLayout = (FrameLayout) findViewById(R.id.mapLayout);
        mapfragment mapF = new mapfragment();
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mapLayout,mapF );
        transaction.addToBackStack(null);
        transaction.commit();

        //getting the house location
       // gethouseLocation();
        //sending the house location
        //sendHouseLocation();

    }

    public void gethouseLocation()
    {
        //getting the data  from the map fragemnt
        Intent intent = this.getIntent();

        /* Obtain String from Intent  */
        if(intent !=null)
        {
            house_latitude = (double)( Integer.valueOf(intent.getExtras().getString("latitude")));
            house_longitude = (double) (Integer.valueOf(intent.getExtras().getString("longitude")));

            Log.d("location", house_latitude + " " + house_longitude);

        }
    }
    public void sendHouseLocation()
    {
        //sending this data to the post house fragment
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", house_latitude);
        bundle.putDouble("longitude", house_longitude);
        // set Fragmentclass Arguments
        PostAHouseFragment postAHouseFragment = new PostAHouseFragment();
        postAHouseFragment.setArguments(bundle);
    }

    public void getLocation (){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);


        } else {
            Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            //fill in TextViews
            latitude = loc.getLatitude();
//            Log.d(TAG, "latitude" + loc.getLatitude());
            longitude = loc.getLongitude();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION: {
                permission = true;
                getLocation();
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {

            }



        }
    }
}
