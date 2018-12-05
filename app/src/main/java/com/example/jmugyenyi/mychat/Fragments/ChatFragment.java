package com.example.jmugyenyi.mychat.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private View chatView;

    private ImageButton sendImageButton;
    private EditText userMsgInput;
    private ScrollView scrollView;
    private TextView displayMessage;

    private String currentGroupName,currentUserID, currentUserName, currentDate, currentTime;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, groupNameref, groupMessageKeyRef, houseChatRef;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatView = inflater.inflate(R.layout.fragment_chat, container, false);

        currentGroupName = "My House";

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        InitializeFields();
        GetUserInfo();

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMessageInfoToDataBase();
                userMsgInput.setText("");
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        return chatView;
    }

    @Override
    public void onStart() {
        super.onStart();


        databaseReference.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String chatName= "My House";

                if ((dataSnapshot.exists())&&(dataSnapshot.hasChild("chat")))
                {
                    chatName = dataSnapshot.child("chat").getValue().toString();
                }

                groupNameref = FirebaseDatabase.getInstance().getReference().child("Groups").child(chatName);
                groupNameref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists())
                        {
                            DisplayMessages(dataSnapshot);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if (dataSnapshot.exists())
                        {
                            //DisplayMessages(dataSnapshot);
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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



       // houseChatRef= FirebaseDatabase.getInstance().getReference().child("House Chats");


        //houseChatRef

    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext())
        {
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            displayMessage.append(chatName+ " :\n"+chatMessage+" :\n"+ chatTime+"     "+chatDate+"\n\n\n");

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    private void InitializeFields() {

        sendImageButton = chatView.findViewById(R.id.send_message_button);
        userMsgInput = chatView.findViewById(R.id.input_group_message);
        scrollView = chatView.findViewById(R.id.myChatScrollView);
        displayMessage = chatView.findViewById(R.id.group_chat_text_display);
    }


    private void GetUserInfo() {
        databaseReference.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    currentUserName= dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SaveMessageInfoToDataBase() {
        String message = userMsgInput.getText().toString();
        String messageKey = groupNameref.push().getKey();

        if (TextUtils.isEmpty(message))
        {
            Toast.makeText(getActivity(), "Write Message!", Toast.LENGTH_SHORT).show();
        }else{
            Calendar dateStamp = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(dateStamp.getTime());


            Calendar timeStamp = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(timeStamp.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupNameref.updateChildren(groupMessageKey);
            groupMessageKeyRef = groupNameref.child(messageKey);


            HashMap<String,Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name",currentUserName);
            messageInfoMap.put("message",message);
            messageInfoMap.put("date",currentDate);
            messageInfoMap.put("time",currentTime);

            groupMessageKeyRef.updateChildren(messageInfoMap);
        }

    }
}
