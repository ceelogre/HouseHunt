package com.example.jmugyenyi.mychat.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jmugyenyi.mychat.R;
import com.example.jmugyenyi.mychat.utils.RoomCRUD;

public class ViewHouseActivity extends AppCompatActivity {

    private RoomCRUD roomTableRetriever;
    private String receiveHouseID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house);

        //roomTableRetriever = new RoomCRUD();
        
        receiveHouseID = getIntent().getExtras().get("visit_house_id").toString();
        //getRoomDetails(receiveHouseID);

        Toast.makeText(this, "House ID: "+ receiveHouseID, Toast.LENGTH_SHORT).show();
    }

    public void getRoomDetails(String receiveHouseID){

        roomTableRetriever.getRoomDetails(receiveHouseID);
    }

}
