package com.example.jmugyenyi.mychat.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

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

    private View houseFacilitiesFragmentView;
//    private ListView listView;
//    private ArrayAdapter<String> arrayAdapter;
//    private ArrayList<String> listOfGroups = new ArrayList<>();
//    private DatabaseReference databaseReference;
    private TextView date,time;
    private Button dateButton,timeButton;

    Calendar calendar;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;


    public HouseFacilitiesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        houseFacilitiesFragmentView = inflater.inflate(R.layout.fragment_house_facilities, container, false);

        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups");


        IntializeFields();

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
                    }
                },hour,minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
                timePickerDialog.show();
            }
        });

      //  RetrieveAndDisplayGroups();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String currentGroupName = parent.getItemAtPosition(position).toString();
//
//                Intent groupChatIntent = new Intent (getContext(),HouseChatActivity.class);
//                groupChatIntent.putExtra("groupName",currentGroupName);
//                startActivity(groupChatIntent);
//            }
//        });

        return  houseFacilitiesFragmentView;
    }

//    private void RetrieveAndDisplayGroups() {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Set <String> set = new HashSet<>();
//                Iterator iterator = dataSnapshot.getChildren().iterator();
//
//                while (iterator.hasNext()){
//                    set.add(((DataSnapshot)iterator.next()).getKey());
//                }
//
//                listOfGroups.clear();
//                listOfGroups.addAll(set);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void IntializeFields() {

//        listView = groupFragmentView.findViewById(R.id.list_view);
//        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,listOfGroups);
//        listView.setAdapter(arrayAdapter);
        date = houseFacilitiesFragmentView.findViewById(R.id.date);
        dateButton = houseFacilitiesFragmentView.findViewById(R.id.date_button);
        time = houseFacilitiesFragmentView.findViewById(R.id.time);
        timeButton = houseFacilitiesFragmentView.findViewById(R.id.time_button);
    }

}
