package com.example.jmugyenyi.mychat.Fragments;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.model.House;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseFacilitiesFragment extends Fragment {


    protected static final String TAG = "HouseFacilitiesFragment";

    private View houseFacilitiesFragmentView;

    private TextView date,time;
    private Button dateButton,timeButton,smsButton,callButton, transportButton;

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    private  static  String message,pick_up_id,currentUserID ;

    Calendar calendar;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private DatabaseReference databaseReference, houseRef, userRef,locationRef;
    private FirebaseAuth mfirebaseAuth;


    public HouseFacilitiesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mfirebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Pick-Ups");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        houseRef =FirebaseDatabase.getInstance().getReference().child("House");

        houseFacilitiesFragmentView = inflater.inflate(R.layout.fragment_house_facilities, container, false);


        IntializeFields();


        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = "+250787799082";
                Intent callIntent = new Intent (Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));
                startActivity(callIntent);
            }
        });

        sendSMS();

        transportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(transportButton.getText().toString().equalsIgnoreCase("MAKE REQUEST"))
                {
                    DatabaseReference newRef = databaseReference.push();
                    pick_up_id = newRef.getKey();
                    userRef.child(currentUserID).child("My Pick-Up").setValue(pick_up_id);

                    userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String houseID = dataSnapshot.child("My House").getValue().toString();

                            //Log.d(TAG, "onDataChange: "+houseID);

                            houseRef.child(houseID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String latitude = dataSnapshot.child("latitude").getValue().toString();
                                    String longitude = dataSnapshot.child("longitude").getValue().toString();



                                    HashMap<String, Object> pickupRequestKey = new HashMap<>();
                                    databaseReference.updateChildren(pickupRequestKey);
                                    locationRef = databaseReference.child(pick_up_id);


                                    HashMap<String, Object> locationInfoMap = new HashMap<>();
                                    locationInfoMap.put("latitude", latitude);
                                    locationInfoMap.put("longitude", longitude);


                                    locationRef.updateChildren(locationInfoMap);
                                    transportButton.setText("CANCEL REQUEST");


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
                }else if(transportButton.getText().toString().equalsIgnoreCase("CANCEL REQUEST"))
                {

                    transportButton.setText("MAKE REQUEST");
                    userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                           // if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("My Pick-Up"))) {
                               // String myPickUpId = dataSnapshot.child("My Pick-Up").getValue().toString();

                                databaseReference.child(dataSnapshot.child("My Pick-Up").getValue().toString()).removeValue();



                            }
                       // }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }
        });


        return  houseFacilitiesFragmentView;
    }


    @Override
    public void onStart() {
        super.onStart();

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("My Pick-Up")))
                {
                    transportButton.setText("CANCEL REQUEST");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void IntializeFields() {

        date = houseFacilitiesFragmentView.findViewById(R.id.date);
        dateButton = houseFacilitiesFragmentView.findViewById(R.id.date_button);
        time = houseFacilitiesFragmentView.findViewById(R.id.time);
        timeButton = houseFacilitiesFragmentView.findViewById(R.id.time_button);
        smsButton = houseFacilitiesFragmentView.findViewById(R.id.message_button);
        callButton = houseFacilitiesFragmentView.findViewById(R.id.call_button);
        transportButton = houseFacilitiesFragmentView.findViewById(R.id.transport_request_button);
    }

    private void sendSMS() {

        //textButton.setEnabled(false);

        if (checkPermission(Manifest.permission.SEND_SMS)) {
            smsButton.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }



        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth+ "/"+ month+"/"+year);
                        message = "We require your laundry services, Please pass by on: "+date.getText().toString()+" at:"+time.getText().toString();
                        Log.d(TAG, "listner: "+date.getText().toString());
                    }
                },day,month,year);
                datePickerDialog.show();
            }
        });


        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay+":"+minute);
                        message = "We require your laundry services, Please pass by on: "+date.getText().toString()+" at:"+time.getText().toString();
                    }
                },hour,minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
                timePickerDialog.show();
            }
        });


        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "+250739832241";
                //String phoneNumber = "+250788231926";

                if (phoneNumber == null || phoneNumber.length() == 0 || message== null || message.length() == 0)
                {
                    return;
                }

                if (checkPermission((Manifest.permission.SEND_SMS)))
                {
                    smsMessageSent(phoneNumber, message);
                }

            }
        });
    }

    // Method to check permission
    private boolean checkPermission(String permission) {

        int check = ContextCompat.checkSelfPermission(getActivity(), permission);

        return (check== PackageManager.PERMISSION_GRANTED);

    }

    // Method to send message to hardcoded phone number
    // method has broadcast receivers to monitor sent and delivered messages
    public void smsMessageSent(String phoneNumber, String message)
    {
        String smsSent = "SMS_SENT";
        String smsDelivered = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0,
                new Intent(smsSent), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,
                new Intent(smsDelivered), 0);



        // Receiver for Sent SMS.
        getActivity().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getActivity().getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getActivity().getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(smsSent));

        // Receiver for Delivered SMS.
        getActivity().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:

                        Toast.makeText(getActivity().getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity().getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(smsDelivered));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
