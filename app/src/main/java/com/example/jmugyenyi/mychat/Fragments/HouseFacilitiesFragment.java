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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class HouseFacilitiesFragment extends Fragment {


    protected static final String TAG = "HouseFacilitiesFragment";

    private View houseFacilitiesFragmentView;
//    private ListView listView;
//    private ArrayAdapter<String> arrayAdapter;
//    private ArrayList<String> listOfGroups = new ArrayList<>();
//    private DatabaseReference databaseReference;
    private TextView date,time;
    private Button dateButton,timeButton,smsButton,callButton;

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    private  static  String message ;

    Calendar calendar;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;


    public HouseFacilitiesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

        return  houseFacilitiesFragmentView;
    }


    private void IntializeFields() {

        date = houseFacilitiesFragmentView.findViewById(R.id.date);
        dateButton = houseFacilitiesFragmentView.findViewById(R.id.date_button);
        time = houseFacilitiesFragmentView.findViewById(R.id.time);
        timeButton = houseFacilitiesFragmentView.findViewById(R.id.time_button);
        smsButton = houseFacilitiesFragmentView.findViewById(R.id.message_button);
        callButton = houseFacilitiesFragmentView.findViewById(R.id.call_button);
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
