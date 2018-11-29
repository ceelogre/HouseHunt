package com.example.jmugyenyi.mychat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ViewHouseActivity extends AppCompatActivity {

    
    private String receiverHouseID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house);
        
        
        receiverHouseID = getIntent().getExtras().get("visit_house_id").toString();

        Toast.makeText(this, "House ID: "+ receiverHouseID, Toast.LENGTH_SHORT).show();
    }
}
